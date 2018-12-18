/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import grid.Grid;
import grid.Position;
import grid.Space;
import game.State;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import support.KeyboardManager;

class Rectangle {
    public int x;
    public int y;
    public int width;
    public int height;
    
    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}

/**
 *
 * @author anei
 */
public class EditImagePanel extends JPanel implements MouseListener, MouseMotionListener {
    
    // rectangle in which the grid is displayed
    private final Rectangle gridRectangle;
    
    // the width and height of the grid in tiles
    private int gridWidth;
    private int gridHeight;
    
    // zoom of the grid
    private int zoom = 10;
    
    // default tileSize of a grid tile
    private final int DEFAULT_TILE_SIZE = 5;
    
    // class to store grid tile values and goal locations
    Grid grid;
    
    // class to store worker and crate positions
    State state;
    
    // pointer to image repository
    ImagePacker packer;
    
    // type of current tile
    private int currentSpace;
    
    // mouse coordinates during dragging
    private int mouseX, mouseY;
    
    public EditImagePanel(int width, int height, ImagePacker packer) {
        this.gridRectangle = new Rectangle(0, 0, 0, 0);
        this.grid = new Grid();
        this.state = new State();
        this.mouseX = 0;
        this.mouseY = 0;
        this.packer = packer;
        this.currentSpace = -1;
        initListeners();
        setParameters(width, height);
    }
    
    
    
    /*
        Private methods
    */    
    private void initListeners() {
        // mouse listener
        this.addMouseListener(this);
        
        // mouse motion listener
        this.addMouseMotionListener(this);
        
        // keyboard listener
        //KeyboardManager.init();
    }
    
    
    
    /*
        Public methods
    */
    public void resize(int zoomValue) {
        zoom = zoomValue;
        
        gridRectangle.width = DEFAULT_TILE_SIZE * gridWidth * zoom;
        gridRectangle.height = DEFAULT_TILE_SIZE * gridHeight * zoom;
        
        this.repaint();
    }
    
    public void resizeGrid(int width, int height) {
        this.gridWidth = width;
        this.gridHeight = height;
        
        gridRectangle.width = DEFAULT_TILE_SIZE * gridWidth * zoom;
        gridRectangle.height = DEFAULT_TILE_SIZE * gridHeight * zoom;
        
        this.repaint();
    }
    
    public void setCurrentSpace(int id) {
        this.currentSpace = id;
    }
    
    public Grid getGrid() {
        return grid;
    }
    
    public State getGameState() {
        return state;
    }
    
    public int getGridWidth() {
        return this.gridWidth;
    }
    
    public int getGridHeight() {
        return this.gridHeight;
    }
    
    public boolean isCrateGoalNumOk() {
        if (grid.getGoals().isEmpty() || state.getCrates().isEmpty())
            return false;
        
        return grid.getGoals().size() == state.getCrates().size();
    }
    
    public boolean isWorkerPresent() {
        return state.getWorker() != null;
    }
    
    public boolean isWallConnected() {
        // check upper wall
        if (!checkWallConnectionUp())
            return false;
        
        // check right wall
        if (!checkWallConnectionRight())
            return false;
        
        // check down wall
        if (!checkWallConnectionDown())
            return false;
        
        // check left wall
        return checkWallConnectionLeft();
    }
    
    public void optimizeGrid() {
        Position pos;
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                pos = new Position(x, y);
                
                if (grid.existsWall(pos.getUp()) && grid.existsWall(pos.getRight()) && grid.existsWall(pos.getDown()) && grid.existsWall(pos.getLeft())) {
                    grid.removeSpace(pos);
                }
            }
        }
        
        this.repaint();
    }
    

    
    
    
    
    /**
     * Paints the grid with all the tiles.
     * @param g graphics component which is used to paint
     */
    @Override
    protected void paintComponent(Graphics g) {
        // super call
        super.paintComponent(g);
        
        int tileSize = DEFAULT_TILE_SIZE * zoom;
        
        // draw vertical lines
        for (int i = 0; i <= gridWidth; i++) {
            g.drawLine(gridRectangle.x + i * tileSize, gridRectangle.y, gridRectangle.x + i * tileSize, gridRectangle.y + gridRectangle.height);
        }
        
        // draw horizontal lines
        for (int i = 0; i <= gridHeight; i++) {
            g.drawLine(gridRectangle.x, gridRectangle.y + i * tileSize, gridRectangle.x + gridRectangle.width, gridRectangle.y + i * tileSize);
        }
        
        // draw walls and free spaces
        for (Position pos : grid.getPositions()) {
            Space s = grid.getSpace(pos);
            Position p = calcTilePos(pos);
            
            if (s.type == Space.WALL || s.type == Space.FREE) {
                g.drawImage(packer.getImage(s.type), p.getX(), p.getY(), tileSize + 1, tileSize + 1, this);
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
        for (Position pos : state.getCrates()) {
            Position p = calcTilePos(pos);
            if (grid.isGoal(pos))
                g.drawImage(packer.getImage(ImagePacker.CRATE_ON_GOAL), p.getX(), p.getY(), tileSize + 1, tileSize + 1, this);
            else
                g.drawImage(packer.getImage(ImagePacker.CRATE), p.getX(), p.getY(), tileSize + 1, tileSize + 1, this);
        }
        
        // draw worker
        if (state.getWorker() != null) {
            Position p = calcTilePos(state.getWorker());
            g.drawImage(packer.getImage(ImagePacker.WORKER), p.getX(), p.getY(), tileSize + 1, tileSize + 1, this);
        }
    }
    
    
    
    
    /**
     * Calculates the drawing position of a tile.
     * @param pos grid position of chosen tile
     * @return drawing position of chosen tile
     */
    private Position calcTilePos(Position pos) {
        int tileSize = DEFAULT_TILE_SIZE * zoom;
        
        int tileX = gridRectangle.x + (pos.getX() * tileSize);
        int tileY = gridRectangle.y + (pos.getY() * tileSize);
        
        return new Position(tileX, tileY);
    }
    
    /**
     * Calculates the grid position of the tile which was clicked on.
     * @param x x coordinate of the mouse click
     * @param y y coordinate of the mouse click
     * @return grid position of clicked tile
     */
    private Position calcMouseToTile(int x, int y) {
        if (x <= gridRectangle.x || y <= gridRectangle.y)
            return null;
        
        if (x >= gridRectangle.x + gridRectangle.width || y >= gridRectangle.y + gridRectangle.height)
            return null;
        
        int tileSize = DEFAULT_TILE_SIZE * zoom;
        
        // x
        int tileX = x - gridRectangle.x;
        tileX = tileX / tileSize;
        
        // y
        int tileY = y - gridRectangle.y;
        tileY = tileY / tileSize;
        
        return new Position(tileX, tileY);
    }
    
    /**
     * Removes the goal, crate and worker tile type from the grid if present on given position.
     * @param pos position to check
     */
    private void checkForTilesToRemove(Position pos) {
        if (grid.isGoal(pos)) {
            grid.removeGoal(pos);
        }
                    
        if (state.hasCrate(pos)) {
            state.removeCrate(pos);
        }
                    
        if (state.isWorker(pos)) {
            state.removeWorker();
        }
    }
    
    /**
     * Adds the selected tile type to the grid to be displayed.
     * @param e mouse event used to draw
     */
    private void draw(MouseEvent e) {
        // pressed mouse button
        int button = e.getButton();

        // calculate which grid tile the mouse has clicked
        Position tilePos = calcMouseToTile(e.getX(), e.getY());
        
        // if mouse has clicked a grid tile
        if (tilePos != null) {
            // add the selected tile type to grid
            if (button == MouseEvent.BUTTON1) {
                // WALL TILE
                if (currentSpace == ImagePacker.WALL) {
                    // add tile to grid
                    grid.setSpace(tilePos, new Space(Space.WALL));
                    
                    // check if any other tile is on this position and remove it from the grid
                    checkForTilesToRemove(tilePos);
                // FREE TILE
                } else if (currentSpace == ImagePacker.FREE) {
                    // add tile to grid
                    grid.setSpace(tilePos, new Space(Space.FREE));
                            
                    // check if any other tile is on this position and remove it from the grid
                    checkForTilesToRemove(tilePos);
                } else {
                    // all the other tile types need to check if there is a wall on the clicked position
                    if (!grid.isWall(tilePos)) {
                        // GOAL TILE
                        if (currentSpace == ImagePacker.GOAL) {
                            // add tile to grid
                            grid.addGoal(tilePos);
                            
                            // also add an empty space under
                            grid.setSpace(tilePos, new Space(Space.FREE));
                        // WORKER TILE
                        } else if (currentSpace == ImagePacker.WORKER) {
                            // check if clicked tile has no crate
                            if (!state.hasCrate(tilePos)) {
                                // add tile to grid
                                state.setWorker(tilePos);
                                
                                // also add an empty space under
                                grid.setSpace(tilePos, new Space(Space.FREE));
                            }
                        // CRATE TILE
                        } else if (currentSpace == ImagePacker.CRATE) {
                            // check if tile has no worker
                            if (!state.isWorker(tilePos)) {
                                // add tile to grid
                                state.addCrate(tilePos);
                                
                                // also add an empty space under
                                grid.setSpace(tilePos, new Space(Space.FREE));
                            }
                        }
                    }
                }
            // or remove the clicked tile from the grid
            } else if (button == MouseEvent.BUTTON3) {
                if (state.hasCrate(tilePos)) {
                    state.removeCrate(tilePos);
                } else if (state.isWorker(tilePos)) {
                    state.removeWorker();
                } else if (grid.isGoal(tilePos)) {
                    grid.removeGoal(tilePos);
                } else {
                    grid.removeSpace(tilePos);
                }
            }
            
            // repaint the images
            this.repaint();
        }
    }
    
    /**
     * Sets the grid width and height and calculates the initial position and drawing tileSize of the grid.
     * @param width grid width in tiles
     * @param height grid height in tiles
     */
    private void setParameters(int width, int height) {
        // first we set the grid width and height
        this.gridWidth = width;
        this.gridHeight = height;
        
        // then we calculate the grid rectangle tileSize and position
        gridRectangle.x = (int) (this.getWidth() * 0.2);
        gridRectangle.y = (int) (this.getHeight() * 0.2);
        gridRectangle.width = DEFAULT_TILE_SIZE * gridWidth * zoom;
        gridRectangle.height = DEFAULT_TILE_SIZE * gridHeight * zoom;
    }
    
    
    private boolean checkWallConnectionUp() {
        Position pos;
        int y = 0;
        for (int x = 0; x < gridWidth; x++) {
            pos = new Position(x, y);
            
            while (y < gridHeight && grid.isVoid(pos)) {
                y++;
                pos.setY(y);
            }
            
            if (y == gridHeight) {
                y = 0;
                continue;
            }
                
            if (!grid.isWall(pos))
                return false;
            
            y = 0;
        }
        
        return true;
    }
    
    private boolean checkWallConnectionRight() {
        Position pos;
        int x = gridWidth - 1;
        for (int y = 0; y < gridHeight; y++) {
            pos = new Position(x, y);
            
            while (x >= 0 && grid.isVoid(pos)) {
                x--;
                pos.setX(x);
            }
            
            if (x < 0) {
                x = gridWidth - 1;
                continue;
            }
            
            if (!grid.isWall(pos))
                return false;
            
            x = gridWidth - 1;
        }
        
        return true;
    }
    
    private boolean checkWallConnectionDown() {
        Position pos;
        int y = gridHeight - 1;
        for (int x = 0; x < gridWidth; x++) {
            pos = new Position(x, y);
            
            while (y >= 0 && grid.isVoid(pos)) {
                y--;
                pos.setY(y);
            }
            
            if (y < 0) {
                y = gridHeight - 1;
                continue;
            }
                
            if (!grid.isWall(pos))
                return false;
            
            y = gridHeight - 1;
        }
        
        return true;
    }
    
    private boolean checkWallConnectionLeft() {
        Position pos;
        int x = 0;
        for (int y = 0; y < gridHeight; y++) {
            pos = new Position(x, y);
            
            while (x < gridWidth && grid.isVoid(pos)) {
                x++;
                pos.setX(x);
            }
            
            if (x == gridWidth) {
                x = 0;
                continue;
            }
            
            if (!grid.isWall(pos))
                return false;
            
            x = 0;
        }
        
        return true;
    }
    
    
    /*
        MouseListener implemented methods
    */
    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        // draw on the grid
        draw(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseX = 0;
        mouseY = 0;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}
    
    
    
    
    /*
        MouseMotionListener implemented methods
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
                gridRectangle.x += e.getX() - mouseX;
                gridRectangle.y += e.getY() - mouseY;

                mouseX = e.getX();
                mouseY = e.getY();
            }
            
            // repaint the grid
            this.repaint();
        } else {
            // draw on the grid
            draw(e);
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {}
}
