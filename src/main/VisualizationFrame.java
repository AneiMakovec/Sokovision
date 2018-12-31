/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import graphics.visualization.VisualizationPane;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author anei
 */
public class VisualizationFrame extends javax.swing.JFrame {
    
    public static final int SOLVER_BFS = 0;
    public static final int SOLVER_DFS = 1;
    public static final int SOLVER_A = 2;
    public static final int SOLVER_IDA = 3;
    
    private File problemFile;
    
    public VisualizationFrame(File file) {
        this.problemFile = file;
        initComponents();
    }
    
    
    @SuppressWarnings("unchecked")
    private void initComponents() {
        pane = new javax.swing.JDesktopPane();
        visualPanes = new ArrayList<>();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        separator1 = new javax.swing.JPopupMenu.Separator();
        addSolverSubMenu = new javax.swing.JMenu("Add solver");
        addBFSSolver = new javax.swing.JMenuItem();
        addDFSSolver = new javax.swing.JMenuItem();
        addAStarSolver = new javax.swing.JMenuItem();
        addIDAStarSolver = new javax.swing.JMenuItem();
        separator2 = new javax.swing.JPopupMenu.Separator();
        quitMenuItem = new javax.swing.JMenuItem();
        
        // frame setup
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sokoban problem visualizator: ~" + problemFile.getName());
        
        //createNewVisualPane();

        // add main pane
        pane.setPreferredSize(new java.awt.Dimension(1000, 600));
        getContentPane().add(pane, java.awt.BorderLayout.CENTER);
        
        // menu bar setup
        fileMenu.setText("File");
        
        openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openMenuItem.setText("Open");
        openMenuItem.setToolTipText("Load new problem from file.");
        openMenuItem.addActionListener((java.awt.event.ActionEvent evt) -> {
            // TODO
        });
        fileMenu.add(openMenuItem);
        
        fileMenu.add(separator1);
        
        
        // set up popup menu
        //addBFSSolver.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        addBFSSolver.setText("Breadth-first search");
        addBFSSolver.setToolTipText("Adds a breadth-first search solver.");
        addBFSSolver.addActionListener((java.awt.event.ActionEvent evt) -> {
            createNewVisualPane(SOLVER_BFS);
        });
        addSolverSubMenu.add(addBFSSolver);
        
        //addBFSSolver.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        addDFSSolver.setText("Depth-first search");
        addDFSSolver.setToolTipText("Adds a depth-first search solver.");
        addDFSSolver.addActionListener((java.awt.event.ActionEvent evt) -> {
            createNewVisualPane(SOLVER_DFS);
        });
        addSolverSubMenu.add(addDFSSolver);
        
        //addBFSSolver.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        addAStarSolver.setText("A* search");
        addAStarSolver.setToolTipText("Adds an A* search solver.");
        addAStarSolver.addActionListener((java.awt.event.ActionEvent evt) -> {
            createNewVisualPane(SOLVER_A);
        });
        addSolverSubMenu.add(addAStarSolver);
        
        //addBFSSolver.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        addIDAStarSolver.setText("Iterative deepening A* search");
        addIDAStarSolver.setToolTipText("Adds an iterative deepening A* search solver.");
        addIDAStarSolver.addActionListener((java.awt.event.ActionEvent evt) -> {
            createNewVisualPane(SOLVER_IDA);
        });
        addSolverSubMenu.add(addIDAStarSolver);
        
        // add popup menu
        fileMenu.add(addSolverSubMenu);
        
        fileMenu.add(separator2);
        
        quitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        quitMenuItem.setText("Quit");
        quitMenuItem.setToolTipText("Quit program execution.");
        quitMenuItem.addActionListener((java.awt.event.ActionEvent evt) -> {
            System.exit(0);
        });
        fileMenu.add(quitMenuItem);
        
        menuBar.add(fileMenu);
        
        setJMenuBar(menuBar);

        pack();
    }
    
    
    private void createNewVisualPane(int solverId) {
        VisualizationPane visualPane = new VisualizationPane(solverId, problemFile);
        visualPanes.add(visualPane);
        
        Random rnd = new Random();
        visualPane.setLocation(rnd.nextInt(this.getPreferredSize().width - visualPane.getPreferredSize().width), rnd.nextInt(this.getPreferredSize().height - visualPane.getPreferredSize().height));
        visualPane.setVisible(true);
        
        pane.add(visualPane);
    }
    
    // Variable declaration
    private javax.swing.JDesktopPane pane;
    private ArrayList<VisualizationPane> visualPanes;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JPopupMenu.Separator separator1;
    private javax.swing.JMenu addSolverSubMenu;
    private javax.swing.JMenuItem addBFSSolver;
    private javax.swing.JMenuItem addDFSSolver;
    private javax.swing.JMenuItem addAStarSolver;
    private javax.swing.JMenuItem addIDAStarSolver;
    private javax.swing.JPopupMenu.Separator separator2;
    private javax.swing.JMenuItem quitMenuItem;
    // End variable declaration
}
