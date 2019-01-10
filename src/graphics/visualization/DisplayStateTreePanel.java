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
import problem.Node;
import solver.BreadthFirstSolver;

/**
 *
 * @author anei
 */
public class DisplayStateTreePanel extends JPanel implements MouseListener, MouseMotionListener {

    private GridRectangle gridRect;
    private BreadthFirstSolver solver;
    private Node solverTreeNode;
    
    private DisplayTreeNode displayTreeNode;
    private StateTree tree;
    private FixedNodeExtentProvider<DisplayTreeNode> extentProvider;
    private DefaultConfiguration<DisplayTreeNode> configuration;
    
    private final int NODE_SIZE = 5;
    
    private int mouseX;
    private int mouseY;
    
    
    public DisplayStateTreePanel(BreadthFirstSolver solver) {
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
//        if (node.childs != null) {
//            int halfChildNum = node.childs.size() / 2;
//            
//            if (halfChildNum == 0) {
//                count = drawTree(g, node.childs.get(0), count);
//            } else {
//                for (int i = 0; i <= halfChildNum; i++) {
//                    count = drawTree(g, node.childs.get(i), count);
//                }
//            }
//        }
//
//        switch (node.type) {
//            case Node.UNSEEN:
//                g.setColor(Color.black);
//                g.fillOval(gridRect.x + count * 20, gridRect.y + node.cost * 20, NODE_SIZE, NODE_SIZE);
//                break;
//            case Node.SEEN:
//                
//                break;
//            case Node.DEADLOCK:
//                
//                break;
//            case Node.START:
//                
//                break;
//            case Node.END:
//                
//                break;
//            
//            default:
//                break;
//        }
//        
//        count++;
//        
//        if (node.childs != null) {
//            int halfChildNum = node.childs.size() / 2;
//            
//            for (int i = halfChildNum + 1; i < node.childs.size(); i++) {
//                count = drawTree(g, node.childs.get(i), count);
//            }
//        }
//        
//        return count;

        Rectangle2D bounds = map.get(node);
        
        if (bounds != null) {
            g.setColor(Color.black);
            Rectangle2D childBounds;
            for (DisplayTreeNode child : node.childs) {
                childBounds = map.get(child);
                if (childBounds != null) {
                    g.drawLine(gridRect.x + (int)bounds.getX() + 2, gridRect.y + (int)bounds.getY() + 2, gridRect.x + (int)childBounds.getX() + 2, gridRect.y + (int)childBounds.getY() + 2);
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
            
            g.fillOval(gridRect.x + (int)bounds.getX(), gridRect.y + (int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight());
            
            for (DisplayTreeNode child : node.childs) {
                drawTree(g, child, map);
            }
        }
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
        
//        System.out.println("Redrawing");
        resetTree(displayTreeNode, solverTreeNode, 1);
        tree = new StateTree(displayTreeNode);
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
