/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import graphics.editor.EditProblemPanel;
import graphics.support.ImagePacker;
import graphics.ui.FileStructurePanel;
import graphics.ui.SolveSettingsPanel;
import graphics.ui.support.DataFile;
import graphics.ui.support.Tab;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;


/**
 *
 * @author anei
 */
public class MainFrame extends JFrame implements MouseListener, ActionListener {
    
    private final ImagePacker packer;
    
    static final private String NEW_FILE = "new_file";
    static final private String NEW_PROJECT = "new_project";
    static final private String NEW_SOLVER = "new_solver";
    static final private String NEXT_STATE = "next_state";
    static final private String PREV_STATE = "prev_state";
    static final private String START = "start";
    static final private String STOP = "stop";
    static final private String PAUSE = "pause";
    static final private String RESUME = "resume";
    
    
    private final String PROJECTS_DIR_PATH = System.getProperty("user.home") + File.separator + "SokovisionProjects";
    
    private final String PROBLEMS_SUBDIR_PATH = File.separator + "Problems";
    private final String SOLVERS_SUBDIR_PATH = File.separator + "Solvers";
    private final String STATS_SUBDIR_PATH = File.separator + "Statistics";

    /**
     * Creates new frame MainFrame
     */
    public MainFrame() {
        this.packer = new ImagePacker();
        initProjectsDir();
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
        menuBar = new JMenuBar();
        toolBar = new JToolBar("Command toolbar");
        displayPane = new JTabbedPane();
        fileStructPane = new FileStructurePanel(this, PROJECTS_DIR_PATH, packer);
        solveSettingsPane = new SolveSettingsPanel();
        selectFileWindow = new JFileChooser();

        
        // frame setup
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sokovision");
        setPreferredSize(new Dimension(1200, 800));
        
        
        // menu bar setup
        
        // file menu
        JMenu fileMenu = new JMenu("File");
        
        // new file
        JMenuItem newFileMenuItem = new JMenuItem("New problem file");
        newFileMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newFileMenuItem.setToolTipText("Create new problem file.");
        newFileMenuItem.addActionListener((java.awt.event.ActionEvent evt) -> {
            createNewProblemFile();
        });
        fileMenu.add(newFileMenuItem);
        
        // new solver
        JMenuItem newSolverMenuItem = new JMenuItem("New solver...");
        newSolverMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        newSolverMenuItem.setToolTipText("Create new solver.");
        newSolverMenuItem.addActionListener((java.awt.event.ActionEvent evt) -> {
            createNewSolver();
        });
        fileMenu.add(newSolverMenuItem);
        
        // new project
        JMenuItem newProjectMenuItem = new JMenuItem("New project");
        newProjectMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        newProjectMenuItem.setToolTipText("Create new project.");
        newProjectMenuItem.addActionListener((java.awt.event.ActionEvent evt) -> {
            createNewProject();
        });
        fileMenu.add(newProjectMenuItem);
        
        fileMenu.addSeparator();
        
        // save
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setToolTipText("Save changes.");
        saveMenuItem.addActionListener((java.awt.event.ActionEvent evt) -> {
            save();
        });
        fileMenu.add(saveMenuItem);
        
        fileMenu.addSeparator();
        
        // export
        JMenuItem exportMenuItem = new JMenuItem("Export...");
        exportMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        exportMenuItem.setToolTipText("Export data to file.");
        exportMenuItem.addActionListener((java.awt.event.ActionEvent evt) -> {
            export();
        });
        fileMenu.add(exportMenuItem);
        
        fileMenu.addSeparator();
        
        // quit
        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        quitMenuItem.setToolTipText("Quit the application.");
        quitMenuItem.addActionListener((java.awt.event.ActionEvent evt) -> {
            System.exit(0);
        });
        fileMenu.add(quitMenuItem);
        
        menuBar.add(fileMenu);
        
        setJMenuBar(menuBar);
        
        
        // toolbar setup
        
        // add buttons
        addButtonsToToolbar(toolBar);
        
        add(toolBar, BorderLayout.PAGE_START);
        
        
        // control pane setup
        JPanel controlPane = new JPanel();
        controlPane.setPreferredSize(new Dimension(300, 800));
        controlPane.setLayout(new BoxLayout(controlPane, BoxLayout.Y_AXIS));
        
        // tabs
        JTabbedPane projectTabs = new JTabbedPane();
        projectTabs.setBorder(BorderFactory.createEtchedBorder());
        
        // file structure
        projectTabs.add("Projects", fileStructPane);
        
        controlPane.add(projectTabs);
        
        // solver settings
        solveSettingsPane.setBorder(BorderFactory.createEtchedBorder());
        controlPane.add(solveSettingsPane);
        
        add(controlPane, BorderLayout.WEST);
        
        
        // display pane setup
        displayPane.setBorder(BorderFactory.createEtchedBorder());
        add(displayPane, BorderLayout.CENTER);
        
        
        pack();
    }// </editor-fold>   
    
    private void initProjectsDir() {
        Path path = Paths.get(PROJECTS_DIR_PATH);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            System.err.println("FATAL ERROR: could not create projects directory - access denied.");
            System.exit(1);
        }
    }

    private void addButtonsToToolbar(JToolBar toolbar) {
        JButton button;
        
        // new file button
        button = addButton(ImagePacker.NEW_FILE, NEW_FILE, "New file");
        button.addActionListener((java.awt.event.ActionEvent evt) -> {
            createNewProblemFile();
        });
        toolBar.add(button);
        
        // new project button
        button = addButton(ImagePacker.NEW_PROJECT, NEW_PROJECT, "New project");
        button.addActionListener((java.awt.event.ActionEvent evt) -> {
            createNewProject();
        });
        toolBar.add(button);
        
        // new solver button
        button = addButton(ImagePacker.NEW_SOLVER, NEW_SOLVER, "New solver");
        button.addActionListener((java.awt.event.ActionEvent evt) -> {
            createNewSolver();
        });
        toolBar.add(button);
        
        // start solving button
        button = addButton(ImagePacker.START, START, "Start solving");
        button.setEnabled(false);
        toolBar.add(button);
        
        // stop solving button
        button = addButton(ImagePacker.STOP, STOP, "Stop solving");
        button.setEnabled(false);
        toolBar.add(button);
        
        // pause solving button
        button = addButton(ImagePacker.PAUSE, PAUSE, "Pause solving");
        button.setEnabled(false);
        toolBar.add(button);
        
        // previous state button
        button = addButton(ImagePacker.PREV_STATE, PREV_STATE, "Previous state");
        button.setEnabled(false);
        toolBar.add(button);
        
        // next state button
        button = addButton(ImagePacker.NEXT_STATE, NEXT_STATE, "Next state");
        button.setEnabled(false);
        toolBar.add(button);
    }
    
    protected JButton addButton(int imageId, String action, String toolTipText) {
        JButton button = new JButton();
        button.setActionCommand(action);
        button.setToolTipText(toolTipText);
        button.addActionListener(this);
        button.setIcon(new ImageIcon(packer.getImage(imageId)));
        
        return button;
    }
    
    private void addEditImagePanelToDisplayPane(File file) {
        // check if a panel with the same name doesn't already exist
        if (displayPane.indexOfTab(file.getName()) == -1) {
            // add edit panel as tabbed pane
            EditProblemPanel editPanel = new EditProblemPanel(this, file, displayPane.getTabCount(), packer);
            displayPane.addTab(file.getName(), editPanel);

            // get index of edit panel's tab
            int index = displayPane.indexOfTab(file.getName());

            Tab tab = new Tab(file, index, this);
//            // create a JPanel that will replace the tab
//            JPanel tab = new JPanel(new GridBagLayout());
//            tab.setOpaque(false);
//            tab.setToolTipText(file.getAbsolutePath());
//            JLabel title = new JLabel(file.getName());
//            JButton closeButton = new JButton("X");
//            closeButton.setActionCommand("Tab" + index);
//
//            // adjust the position of tab components
//            GridBagConstraints gbc = new GridBagConstraints();
//            gbc.gridx = 0;
//            gbc.gridy = 0;
//            gbc.weightx = 1;
//
//            // add the tab title
//            tab.add(title, gbc);
//
//            gbc.gridx++;
//            gbc.weightx = 0;
//
//            // add the close button to tab
//            tab.add(closeButton, gbc);

            // set the tab
            displayPane.setTabComponentAt(index, tab);

//            closeButton.addActionListener(this);
        }
    }
    
    
    
    private void save() {
        Component comp = displayPane.getSelectedComponent();
        if (comp != null) {
            if (comp instanceof EditProblemPanel) {
                EditProblemPanel editPanel = (EditProblemPanel) comp;
                editPanel.save();
            }
        }
    }
    
    private void export() {
        Component comp = displayPane.getSelectedComponent();
        if (comp != null) {
            File selectedFile = selectFile(); 
            
            if (comp instanceof EditProblemPanel) {
                if (!selectedFile.getName().endsWith(".txt")) {
                    JOptionPane.showMessageDialog(this, "Invalid file name extension. File name should end in '.txt'.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                } else {
                    if (!selectedFile.exists()) {
                        try {
                            selectedFile.createNewFile();
                        } catch (IOException e) {
                            return;
                        }
                    }
                    EditProblemPanel editPanel = (EditProblemPanel) comp;
                    editPanel.export(selectedFile);
                }
            }
        }
    }
    
    private File selectFile() {
        int retVal = selectFileWindow.showOpenDialog(this);
        
        File sFile = null;
        
        if (retVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            sFile = selectFileWindow.getSelectedFile();
        }
        
        return sFile;
    }
    
    public void createNewProblemFile() {
        // init dialog component
        JTextField nameInput = new JTextField(30);
        JTextField xInput = new JTextField(5);
        JTextField yInput = new JTextField(5);
        JTextField projectName = new JTextField(30);
        
        JPanel inputPanel = new JPanel(new GridLayout(7, 2));
        inputPanel.setPreferredSize(new Dimension(300, 200));
        inputPanel.add(new JLabel("Name: "));
        inputPanel.add(nameInput);
        inputPanel.add(Box.createVerticalStrut(15));
        inputPanel.add(Box.createVerticalStrut(15));
        inputPanel.add(new JLabel("Width: "));
        inputPanel.add(xInput);
        inputPanel.add(Box.createVerticalStrut(15));
        inputPanel.add(Box.createVerticalStrut(15));
        inputPanel.add(new JLabel("Height: "));
        inputPanel.add(yInput);
        inputPanel.add(Box.createVerticalStrut(15));
        inputPanel.add(Box.createVerticalStrut(15));
        inputPanel.add(new JLabel("Project: "));
        inputPanel.add(projectName);
        
        // get last clicked tree path
        TreePath path = fileStructPane.getClickedTreePath();
        if (path != null) {
            // find the project dir in path
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            DataFile data = (DataFile) node.getUserObject();
            
            projectName.setText(data.toString());
            
            // ask for input
            int result = JOptionPane.showConfirmDialog(this, inputPanel, "New problem file", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                // retrieve input
                int width = Integer.parseInt(xInput.getText());
                int height = Integer.parseInt(yInput.getText());
                String name = nameInput.getText() + ".txt";

                // check if input parameters are acceptable
                if (width < 5 || height < 5) {
                    JOptionPane.showMessageDialog(this, "Problem must be at least 5 tiles wide and 5 tiles high.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                } else {
                    // build empty grid
                    StringBuilder sb = new StringBuilder();
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            sb.append("o");
                        }

                        if (y < height - 1)
                            sb.append("\n");
                    }

                    // construct path to new file
                    File newFile = new File(data.getDataFile().getAbsolutePath() + PROBLEMS_SUBDIR_PATH + File.separator + name);
                    
                    try {
                        // check if file with this name already exists
                        if (newFile.exists()) {
                            JOptionPane.showMessageDialog(this, "File " + name + " already exists.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                        } else {
                            // create new empty file
                            newFile.createNewFile();
                            
                            // write empty grid to file
                            BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
                            writer.write(sb.toString());
                            writer.flush();
                            writer.close();
                            
                            // update file structure
                            fileStructPane.updateDirTree();
                        }
                    } catch (IOException e) {
                        // if error while writing, delete file
                        newFile.delete();
                    }
                }
            }
        }
    }
    
    private void createNewProject() {
        String projectName = JOptionPane.showInputDialog(this, "Project name:");
        
        // create project directory
        File file = new File(PROJECTS_DIR_PATH + File.separator + projectName);
        file.mkdirs();
        
        // create problems sub-directory
        file = new File(PROJECTS_DIR_PATH + File.separator + projectName + PROBLEMS_SUBDIR_PATH);
        file.mkdirs();
        
        // create solvers sub-directory
        file = new File(PROJECTS_DIR_PATH + File.separator + projectName + SOLVERS_SUBDIR_PATH);
        file.mkdirs();
        
        // create stats sub-directory
        file = new File(PROJECTS_DIR_PATH + File.separator + projectName + STATS_SUBDIR_PATH);
        file.mkdirs();
        
        fileStructPane.updateDirTree();
    }
    
    public void createNewSolver() {
        
    }
    
    
    
    
    
    /*
        EDIT PANEL STATE CHANGED METHOD
    */
    public void stateChanged(int tabIndex) {
        EditProblemPanel editPanel = (EditProblemPanel) displayPane.getComponentAt(tabIndex);
        Tab tab = (Tab) displayPane.getTabComponentAt(tabIndex);
        Font font = tab.getTitle().getFont();
        
        if (editPanel.wasEdited()) {
            tab.getTitle().setFont(font.deriveFont(font.getStyle() | Font.BOLD));
        } else {
            tab.getTitle().setFont(font.deriveFont(font.getStyle() & ~Font.BOLD));
        }
    }
    
    
    /*
        FILE STRUCTURE PANEL DELETE METHOD
    */
    public void delete() {
        TreePath path = fileStructPane.getClickedTreePath();
        if (path != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            DataFile data = (DataFile) node.getUserObject();

            if (data.getFileType() == DataFile.DIRECTORY) {
                JOptionPane.showMessageDialog(this, "Directory " + data.toString() + " cannot be deleted.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            } else {
                int retVal = JOptionPane.showOptionDialog(this, "Do you want to delete " + data.toString() + "?", "Confirm delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);

                if (retVal == 0) {
                    // delete the file
                    if (data.getDataFile().isDirectory())
                        deleteRecursively(data.getDataFile());
                    else 
                        data.getDataFile().delete();

                    // update file tree
                    fileStructPane.updateDirTree();
                }
            }
        }
    }
    
    private void deleteRecursively(File file) {
        for (File child : file.listFiles()) {
            if (child == null) {
                System.out.println("Child is NULL.");
            }
            if (child.isDirectory()) {
                deleteRecursively(child);
            } else {
                child.delete();
            }
        }

        file.delete();
    }
    
    
    
    /*
        ACTION EVENT LISTENER METHODS
    */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().contains("Tab")) {
            int tabIndex = Integer.parseInt(e.getActionCommand().replaceAll("Tab", ""));
 
            if (tabIndex >= 0) {
                // check if closing edit problem panel and if yes, save file
                Component comp = displayPane.getComponentAt(tabIndex);
                if (comp instanceof EditProblemPanel) {
                    EditProblemPanel editPanel = (EditProblemPanel) comp;
                    editPanel.save();
                }
                
                // then remove the panel
                displayPane.removeTabAt(tabIndex);
                
                // also remove this action listener from source button
                JButton closeButton = (JButton) e.getSource();
                closeButton.removeActionListener(this);
            }
        }
    }
    
    
    /*
        MOUSE LISTENER METHODS
    */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
            JTree dirTree = fileStructPane.getDirTree();
            TreePath path = dirTree.getPathForLocation(e.getX(), e.getY());
            if (path != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                DataFile file = (DataFile) node.getUserObject();

                if (file.getFileType() == DataFile.PROBLEM) {
                    addEditImagePanelToDisplayPane(file.getDataFile());
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
                                                          

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            
            
            //javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            
            
            
            // set the tree icons
            
            
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private JTabbedPane displayPane;
    private FileStructurePanel fileStructPane;
    private SolveSettingsPanel solveSettingsPane;
    private JFileChooser selectFileWindow;
    // End of variables declaration                  
}
