/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import graphics.editor.EditImagePanel;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.BorderFactory;
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
        fileStructPane = new FileStructurePanel(PROJECTS_DIR_PATH, packer, this);
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
            // TODO
        });
        fileMenu.add(newFileMenuItem);
        
        // new solver
        JMenuItem newSolverMenuItem = new JMenuItem("New solver...");
        newSolverMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        newSolverMenuItem.setToolTipText("Create new solver.");
        newSolverMenuItem.addActionListener((java.awt.event.ActionEvent evt) -> {
            // TODO
        });
        fileMenu.add(newSolverMenuItem);
        
        // new project
        JMenuItem newProjectMenuItem = new JMenuItem("New project");
        newProjectMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        newProjectMenuItem.setToolTipText("Create new project.");
        newProjectMenuItem.addActionListener((java.awt.event.ActionEvent evt) -> {
            // TODO
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
        toolBar.add(button);
        
        // new project button
        button = addButton(ImagePacker.NEW_PROJECT, NEW_PROJECT, "New project");
        toolBar.add(button);
        
        // new solver button
        button = addButton(ImagePacker.NEW_SOLVER, NEW_SOLVER, "New solver");
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
            EditImagePanel editPanel = new EditImagePanel(this, file, displayPane.getTabCount(), packer);
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
            if (comp instanceof EditImagePanel) {
                EditImagePanel editPanel = (EditImagePanel) comp;
                editPanel.save();
            }
        }
    }
    
    private void export() {
        Component comp = displayPane.getSelectedComponent();
        if (comp != null) {
            File selectedFile = selectFile(); 
            
            if (comp instanceof EditImagePanel) {
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
                    EditImagePanel editPanel = (EditImagePanel) comp;
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
    
    private void createNewProblemFile() {
        
    }
    
    private void createNewProject() {
        
    }
    
    private void createNewSolver() {
        
    }
    
    
    
    
    
    /*
        EDIT PANEL STATE CHANGED METHOD
    */
    public void stateChanged(int tabIndex) {
        EditImagePanel editPanel = (EditImagePanel) displayPane.getComponentAt(tabIndex);
        Tab tab = (Tab) displayPane.getTabComponentAt(tabIndex);
        Font font = tab.getTitle().getFont();
        
        if (editPanel.wasEdited()) {
            tab.getTitle().setFont(font.deriveFont(font.getStyle() | Font.BOLD));
        } else {
            tab.getTitle().setFont(font.deriveFont(font.getStyle() & ~Font.BOLD));
        }
    }
    
    
    
    /*
        ACTION EVENT LISTENER METHODS
    */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().contains("Tab")) {
            int tabIndex = Integer.parseInt(e.getActionCommand().replaceAll("Tab", ""));
            
            //int tabIndex = displayPane.indexOfTab(e.getActionCommand());
            if (tabIndex >= 0) {
                displayPane.removeTabAt(tabIndex);
                
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
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            DataFile file = (DataFile) node.getUserObject();
            
            if (file.getFileType() == DataFile.PROBLEM) {
                addEditImagePanelToDisplayPane(file.getDataFile());
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
