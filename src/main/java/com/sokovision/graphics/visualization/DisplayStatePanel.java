/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sokovision.graphics.visualization;

import com.sokovision.graphics.support.GridRectangle;
import com.sokovision.graphics.support.ImagePacker;
import com.sokovision.grid.Grid;
import com.sokovision.grid.Position;
import com.sokovision.grid.Space;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import com.sokovision.plugin.Solver;
import com.sokovision.problem.Node;
import com.sokovision.problem.State;

/**
 *
 * @author anei
 */
public class DisplayStatePanel extends JPanel implements MouseListener, MouseMotionListener {
    
    private final ImagePacker packer;
    
    private Grid grid;
    private Solver solver;
    private Node state;
    
    private final GridRectangle gridRect;
    
    private int tileSize = 30;
    
    private int mouseX;
    private int mouseY;
    
    /**
     * Creates new instance of class DisplayImagePanel.
     * @param packer The image buffer.
     * @param grid The problem grid.
     * @param gridWidth
     * @param gridHeight
     * @param state
     */
    public DisplayStatePanel(ImagePacker packer, Grid grid, int gridWidth, int gridHeight, Node state) {
        this.packer = packer;
        this.grid = grid;
        this.state = state;
        this.gridRect = new GridRectangle(0, 0, gridWidth, gridHeight);
        initListeners();
    }
    
    
    private void initListeners() {
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    
    public void resizeGrid() {
        gridRect.x = getWidth() / 10;
        gridRect.y = getHeight() / 10;
        
        if (tileSize > 5) {
            boolean tooWide = false;
            boolean tooHigh = false;

            // check if grid is too wide
            if (gridRect.width * tileSize >= getWidth())
                tooWide = true;

            // check if grid is too high
            if (gridRect.height * tileSize >= getHeight())
                tooHigh = true;


            // resize tile size accordingly
            if (tooWide || tooHigh) {
                tileSize -= 5;
                resizeGrid();
            }
        }
    }
    
    public void updateState(Node newState) {
        state = newState;
        repaint();
    }
    
    public void updateGridSize(int gridWidth, int gridHeight) {
        gridRect.width = gridWidth;
        gridRect.height = gridHeight;
    }
    
    public void updateGrid(Grid grid) {
        this.grid = grid;
    }
    
    public State getCurrentState() {
        return state.state;
    }
    
    public void setCurrentState(State newState) {
        state.state = newState;
    }
    
    
    private Position calcTilePos(Position pos) {
        int tileX = gridRect.x + (pos.getX() * tileSize);
        int tileY = gridRect.y + (pos.getY() * tileSize);
        
        return new Position(tileX, tileY);
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        // call parent method
        super.paintComponent(g);
        
        // draw walls and free spaces
        for (Position pos : grid.getPositions()) {
            Space s = grid.getSpace(pos);
            Position p = calcTilePos(pos);
            
            if (s.type == Space.WALL || s.type == Space.FREE) {
                g.drawImage(packer.getImage(s.type), p.getX(), p.getY(), tileSize + 1, tileSize + 1, this);
                if (s.isDead) {
                    g.drawImage(packer.getImage(ImagePacker.DEAD), p.getX(), p.getY(), tileSize + 1, tileSize + 1, this);
                }
            } else {
                System.err.println("FATAL ERROR: EditImagePanel@paintComponent -> Space on grid has illegal type.");
                System.exit(1);
            }
        }
        
        // draw goals
        for (Position pos : grid.getGoals()) {
            Position p = calcTilePos(pos);
            g.drawImage(packer.getImage(ImagePacker.GOAL), p.getX(), p.getY(), tileSize + 1, tileSize + 1, this);
        }
        
        // draw crates
        for (Position pos : state.state.getCrates()) {
            Position p = calcTilePos(pos);
            if (grid.isGoal(pos))
                g.drawImage(packer.getImage(ImagePacker.CRATE_ON_GOAL), p.getX(), p.getY(), tileSize + 1, tileSize + 1, this);
            else
                g.drawImage(packer.getImage(ImagePacker.CRATE), p.getX(), p.getY(), tileSize + 1, tileSize + 1, this);
        }
        
        // draw worker
        if (state.state.getWorker() != null) {
            Position p = calcTilePos(state.state.getWorker());
            g.drawImage(packer.getImage(ImagePacker.WORKER), p.getX(), p.getY(), tileSize + 1, tileSize + 1, this);
        }
    }

    
    /*
        MOUSE MOTION LISTENER METHODS
    */
    @Override
    public void mouseDragged(MouseEvent e) {
        // if ctrl is pressed move the grid arround
        if (e.isControlDown()) {  
            // set the previous mouse click location
            if (mouseX == 0 && mouseY == 0) {
                mouseX = e.getX();
                mouseY = e.getY();
            // then add the difference between the current and previous mouse click to the grid position
            } else {
                gridRect.x += e.getX() - mouseX;
                gridRect.y += e.getY() - mouseY;

                mouseX = e.getX();
                mouseY = e.getY();
            }
            
            // repaint the grid
            this.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    
    /*
        MOUSE LISTENER METHODS
    */
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseX = 0;
        mouseY = 0;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
