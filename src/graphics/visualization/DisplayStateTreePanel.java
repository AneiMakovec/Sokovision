/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.visualization;

import graphics.support.GridRectangle;
import graphics.visualization.tree.DisplayTreeNode;
import graphics.visualization.tree.StateTree;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import javax.swing.JPanel;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.FixedNodeExtentProvider;
import plugin.Solver;
import problem.Node;

/**
 *
 * @author anei
 */
public class DisplayStateTreePanel extends JPanel implements MouseListener, MouseMotionListener {

    private GridRectangle gridRect;
    private Solver solver;
    private Node solverTreeNode;
    
    private DisplayTreeNode displayTreeNode;
    private StateTree tree;
    private FixedNodeExtentProvider<DisplayTreeNode> extentProvider;
    private DefaultConfiguration<DisplayTreeNode> configuration;
    
    private final int NODE_SIZE = 5;
    
    private int mouseX;
    private int mouseY;
    
    
    public DisplayStateTreePanel(Solver solver) {
        this.solver = solver;
        this.solverTreeNode = solver.getTreeState();
        this.displayTreeNode = new DisplayTreeNode(0, null, solverTreeNode.type);
        this.gridRect = new GridRectangle(0, 0, 0, 0);
        this.mouseX = 0;
        this.mouseY = 0;
        initTree();
        initListeners();
    }
    
    
    
    private void initTree() {
        tree = new StateTree(displayTreeNode);
        extentProvider = new FixedNodeExtentProvider<>(5, 5);
        configuration = new DefaultConfiguration<>(10, 10);
    }
    
    private void initListeners() {
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    
    public void resizeTree() {
        gridRect.x = 20;
        gridRect.y = 20;
    }
    
    
    
    
    private void drawTree(Graphics g, DisplayTreeNode node, Map<DisplayTreeNode, Rectangle2D> map) {
        Rectangle2D bounds = map.get(node);
        Rectangle2D rootBounds = map.get(displayTreeNode);
        
        if (bounds != null) {
            int x = gridRect.x + ((int)bounds.getX() - (int)rootBounds.getX());
            int y = gridRect.y + ((int)bounds.getY() - (int)rootBounds.getY());
            
            g.setColor(Color.black);
            
            Rectangle2D childBounds;
            int childX;
            int childY;
            int offset = 2;
            for (DisplayTreeNode child : node.childs) {
                childBounds = map.get(child);
                if (childBounds != null) {
                    childX = gridRect.x + ((int)childBounds.getX() - (int)rootBounds.getX());
                    childY = gridRect.y + ((int)childBounds.getY() - (int)rootBounds.getY());
                    g.drawLine(x + offset, y + offset, childX + offset, childY + offset);
                }
            }
            
            switch (node.type) {
                case DisplayTreeNode.CURRENT:
                    g.setColor(Color.blue);
                    break;
                case DisplayTreeNode.SEEN:
                    g.setColor(Color.yellow);
                    break;
                case DisplayTreeNode.DEADLOCK:
                    g.setColor(Color.red);
                    break;
                case DisplayTreeNode.VISITED:
                    g.setColor(Color.green);
                    break;
                default:
                    break;
            }
            
            g.fillOval(x, y, (int)bounds.getWidth(), (int)bounds.getHeight());
            
            for (DisplayTreeNode child : node.childs) {
                drawTree(g, child, map);
            }
        }
    }
    
    
    private int resetTree(DisplayTreeNode displayNode, Node solverNode, int id) {
        if (solverNode.childs != null) {
            displayNode.reset();
            for (Node child : solverNode.childs) {
                displayNode.addChild(new DisplayTreeNode(id, displayNode, child.type));
                id++;
            }
            
            for (int i = 0; i < solverNode.childs.size(); i++) {
                id = resetTree(displayNode.childs.get(i), solverNode.childs.get(i), id);
            }
        }
        
        return id;
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        // call parent method
        super.paintComponent(g);
        
        solverTreeNode = solver.getTreeState();
        resetTree(displayTreeNode, solverTreeNode, 1);
        treeLayout = new TreeLayout(tree, extentProvider, configuration);
        
        Map<DisplayTreeNode, Rectangle2D> bounds = treeLayout.getNodeBounds();
        
        drawTree(g, displayTreeNode, bounds);
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
    
    
    
    // Variable declaration
    private TreeLayout treeLayout;
    // End of variable declaration
}
