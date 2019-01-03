/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.ui;

import graphics.support.ImagePacker;
import graphics.ui.support.DataFile;
import graphics.ui.support.FileTreeCellRenderer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

/**
 *
 * @author anei
 */
public class FileStructurePanel extends JPanel {
    
    public static final String TREE_NODE_SELECTED = "tree_node_selected";
    public static final String TREE_NODE_DOUBLE_CLICKED = "tree_node_double_clicked";
    
    private final String dirPath;
    private final ImagePacker packer;
    
    /**
     * Creates new panel with tree directory structure.
     * @param dirPath Path of directory with projects.
     * @param packer Image buffer.
     * @param treeNodeListener Action listener to be added to tree nodes.
     */
    public FileStructurePanel(String dirPath, ImagePacker packer, MouseListener treeNodeListener) {
        this.dirPath = dirPath;
        this.packer = packer;
        initComponents(treeNodeListener);
    }
    
    
    /**
     * Initializes the components.
     */
    @SuppressWarnings("unchecked")
    private void initComponents(MouseListener listener) {
        setPreferredSize(new Dimension(300, 500));
        
        dirTree = new JTree();
        File rootFile = new File(dirPath);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootFile);
        DefaultTreeModel model = new DefaultTreeModel(root);
        
        dirTree.setModel(model);
        
        TreeCellRenderer renderer = new FileTreeCellRenderer();
        dirTree.setCellRenderer(renderer);
        dirTree.setRootVisible(false);
        dirTree.setShowsRootHandles(true);
        dirTree.addMouseListener(listener);
        
        JScrollPane scrollPane = new JScrollPane(dirTree);
        scrollPane.setPreferredSize(getPreferredSize());
        add(scrollPane, BorderLayout.CENTER);
        
        updateDirTree();
    }
     
    private void updateDirTree() {
        DefaultTreeModel model = (DefaultTreeModel) dirTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.removeAllChildren();
        model.reload();
        
        // add projects
        File rootFile = (File) root.getUserObject();
        for (File project : rootFile.listFiles()) {
            if (project.isDirectory()) {
                DefaultMutableTreeNode child = new DefaultMutableTreeNode(new DataFile(project, DataFile.PROJECT, new ImageIcon(packer.getImage(ImagePacker.PROJECT_FILE))));
                model.insertNodeInto(child, root, root.getChildCount());
                
                addFileNodes(project, model, child);
            }
        }
        
        dirTree.expandPath(new TreePath(root));
    }
    
    private void addFileNodes(File rootFile, DefaultTreeModel model, DefaultMutableTreeNode root) {
        for (File file : rootFile.listFiles()) {
            DefaultMutableTreeNode child;
            
            if (file.isDirectory()) {
                child = new DefaultMutableTreeNode(new DataFile(file, DataFile.DIRECTORY, new ImageIcon(packer.getImage(ImagePacker.NEW_PROJECT))));
                model.insertNodeInto(child, root, root.getChildCount());
                addFileNodes(file, model, child);
            } else {
                if (file.getName().endsWith(".txt")) {
                    child = new DefaultMutableTreeNode(new DataFile(file, DataFile.PROBLEM, new ImageIcon(packer.getImage(ImagePacker.PROBLEM_FILE))));
                } else if (file.getName().endsWith(".slvr")) {
                    child = new DefaultMutableTreeNode(new DataFile(file, DataFile.SOLVER, new ImageIcon(packer.getImage(ImagePacker.SOLVER_FILE))));
                } else if (file.getName().endsWith(".stat")) {
                    child = new DefaultMutableTreeNode(new DataFile(file, DataFile.STAT, new ImageIcon(packer.getImage(ImagePacker.STAT_FILE))));
                } else if (file.getName().endsWith(".csv")) {
                    child = new DefaultMutableTreeNode(new DataFile(file, DataFile.CSV, new ImageIcon(packer.getImage(ImagePacker.CSV_FILE))));
                } else {
                    child = new DefaultMutableTreeNode(new DataFile(file, DataFile.UNKNOWN, new ImageIcon(packer.getImage(ImagePacker.UNKNOWN_FILE))));;
                }
                
                
                
                model.insertNodeInto(child, root, root.getChildCount());
            }
        }
    }
    
    public JTree getDirTree() {
        return dirTree;
    }
    
    
    // Variable declaration
    private JTree dirTree;
    // End of variable declaration
}
