/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import graphics.support.ImagePacker;
import graphics.ui.FileStructurePanel;
import graphics.ui.SolveSettingsPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;


/**
 *
 * @author anei
 */
public class MainFrame extends javax.swing.JFrame implements java.awt.event.ActionListener {
    
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

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        this.packer = new ImagePacker();
        initProjectsDir();
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
        menuBar = new javax.swing.JMenuBar();
        toolBar = new javax.swing.JToolBar("Command toolbar");
        displayPane = new javax.swing.JTabbedPane();
        displayedTabs = new ArrayList<>();
        fileStructPane = new FileStructurePanel();
        solveSettingsPane = new SolveSettingsPanel();

        
        // frame setup
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sokovision");
        setPreferredSize(new java.awt.Dimension(1200, 800));
        
        
        // menu bar setup
        
        // file menu
        javax.swing.JMenu fileMenu = new javax.swing.JMenu("File");
        
        // new file
        javax.swing.JMenuItem newFileMenuItem = new javax.swing.JMenuItem("New problem file");
        newFileMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newFileMenuItem.setToolTipText("Create new problem file.");
        newFileMenuItem.addActionListener((java.awt.event.ActionEvent evt) -> {
            // TODO
        });
        fileMenu.add(newFileMenuItem);
        
        // new solver
        javax.swing.JMenuItem newSolverMenuItem = new javax.swing.JMenuItem("New solver...");
        newSolverMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        newSolverMenuItem.setToolTipText("Create new solver.");
        newSolverMenuItem.addActionListener((java.awt.event.ActionEvent evt) -> {
            // TODO
        });
        fileMenu.add(newSolverMenuItem);
        
        // new project
        javax.swing.JMenuItem newProjectMenuItem = new javax.swing.JMenuItem("New project");
        newProjectMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        newProjectMenuItem.setToolTipText("Create new project.");
        newProjectMenuItem.addActionListener((java.awt.event.ActionEvent evt) -> {
            // TODO
        });
        fileMenu.add(newProjectMenuItem);
        
        fileMenu.addSeparator();
        
        // quit
        javax.swing.JMenuItem quitMenuItem = new javax.swing.JMenuItem("Quit");
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
        javax.swing.JPanel controlPane = new javax.swing.JPanel();
        controlPane.setPreferredSize(new java.awt.Dimension(300, 800));
        controlPane.setLayout(new javax.swing.BoxLayout(controlPane, BoxLayout.Y_AXIS));
        
        // tabs
        javax.swing.JTabbedPane projectTabs = new javax.swing.JTabbedPane();
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
        Path path = Paths.get(System.getProperty("user.home") + File.separator + "SokovisionProjects");
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            System.err.println("FATAL ERROR: could not create projects directory - access denied.");
            System.exit(1);
        }
    }

    private void addButtonsToToolbar(javax.swing.JToolBar toolbar) {
        javax.swing.JButton button;
        
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
    
    protected javax.swing.JButton addButton(int imageId, String action, String toolTipText) {
        javax.swing.JButton button = new javax.swing.JButton();
        button.setActionCommand(action);
        button.setToolTipText(toolTipText);
        button.addActionListener(this);
        button.setIcon(new javax.swing.ImageIcon(packer.getImage(imageId)));
        
        return button;
    }
    
    
    
    
    /*
        ACTION LISTENER METHODS
    */
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO
    }
                                                          

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
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JToolBar toolBar;
    private javax.swing.JTabbedPane displayPane;
    private ArrayList<javax.swing.JPanel> displayedTabs;
    //private javax.swing.JPanel controlPane;
    private FileStructurePanel fileStructPane;
    private SolveSettingsPanel solveSettingsPane;
    // End of variables declaration                  
}
