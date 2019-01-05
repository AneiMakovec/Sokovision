/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.visualization;

import graphics.support.GridRectangle;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import problem.Node;
import solver.BreadthFirstSolver;

/**
 *
 * @author anei
 */
public class DisplayStateTreePanel extends JPanel implements MouseListener, MouseMotionListener {

    private GridRectangle gridRect;
    private BreadthFirstSolver solver;
    private Node treeNode;
    
    private final int NODE_SIZE = 5;
    
    private int mouseX;
    private int mouseY;
    
    
    public DisplayStateTreePanel(BreadthFirstSolver solver) {
        this.solver = solver;
        this.treeNode = solver.getTreeState();
        this.gridRect = new GridRectangle(0, 0, 0, 0);
        this.mouseX = 0;
        this.mouseY = 0;
        initListeners();
    }
    
    
    private void initListeners() {
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    
    public void resizeTree() {
        gridRect.x = 20;
        gridRect.y = 20;
    }
    
    
    
    
    private int drawTree(Graphics g, Node node, int count) {
        if (node.childs != null) {
            int halfChildNum = node.childs.size() / 2;
            
            if (halfChildNum == 0) {
                count = drawTree(g, node.childs.get(0), count);
            } else {
                for (int i = 0; i <= halfChildNum; i++) {
                    count = drawTree(g, node.childs.get(i), count);
                }
            }
        }

        switch (node.type) {
            case Node.UNSEEN:
                g.setColor(Color.black);
                g.fillOval(gridRect.x + count * 20, gridRect.y + node.cost * 20, NODE_SIZE, NODE_SIZE);
                break;
            case Node.SEEN:
                
                break;
            case Node.DEADLOCK:
                
                break;
            case Node.START:
                
                break;
            case Node.END:
                
                break;
            
            default:
                break;
        }
        
        count++;
        
        if (node.childs != null) {
            int halfChildNum = node.childs.size() / 2;
            
            for (int i = halfChildNum + 1; i < node.childs.size(); i++) {
                count = drawTree(g, node.childs.get(i), count);
            }
        }
        
        return count;
    }
    
    
    private int getTreeDepth(Node node, int depth) {
        if (node != null) {
            if (node.cost + 1 > depth)
                depth = node.cost + 1;
            
            if (node.childs != null) {
                for (Node child : node.childs) {
                    depth = getTreeDepth(child, depth);
                }
            }
        }
        
        return depth;
    }
    
    private int getWidestTreeRow(int treeDepth) {
        int rowWidths[] = new int[treeDepth];
        
        findWidthOfRows(treeNode, rowWidths);
        
        int widest = 0;
        for (int row : rowWidths) {
            if (row > widest)
                widest = row;
        }
        
        return widest;
    }
    
    private void findWidthOfRows(Node node, int[] rowWidths) {
        
    }
    
    
    
    
    @Override
    protected void paintComponent(Graphics g) {
        // call parent method
        super.paintComponent(g);
        
        // update tree grid
        gridRect.height = getTreeDepth(treeNode, 0);
        gridRect.width = getWidestTreeRow(gridRect.height);
        
        // draw tree
        drawTree(g, treeNode, 0);
    }
    
    
    
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
    
    
    
    
    
}
