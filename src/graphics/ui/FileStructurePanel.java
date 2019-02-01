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
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import main.MainFrame;

/**
 *
 * @author anei
 */
public class FileStructurePanel extends JPanel implements MouseListener {
    
    public static final String TREE_NODE_SELECTED = "tree_node_selected";
    public static final String TREE_NODE_DOUBLE_CLICKED = "tree_node_double_clicked";
    
    private final String dirPath;
    private final ImagePacker packer;
    private final MainFrame parent;
    
    private TreePath clickedPath;
    
    /**
     * Creates new panel with tree directory structure.
     * @param parent Parent component.
     * @param dirPath Path of directory with projects.
     * @param packer Image buffer.
     */
    public FileStructurePanel(MainFrame parent, String dirPath, ImagePacker packer) {
        this.dirPath = dirPath;
        this.packer = packer;
        this.parent = parent;
        this.clickedPath = null;
        initComponents((MouseListener) this.parent);
    }
    
    
    /**
     * Initializes the components.
     */
    @SuppressWarnings("unchecked")
    private void initComponents(MouseListener listener) {
        dirTree = new JTree();
        File rootFile = new File(dirPath);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new DataFile(rootFile, DataFile.DIRECTORY, null));
        DefaultTreeModel model = new DefaultTreeModel(root);
        
        dirTree.setModel(model);
        
        TreeCellRenderer renderer = new FileTreeCellRenderer();
        dirTree.setCellRenderer(renderer);
        dirTree.setRootVisible(false);
        dirTree.setShowsRootHandles(true);
        dirTree.addMouseListener(listener);
        
        updateDirTree();
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().add(dirTree);
        scrollPane.setPreferredSize(new Dimension(300, 500));
        add(scrollPane, BorderLayout.CENTER);
        
        addPopupMenu();
    }
     
    public void updateDirTree() {
        DefaultTreeModel model = (DefaultTreeModel) dirTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.removeAllChildren();
        model.reload();
        
        // add projects
        DataFile rootFile = (DataFile) root.getUserObject();
        for (File project : rootFile.getDataFile().listFiles()) {
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
                } else if (file.getName().endsWith(".sol")) {
                    child = new DefaultMutableTreeNode(new DataFile(file, DataFile.SOLUTION, new ImageIcon(packer.getImage(ImagePacker.STAT_FILE))));
                } else {
                    child = new DefaultMutableTreeNode(new DataFile(file, DataFile.UNKNOWN, new ImageIcon(packer.getImage(ImagePacker.UNKNOWN_FILE))));;
                }
                
                model.insertNodeInto(child, root, root.getChildCount());
            }
        }
    }
    
    private void addPopupMenu() {
        // normal files popup
        filePopupMenu = new JPopupMenu();
        
        Action action = new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.delete();
            }
        };
        filePopupMenu.add(action);     
        
        
        // project popup
        projectPopupMenu = new JPopupMenu();
        
        action = new AbstractAction("New problem file") {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.createNewProblemFile();
            }
        };
        projectPopupMenu.add(action);
        
        action = new AbstractAction("New solver") {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.createNewSolver();
            }
        };
        projectPopupMenu.add(action);
        
        projectPopupMenu.addSeparator();
        
        action = new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.delete();
            }
        };
        projectPopupMenu.add(action);
        
        
        // stats popup
        statsPopupMenu = new JPopupMenu();
        
        action = new AbstractAction("Export") {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.exportSingleFile();
            }
        };
        statsPopupMenu.add(action);
        
        statsPopupMenu.addSeparator();
        
        action = new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.delete();
            }
        };
        statsPopupMenu.add(action);
        
        
        dirTree.add(filePopupMenu);
        dirTree.add(projectPopupMenu);
        dirTree.add(statsPopupMenu);
        dirTree.addMouseListener(this);
    }
    
    public JTree getDirTree() {
        return dirTree;
    }
    
    public TreePath getClickedTreePath() {
        return clickedPath;
    }
    
    public DefaultMutableTreeNode getProjectTreeNodeOnPath(TreePath path) {
        DefaultMutableTreeNode node;
        DataFile data;
        for (Object objectInPath : path.getPath()) {
            if (objectInPath instanceof DefaultMutableTreeNode) {
                node = (DefaultMutableTreeNode) objectInPath;
                data = (DataFile) node.getUserObject();
                            
                if (data.getFileType() == DataFile.PROJECT) {
                    return node;
                }
            }
        }
        
        return null;
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
        if (e.getButton() == MouseEvent.BUTTON3) {
            int x = e.getX();
            int y = e.getY();
            TreePath path = dirTree.getPathForLocation(x, y);
            if (path != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                DataFile data = (DataFile) node.getUserObject();
                
                if (data.getFileType() == DataFile.PROJECT)
                    projectPopupMenu.show(dirTree, x, y);
                else if (data.getFileType() == DataFile.STAT)
                    statsPopupMenu.show(dirTree, x, y);
                else
                    filePopupMenu.show(dirTree, x, y);
                
                clickedPath = path;
            }
        } else if (e.getButton() == MouseEvent.BUTTON1) {
            int x = e.getX();
            int y = e.getY();
            TreePath path = dirTree.getPathForLocation(x, y);
            if (path != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                DataFile data = (DataFile) node.getUserObject();
                
                if (data.getFileType() == DataFile.PROJECT) {
                    clickedPath = path;
                } else {
                    node = getProjectTreeNodeOnPath(path);
                    clickedPath = new TreePath(node.getPath());
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
     
    
    
    // Variable declaration
    private JTree dirTree;
    private JPopupMenu filePopupMenu;
    private JPopupMenu projectPopupMenu;
    private JPopupMenu statsPopupMenu;
    // End of variable declaration
}
