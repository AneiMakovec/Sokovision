/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import graphics.editor.EditProblemPanel;
import graphics.statistics.StatisticsVisualPanel;
import graphics.support.ImagePacker;
import graphics.ui.FileStructurePanel;
import graphics.ui.SolveSettingsPanel;
import graphics.ui.support.DataFile;
import graphics.ui.support.Tab;
import graphics.visualization.SolutionVisualizationPanel;
import graphics.visualization.VisualizationPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import support.reader.StatsReader;
import support.writer.SolverWriter;
import support.writer.StatsWriter;


/**
 *
 * @author anei
 */
public class MainFrame extends JFrame implements MouseListener, ActionListener, ContainerListener, ChangeListener {
    
    private final ImagePacker packer;
    
    static final private String NEW_FILE = "new_file";
    static final private String NEW_PROJECT = "new_project";
    static final private String NEW_SOLVER = "new_solver";
    static final private String NEXT_STATE = "next_solver_state";
    static final private String PREV_STATE = "prev_solver_state";
    static final private String START = "start";
    static final private String STOP = "stop";
    static final private String PAUSE = "pause";
    static final private String RESUME = "resume";
    static final private String RESET = "reset";
    
    
    private final String PROJECTS_DIR_PATH = System.getProperty("user.home") + File.separator + "SokovisionProjects";
    
    public static final String PROBLEMS_SUBDIR_PATH = File.separator + "Problems";
    public static final String SOLVERS_SUBDIR_PATH = File.separator + "Solvers";
    public static final String STATS_SUBDIR_PATH = File.separator + "Statistics";

    /**
     * Creates new frame MainFrame
     */
    public MainFrame() {
        this.packer = new ImagePacker();
        initProjectsDir();
        initComponents();
    }
    
    /**
     * Initializes frame components.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Init Components">                          
    private void initComponents() {
        menuBar = new JMenuBar();
        toolBar = new JToolBar("Command toolbar");
        displayPane = new JTabbedPane();
        fileStructPane = new FileStructurePanel(this, PROJECTS_DIR_PATH, packer);
        solveSettingsPane = new SolveSettingsPanel();
        selectFileWindow = new JFileChooser();
        solvingTimer = new Timer(800, this);

        
        // frame setup
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sokovision");
        setPreferredSize(new Dimension(1200, 800));
        
        
        // solving timer setup
        solvingTimer.setActionCommand(NEXT_STATE);
        
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
        
        displayPane.addContainerListener(this);
        
        pack();
    }// </editor-fold>   
    
    
    /**
     * Initializes projects directory on path "user.home".
     */
    // <editor-fold defaultstate="collapsed" desc="Init Projects Directory">
    private void initProjectsDir() {
        Path path = Paths.get(PROJECTS_DIR_PATH);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            System.err.println("FATAL ERROR: could not create projects directory - access denied.");
            System.exit(1);
        }
    }// </editor-fold>

    /**
     * Initializes and adds buttons to tool bar.
     * @param toolbar Tool bar to which the buttons are to be added.
     */
    // <editor-fold defaultstate="collapsed" desc="Add Buttons To Tool Bar">
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
        
        // solving control buttons
        toolBar.addSeparator();
        
        // start solving button
        toolBarStartButton = addButton(ImagePacker.START, START, "Start solving");
        toolBarStartButton.setEnabled(false);
        toolBar.add(toolBarStartButton);
        
        // stop solving button
        toolBarStopButton = addButton(ImagePacker.STOP, STOP, "Stop solving");
        toolBarStopButton.setEnabled(false);
        toolBar.add(toolBarStopButton);
        
        // pause solving button
        toolBarPauseResumeButton = addButton(ImagePacker.PAUSE, PAUSE, "Pause solving");
        toolBarPauseResumeButton.setEnabled(false);
        toolBar.add(toolBarPauseResumeButton);
        
        // reset button
        toolBarResetButton = addButton(ImagePacker.RESET, RESET, "Reset solver");
        toolBarResetButton.setEnabled(false);
        toolBar.add(toolBarResetButton);
        
        // previous state button
        toolBarPrevButton = addButton(ImagePacker.PREV_STATE, PREV_STATE, "Previous state");
        toolBarPrevButton.setEnabled(false);
        toolBar.add(toolBarPrevButton);
        
        // next state button
        toolBarNextButton = addButton(ImagePacker.NEXT_STATE, NEXT_STATE, "Next state");
        toolBarNextButton.setEnabled(false);
        toolBar.add(toolBarNextButton);
    }// </editor-fold>
    
    /**
     * Initializes a single tool bar button.
     * @param imageId Id of icon to be displayed on button.
     * @param action Action command of button action event.
     * @param toolTipText Text to be set in tool tip of the button.
     * @return Initialized button.
     */
    // <editor-fold defaultstate="collapsed" desc="Add Button">
    protected JButton addButton(int imageId, String action, String toolTipText) {
        JButton button = new JButton();
        button.setActionCommand(action);
        button.setToolTipText(toolTipText);
        button.addActionListener(this);
        button.setIcon(new ImageIcon(packer.getImage(imageId)));
        
        return button;
    }// </editor-fold>
    
    /**
     * Initializes and adds a new panel for editing problems to the display pane.
     * @param file Problem file to be edited.
     */
    // <editor-fold defaultstate="collapsed" desc="Add Panel To Display">
    private void addPanelToDisplay(JPanel panel, File file) {
        // check if a panel with the same name doesn't already exist
        if (displayPane.indexOfTab(file.getName()) == -1) {
            // add panel
            displayPane.addTab(file.getName(), panel);

            // get index of edit panel's tab
            int index = displayPane.indexOfTab(file.getName());

            Tab tab = new Tab(file, index, this);

            // set the tab
            displayPane.setTabComponentAt(index, tab);
            
            // if the panel added is a visualization panel also enable the tool bar buttons
            if (panel instanceof VisualizationPanel || panel instanceof SolutionVisualizationPanel) {
                toolBarStartButton.setEnabled(true);
                toolBarResetButton.setEnabled(true);
                toolBarNextButton.setEnabled(true);
                toolBarPrevButton.setEnabled(true);
            }
        }
    }// </editor-fold>
    
    /**
     * Loads a visualization panel from a solver file.
     * @param path Tree path on which the selected solver file is located.
     * @param solverData The data file of chosen solver.
     */
    //<editor-fold defaultstate="collapsed" desc="Load Solver Visualizator">
    private void loadSolverVisualizator(TreePath path, DataFile solverData) {
        // ask for input
        String problemName = JOptionPane.showInputDialog(this, "Name of problem file to solve:");
        
        if (problemName != null) {
            // check if input value is acceptable
            if (problemName.length() == 0) {
                JOptionPane.showMessageDialog(this, "No problem file specified.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            } else {
                if (!problemName.endsWith(".txt"))
                    problemName = problemName.concat(".txt");
            }
            
            // find the project in which the solver is located
            DefaultMutableTreeNode projectNode = fileStructPane.getProjectTreeNodeOnPath(path);
            if (projectNode != null) {
                DataFile projectData = (DataFile) projectNode.getUserObject();
                
                // find the problem file specified by the user and check if it exists
                File problemFile = new File(projectData.getDataFile().getAbsolutePath() + PROBLEMS_SUBDIR_PATH + File.separator + problemName);
                if (!problemFile.exists()) {
                    JOptionPane.showMessageDialog(this, "There is no problem named " + problemName + " in current project " + projectData.toString() + ".", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                } else {
                    // find or create a new stat file for this solver
                    String statsFileName = solverData.toString();
                    statsFileName = statsFileName.replace(".slvr", ".stat");
                    
                    File statsFile = new File(projectData.getDataFile().getAbsolutePath() + STATS_SUBDIR_PATH + File.separator + statsFileName);
                    if (!statsFile.exists()) {
                        try {
                            statsFile.createNewFile();
                            fileStructPane.updateDirTree();
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(this, "Could not create statistics file for solver" + solverData.toString() + ".", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    
                    // find or create a new solution file for this solver
                    String solutionFileName = solverData.toString();
                    solutionFileName = solutionFileName.replace(".slvr", ".sol");
                    
                    File solutionFile = new File(projectData.getDataFile().getAbsolutePath() + STATS_SUBDIR_PATH + File.separator + solutionFileName);
                    if (!solutionFile.exists()) {
                        try {
                            solutionFile.createNewFile();
                            fileStructPane.updateDirTree();
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(this, "Could not create solution file for solver" + solverData.toString() + ".", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    
                    addPanelToDisplay(new VisualizationPanel(solverData.getDataFile(), problemFile, statsFile, solutionFile, packer, solveSettingsPane, this), solverData.getDataFile());
                }
            }
        }
    }
//</editor-fold>
    
    
    
    /*
        MENU METHODS
    */
    
    /**
     * Saves currently open panel's data.
     */
    //<editor-fold defaultstate="collapsed" desc="Save">
    private void save() {
        Component comp = displayPane.getSelectedComponent();
        if (comp != null) {
            if (comp instanceof EditProblemPanel) {
                EditProblemPanel editPanel = (EditProblemPanel) comp;
                editPanel.save();
            }
        }
    }
//</editor-fold>
    
    /**
     * Exports currently open panel's data.
     */
    //<editor-fold defaultstate="collapsed" desc="Export">
    private void export() {
        Component comp = displayPane.getSelectedComponent();
        if (comp != null) {
            File selectedFile = selectFile();
            
            if (selectedFile != null) {
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
                        if (editPanel.export(selectedFile)) {
                            JOptionPane.showMessageDialog(this, "Problem has been exported.", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Could not export problem.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                        }
                    }
                } else if (comp instanceof VisualizationPanel) {
                    if (!selectedFile.getName().endsWith(".csv")) {
                        JOptionPane.showMessageDialog(this, "Invalid file name extension. File name should end in '.csv'.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                    } else {
                        if (!selectedFile.exists()) {
                            try {
                                selectedFile.createNewFile();
                            } catch (IOException e) {
                                JOptionPane.showMessageDialog(this, "Could not export statistic data. Error while creating file.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        }
                        
                        VisualizationPanel visualPanel = (VisualizationPanel) comp;
                        if (visualPanel.exportStats(selectedFile)) {
                            JOptionPane.showMessageDialog(this, "Statistic data has been exported.", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Could not export statistic data.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
        }
    }
//</editor-fold>
    
    /**
     * Displays a file choosing dialog and asks for user input.
     * @return file chosen by user
     */
    //<editor-fold defaultstate="collapsed" desc="Select File">
    private File selectFile() {
        int retVal = selectFileWindow.showOpenDialog(this);
        
        File sFile = null;
        
        if (retVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            sFile = selectFileWindow.getSelectedFile();
        }
        
        return sFile;
    }
//</editor-fold>
    
    /**
     * Creates a new problem file.
     */
    //<editor-fold defaultstate="collapsed" desc="Create New Problem File">
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
            DefaultMutableTreeNode node = fileStructPane.getProjectTreeNodeOnPath(path);
            DataFile data = (DataFile) node.getUserObject();
            
            projectName.setText(data.toString());
            
            // ask for input
            int result = JOptionPane.showConfirmDialog(this, inputPanel, "New problem file", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                // retrieve input
                
                // width and height of grid
                int width = Integer.parseInt(xInput.getText());
                int height = Integer.parseInt(yInput.getText());
                
                // name of file
                String name = nameInput.getText();
                if (name.length() > 0 && !name.endsWith(".txt"))
                    name = name.concat(".txt");
                
                File projectDir;
                
                // name of project to contain file
                String project = projectName.getText();
                if (!project.equals(data.toString())) {
                    projectDir = new File(PROJECTS_DIR_PATH + File.separator + project);
                    
                    if (!projectDir.exists()) {
                        JOptionPane.showMessageDialog(this, "No existing project named " + project + ".", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } else {
                    projectDir = data.getDataFile();
                }
                
                // check if input parameters are acceptable
                if (width < 5 || height < 5) {
                    JOptionPane.showMessageDialog(this, "Problem must be at least 5 tiles wide and 5 tiles high.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                } else if (name.length() == 0) {
                    JOptionPane.showMessageDialog(this, "Problem file must have a name.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
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
                    File newFile = new File(projectDir.getAbsolutePath() + PROBLEMS_SUBDIR_PATH + File.separator + name);
                    
                    // check if file with this name already exists
                    if (newFile.exists()) {
                        JOptionPane.showMessageDialog(this, "File " + name + " already exists.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                    } else {
                        try {
                            // create new empty file
                            newFile.createNewFile();
                            
                            // write empty grid to file
                            BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
                            writer.write(sb.toString());
                            writer.flush();
                            writer.close();
                            
                            // update file structure
                            fileStructPane.updateDirTree();
                        } catch (IOException e) {
                            // if error while writing, delete file
                            newFile.delete();
                        }
                    }
                }
            }
        }
    }
//</editor-fold>
    
    /**
     * Creates a new project.
     */
    //<editor-fold defaultstate="collapsed" desc="Create New Project">
    private void createNewProject() {
        String projectName = JOptionPane.showInputDialog(this, "Project name:");
        
        if (projectName != null && projectName.length() > 0) {
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
    }
//</editor-fold>
    
    /**
     * Creates a new solver file.
     */
    //<editor-fold defaultstate="collapsed" desc="Create New Solver">
    public void createNewSolver() {
        // init dialog component
        String solverTypeNames[] = {"Breadth-first search", "Depth-first search", "A* search", "Iterative deepening A* search"};
        JTextField nameInput = new JTextField(30);
        JComboBox solverTypes = new JComboBox(solverTypeNames);
        JTextField projectName = new JTextField(30);
        
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.setPreferredSize(new Dimension(300, 150));
        inputPanel.add(new JLabel("Name: "));
        inputPanel.add(nameInput);
        inputPanel.add(Box.createVerticalStrut(15));
        inputPanel.add(Box.createVerticalStrut(15));
        inputPanel.add(new JLabel("Solver type:"));
        inputPanel.add(solverTypes);
        inputPanel.add(Box.createVerticalStrut(15));
        inputPanel.add(Box.createVerticalStrut(15));
        inputPanel.add(new JLabel("Project: "));
        inputPanel.add(projectName);
        
        // get last clicked tree path
        TreePath path = fileStructPane.getClickedTreePath();
        if (path != null) {
            // find the project dir in path
            DefaultMutableTreeNode node = fileStructPane.getProjectTreeNodeOnPath(path);
            DataFile data = (DataFile) node.getUserObject();
            
            projectName.setText(data.toString());
            
            // ask for input
            int result = JOptionPane.showConfirmDialog(this, inputPanel, "New solver", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String solverName = nameInput.getText();
                String solverType = solverTypes.getSelectedItem().toString();
                String project = projectName.getText();
                
                if (solverName != null && solverType != null && project != null) {
                    
                    // if solver needs heuristics, get user input
                    String heuristicsType = "";
                    if (solverType.equals("A* search") || solverType.equals("Iterative deepening A* search")) {
                        String heurTypeNames[] = {"Manhattan", "Euclidean", "Hungarian"};
                        JComboBox heurTypes = new JComboBox(heurTypeNames);
                        
                        JPanel heurInputPanel = new JPanel(new GridLayout(1, 2));
                        heurInputPanel.setPreferredSize(new Dimension(300, 100));
                        heurInputPanel.add(new JLabel("Heuristics type:"));
                        heurInputPanel.add(heurTypes);
                        
                        // ask for input
                        int heurResult = JOptionPane.showConfirmDialog(this, heurInputPanel, "Choose heuristics", JOptionPane.OK_CANCEL_OPTION);
                        if (heurResult == JOptionPane.OK_OPTION) {
                            heuristicsType = heurTypes.getSelectedItem().toString();
                        } else {
                            heuristicsType = "Manhattan";
                        }
                    }
                    
                    // check solver name
                    if (solverName.length() > 0 && !solverName.endsWith(".slvr"))
                        solverName = solverName.concat(".slvr");
                    
                    File projectDir;
                    
                    // name of project to contain file
                    if (!project.equals(data.toString())) {
                        projectDir = new File(PROJECTS_DIR_PATH + File.separator + project);
                        
                        if (!projectDir.exists()) {
                            JOptionPane.showMessageDialog(this, "No existing project named " + project + ".", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    } else {
                        projectDir = data.getDataFile();
                    }
                    
                    // check if input parameters are acceptable
                    if (solverName.length() == 0) {
                        JOptionPane.showMessageDialog(this, "Solver must have a name.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                    } else {
                        // check if files exist
                        File solverFile = new File(projectDir.getAbsolutePath() + SOLVERS_SUBDIR_PATH + File.separator + solverName);
                        
                        if (solverFile.exists()) {
                            JOptionPane.showMessageDialog(this, "A solver named " + solverName + " already exists.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                        } else {
                            try {
                                // create a new solver file
                                solverFile.createNewFile();
                                
                                // write data to file
                                SolverWriter writer = new SolverWriter(solverFile);
                                writer.write(solverType, heuristicsType);
                                
                                // update file structure
                                fileStructPane.updateDirTree();
                            } catch (IOException e) {
                                // if error while writing, delete file
                                solverFile.delete();
                            }
                        }
                    }
                }
            }
        }
    }
//</editor-fold>
    
    
    
    
    /*
        SOLVING CONTROLS
    */
    //<editor-fold defaultstate="collapsed" desc="Solving Controls">
    /**
     * Signalizes the solver to find a next state.
     */
    private void nextState(boolean repaint) {
        JPanel visualPanel = getSelectedVisualizationPanel();
        VisualizationPanel vPanel = null;
        SolutionVisualizationPanel svPanel = null;
        if (visualPanel instanceof VisualizationPanel) {
            vPanel = (VisualizationPanel) visualPanel;
        } else if (visualPanel instanceof SolutionVisualizationPanel) {
            svPanel = (SolutionVisualizationPanel) visualPanel;
        }
        
        if (vPanel != null && vPanel.isDone()) {
            if (vPanel.isSolutionFound()) {
                if (solvingTimer.isRunning()) {
                    finishSolving();
                    
                    JOptionPane.showMessageDialog(this, "Solution has been found!\nSolution: " + vPanel.getSolution(), "Solving completed", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    
                    vPanel.saveStats();
                }
            } else {
                if (vPanel.isStillSolving()) {
                    vPanel.nextState(repaint);
                } else {
                    if (solvingTimer.isRunning()) {
                        finishSolving();
                        JOptionPane.showMessageDialog(this, "No solution has been found.", "Solving completed", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        } else if (svPanel != null) {
            if (!svPanel.hasFinished()) {
                svPanel.nextStep();
            } else {
                if (solvingTimer.isRunning()) {
                    finishSolving();
                    JOptionPane.showMessageDialog(this, "Solution has ended.", "Solution ended", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
    
    
//    private void nextStateSolveImediately() {
//        VisualizationPanel visualPanel = getSelectedVisualizationPanel();
//        if (visualPanel != null) {
//            if (visualPanel.isSolutionFound()) {
//                finishSolving();
//                    
//                JOptionPane.showMessageDialog(this, "Solution has been found!\nSolution: " + visualPanel.getSolution(), "Solving completed", javax.swing.JOptionPane.INFORMATION_MESSAGE);
//                    
//                visualPanel.saveStats();
//            } else {
//                if (visualPanel.isStillSolving()) {
//                    visualPanel.nextState(true);
//                } else {
//                    finishSolving();
//                    JOptionPane.showMessageDialog(this, "No solution has been found.", "Solving completed", javax.swing.JOptionPane.INFORMATION_MESSAGE);
//                }
//            }
//        }
//    }

    
    /**
     * Signalizes the solver to restore a previous state.
     */
    private void prevState() {
        JPanel visualPanel = getSelectedVisualizationPanel();
        if (visualPanel instanceof VisualizationPanel) {
            VisualizationPanel vPanel = (VisualizationPanel) visualPanel;
            vPanel.prevState();
        } else {
            SolutionVisualizationPanel svPanel = (SolutionVisualizationPanel) visualPanel;
            svPanel.prevStep();
        }
    }
    
    /**
     * Signalizes the solver to start solving.
     */
    private void startSolving() {
        if (!solvingTimer.isRunning()) {
            toolBarStopButton.setEnabled(true);
            toolBarPauseResumeButton.setEnabled(true);
            toolBarPauseResumeButton.setActionCommand(PAUSE);
            toolBarPauseResumeButton.setIcon(new ImageIcon(packer.getImage(ImagePacker.PAUSE)));
            solvingTimer.start();
        } else {
            // solve imediately
            solvingTimer.stop();
            
//            VisualizationPanel visualPanel = getSelectedVisualizationPanel();
//            if (visualPanel != null) {
//                java.awt.EventQueue.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        while (true) {
//                            if (visualPanel.isSolutionFound()) {
//                                break;
//                            } else {
//                                if (!visualPanel.isStillSolving()) {
//                                    break;
//                                }
//                            }
//
//                            nextStateSolveImediately();
//                        }
//
////                        nextState(true);
//                    }
//                });
                
//                while (!visualPanel.isSolutionFound()) {
//                    if (!visualPanel.isStillSolving()) {
//                        break;
//                    }
//                    
//                    nextState(false);
//                }
//                
//                nextState(true);
//            }
        }
    }
    
    /**
     * Signalizes the solver to pause the solving.
     */
    private void pauseSolving() {
        solvingTimer.stop();
    }
    
    /**
     * Signalizes the solver to resume the solving.
     */
    private void resumeSolving() {
        solvingTimer.start();
    }
    
    /**
     * Signalizes the solver to stop the solving and reset the data.
     */
    private void stopSolving() {
        finishSolving();
        resetSolver();
    }
    
    /**
     * Signalizes the solver to reset the data.
     */
    private void resetSolver() {
        solvingTimer.stop();
        JPanel visualPanel = getSelectedVisualizationPanel();
        if (visualPanel instanceof VisualizationPanel) {
            VisualizationPanel vPanel = (VisualizationPanel) visualPanel;
            vPanel.resetSolver();
        } else if (visualPanel instanceof SolutionVisualizationPanel) {
            SolutionVisualizationPanel svPanel = (SolutionVisualizationPanel) visualPanel;
            svPanel.stop();
        }
    }
    
    /**
     * Signalizes the solver to finish solving.
     */
    private void finishSolving() {
        solvingTimer.stop();
        toolBarStopButton.setEnabled(false);
        toolBarPauseResumeButton.setEnabled(false);
    }
//</editor-fold>
 
    /**
     * Checks if the currently displayed panel is a VisualizationPanel and returns it.
     * @return currently displayed VisualizationPanel
     */
    //<editor-fold defaultstate="collapsed" desc="Get Selected Visualization Panel">
    private JPanel getSelectedVisualizationPanel() {
        Component comp = displayPane.getSelectedComponent();
        if (comp instanceof VisualizationPanel) {
            VisualizationPanel visualPanel = (VisualizationPanel) comp;
            return visualPanel;
        } else if (comp instanceof SolutionVisualizationPanel) {
            SolutionVisualizationPanel visualPanel = (SolutionVisualizationPanel) comp;
            return visualPanel;
        } else {
            return null;
        }
    }
//</editor-fold>
    
    
    
    
    /*
        EDIT PANEL STATE LISTENER METHODS
    */
    
    /**
     * Changes the tab text if the user changed a state or saved.
     * @param tabIndex index of the currently displayed panel
     */
    //<editor-fold defaultstate="collapsed" desc="State Changed">
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
//</editor-fold>
    
    
    
    
    /*
        FILE STRUCTURE PANEL LISTENER METHODS
    */
    
    /**
     * Deletes a file in the file structure.
     */
    //<editor-fold defaultstate="collapsed" desc="Delete">
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
//</editor-fold>
    
    /**
     * Deletes recursively all folders and files under the given file's path.
     * @param file file from which to start deleting
     */
    //<editor-fold defaultstate="collapsed" desc="Delete Recursively">
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
//</editor-fold>
    
    /**
     * Exports a statistics file.
     */
    //<editor-fold defaultstate="collapsed" desc="Export Stat File">
    public void exportSingleFile() {
        TreePath path = fileStructPane.getClickedTreePath();
        if (path != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            DataFile data = (DataFile) node.getUserObject();
            
            if (data.getFileType() != DataFile.STAT) {
                JOptionPane.showMessageDialog(this, "Cannot export this file.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            } else {
                File selectedFile = selectFile();
                
                if (selectedFile != null) {
                    if (!selectedFile.getName().endsWith(".csv")) {
                        JOptionPane.showMessageDialog(this, "Invalid file name extension. File name should end in '.csv'.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                    } else {
                        if (!selectedFile.exists()) {
                            try {
                                selectedFile.createNewFile();
                            } catch (IOException e) {
                                JOptionPane.showMessageDialog(this, "Could not export statistic data. Error while creating file.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        }
                        
                        StatsReader reader = new StatsReader(data.getDataFile());
                        StatsWriter writer = new StatsWriter(selectedFile);
                        
                        if (reader.isEnabled() && writer.isEnabled()) {
                            writer.setUpCsv();
                            
                            String line = reader.readLine();;
                            while (line != null) {
                                line = line.replace(":", ",");
                                writer.writeToCsvFile(line);
                                line = reader.readLine();
                            }
                            
                            reader.close();
                            writer.close();
                            
                            JOptionPane.showMessageDialog(this, "Statistic data has been exported.", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
                
                JOptionPane.showMessageDialog(this, "Could not export statistic data.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        }
    }
//</editor-fold>
    
    
    
    
    /*
        ACTION EVENT LISTENER METHODS
    */
    
    /**
     * Method implemented for the ActionListener interface. Handles the solving command button actions.
     * @param e action event triggering this method
     */
    //<editor-fold defaultstate="collapsed" desc="Action Performed">
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().contains("Tab")) {
            // closing a tab
            
            // get tab index
            String tabName = e.getActionCommand().replaceAll("Tab", "");
    
            // check closing tab type
            int tabIndex = displayPane.indexOfTab(tabName);
            Component comp = displayPane.getComponentAt(tabIndex);
                
            if (comp != null) {
                if (comp instanceof EditProblemPanel) {
                   // if edit problem file, save progress
                    EditProblemPanel editPanel = (EditProblemPanel) comp;
                    editPanel.save();
                } else if (comp instanceof VisualizationPanel) {
                    // if visualization panel, disable tool bar buttons
                    toolBarStartButton.setEnabled(false);
                    toolBarStopButton.setEnabled(false);
                    toolBarPauseResumeButton.setEnabled(false);
                    toolBarNextButton.setEnabled(false);
                    toolBarPrevButton.setEnabled(false);
                    toolBarResetButton.setEnabled(false);

                    // remove stats display
                    solveSettingsPane.removeAll();
                    solveSettingsPane.repaint();

                    // stop solving if not stopped already
                    solvingTimer.stop();
                    
                    // reset the timer
                    solvingTimer.setDelay(800);
                } else if (comp instanceof SolutionVisualizationPanel) {
                    // if solution visualization panel, disable tool bar buttons
                    toolBarStartButton.setEnabled(false);
                    toolBarStopButton.setEnabled(false);
                    toolBarPauseResumeButton.setEnabled(false);
                    toolBarNextButton.setEnabled(false);
                    toolBarPrevButton.setEnabled(false);
                    toolBarResetButton.setEnabled(false);
                    
                    // stop displaying solution if not stopped already
                    solvingTimer.stop();
                }

                // then remove the panel
                displayPane.removeTabAt(tabIndex);

                // also remove this action listener from source button
                JButton closeButton = (JButton) e.getSource();
                closeButton.removeActionListener(this);
            }
        } else if (e.getActionCommand().equals(NEW_FILE)) {
            createNewProblemFile();
        } else if (e.getActionCommand().equals(NEW_PROJECT)) {
            createNewProject();
        } else if (e.getActionCommand().equals(NEW_SOLVER)) {
            createNewSolver();
        } else if (e.getActionCommand().equals(START)) {
            startSolving();
        } else if (e.getActionCommand().equals(STOP)) {
            stopSolving();
        } else if (e.getActionCommand().equals(PAUSE)) {
            JButton source = (JButton) e.getSource();
            source.setActionCommand(RESUME);
            source.setIcon(new ImageIcon(packer.getImage(ImagePacker.RESUME)));
            pauseSolving();
        } else if (e.getActionCommand().equals(RESUME)) {
            JButton source = (JButton) e.getSource();
            source.setActionCommand(PAUSE);
            source.setIcon(new ImageIcon(packer.getImage(ImagePacker.PAUSE)));
            resumeSolving();
        } else if (e.getActionCommand().equals(RESET)) {
            resetSolver();
        } else if (e.getActionCommand().equals(NEXT_STATE)) {
            nextState(true);
        } else if (e.getActionCommand().equals(PREV_STATE)) {
            prevState();
        }
    }
//</editor-fold>
    
    
    
    
    /*
        MOUSE LISTENER METHODS
    */
    
    //<editor-fold defaultstate="collapsed" desc="Mouse Listener Methods">
    /**
     * Method implemented for the MouseListener interface. Handles the opening of appropriate panels when a file is double clicked.
     * @param e mouse event triggering this method
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // check if double clicked on file
        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
            
            // retrieve path for click location
            JTree dirTree = fileStructPane.getDirTree();
            TreePath path = dirTree.getPathForLocation(e.getX(), e.getY());
            if (path != null) {
                // get node at end of path
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                DataFile data = (DataFile) node.getUserObject();
                
                // check type of file that has been clicked
                if (data.getFileType() == DataFile.PROBLEM) {
                    // problem file -> add a problem editor to display
                    addPanelToDisplay(new EditProblemPanel(this, data.getDataFile(), displayPane.getTabCount(), packer), data.getDataFile());
                } else if (data.getFileType() == DataFile.SOLVER) {
                    // solver file  -> add a solver visualizator to display
                    loadSolverVisualizator(path, data);
                } else if (data.getFileType() == DataFile.STAT) {
                    // stat file -> add a statistics visualizator to display
                    addPanelToDisplay(new StatisticsVisualPanel(data.getDataFile()), data.getDataFile());
                } else if (data.getFileType() == DataFile.SOLUTION) {
                    // solution file -> add a solution visualizator to display
                    addPanelToDisplay(new SolutionVisualizationPanel(data.getDataFile(), packer), data.getDataFile());
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
//</editor-fold>
    
    
    
    
    /*
        COMPONENT LISTENER METHODS
    */
    
    //<editor-fold defaultstate="collapsed" desc="Component Listener Methods">
    /**
     * Method implemented for the ComponentListener interface. Handles display resizing.
     * @param e container event triggering this method
     */
    @Override
    public void componentAdded(ContainerEvent e) {
        pack();
        Component compAdded = e.getChild();
        if (compAdded instanceof VisualizationPanel) {
            VisualizationPanel visualPanel = (VisualizationPanel) compAdded;
            visualPanel.adjustSize();
        }
    }
    
    @Override
    public void componentRemoved(ContainerEvent e) {}
//</editor-fold>
    
    
    
    
    /*
        CHANGE LISTENER METHODS
    */
    
    /**
     * Method implemented for the ChangeListener interface. Handles solving speed changes.
     * @param e change event triggering the method
     */
    //<editor-fold defaultstate="collapsed" desc="State Changed">
    @Override
    public void stateChanged(ChangeEvent e) {
        JSpinner spinner = (JSpinner) e.getSource();
        Integer value = (Integer) spinner.getValue();
        solvingTimer.setDelay(800 / value);
    }
//</editor-fold>
                                                          

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
    private JButton toolBarStartButton;
    private JButton toolBarStopButton;
    private JButton toolBarPauseResumeButton;
    private JButton toolBarResetButton;
    private JButton toolBarPrevButton;
    private JButton toolBarNextButton;
    private Timer solvingTimer;
    // End of variables declaration
}
