/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sokovision.graphics.visualization;

import com.sokovision.graphics.support.ImagePacker;
import com.sokovision.grid.Grid;
import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import com.sokovision.main.MainFrame;
import com.sokovision.problem.Node;
import com.sokovision.problem.State;
import com.sokovision.support.reader.SokobanReader;
import com.sokovision.support.reader.SolutionReader;

/**
 *
 * @author anei
 */
public class SolutionVisualizationPanel extends JPanel implements ItemListener {
    
    private ImagePacker packer;
    
    private final File solutionFile;
    private File problemFile;
    
    private final HashMap<String, ArrayList<String>> solutionMap;
    
    private String[] problemNames;
    private String currentProblem;
    private int currentSolutionIndex;
    private String currentSolution;
    private int solutionPointer;
    
    
    private Grid grid;
    private State state;
    private int gridWidth;
    private int gridHeight;
    
    public SolutionVisualizationPanel(File solutionFile, ImagePacker packer) {
        this.packer = packer;
        this.solutionFile = solutionFile;
        this.solutionMap = new HashMap<>();
        this.grid = new Grid();
        this.state = new State();
        parseData();
        findCurrentProblemFile();
        readProblemFile();
        initComponents();
    }
    
    
    private void parseData() {
        SolutionReader reader = new SolutionReader(solutionFile);
        if (reader.isEnabled()) {
            HashSet<String> set = new HashSet<>();
            
            // start reading
            String line = reader.readLine();
            while (line != null && !line.equals("")) {
                // split line in data chunks
                String[] splitLine = line.split(":");
                
                // if solution map does not already contain a solution for this problem
                String problemName = splitLine[0].replace(".txt", "");
                if (!solutionMap.containsKey(problemName)) {
                    // create a new data array
                    solutionMap.put(problemName, new ArrayList<>());
                }
                
                // assign solution to problem
                if (!solutionMap.get(problemName).contains(splitLine[1]))
                    solutionMap.get(problemName).add(splitLine[1]);
                
                // also add problem name to available problems
                set.add(problemName);
                
                // read next line
                line = reader.readLine();
            }
            
            // finish reading
            reader.close();
            
            // get available problems
            problemNames = new String[set.size()];
            set.toArray(problemNames);
            Arrays.sort(problemNames);
            
            if (set.size() > 0)
                currentProblem = problemNames[0];
            
            currentSolutionIndex = 0;
            currentSolution = solutionMap.get(currentProblem).get(currentSolutionIndex);
            solutionPointer = 0;
        }
    }
    
    
    @SuppressWarnings("unchecked")
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // setup toolbar
        JToolBar toolbar = new JToolBar();
        
        JLabel solutionText = new JLabel("Show solution:");
        toolbar.add(solutionText);
        
        solutionSelect = new JComboBox();
        updateSolutionSelector();
        solutionSelect.setSelectedIndex(0);
        solutionSelect.addItemListener(this);
        toolbar.add(solutionSelect);
        
        toolbar.addSeparator();
        
        JLabel forProblemText = new JLabel("for problem:");
        toolbar.add(forProblemText);
        
        problemSelect = new JComboBox(problemNames);
        problemSelect.setSelectedIndex(0);
        problemSelect.addItemListener(this);
        toolbar.add(problemSelect);
        
        add(toolbar, BorderLayout.PAGE_START);
        
        
        // init display
        if (grid != null && state != null) {
            stateDisplay = new DisplayStatePanel(packer, grid, gridWidth, gridHeight, new Node(null, state, "", 0));
            add(stateDisplay, BorderLayout.CENTER);
        }
    }
    
    
    private void findCurrentProblemFile() {
        File statDir = solutionFile.getParentFile();
        if (statDir != null) {
            // move into project directory
            String projectDirPath = statDir.getParent();
            
            String problemName = currentProblem + ".txt";
            
            // search for problem file
            problemFile = new File(projectDirPath + MainFrame.PROBLEMS_SUBDIR_PATH + File.separator + problemName);
            if (!problemFile.exists()) {
                problemFile = null;
            }
        } else {
            problemFile = null;
        }
    }
    
    
    private void readProblemFile() {
        if (problemFile != null) {
            SokobanReader reader = new SokobanReader(problemFile);
            if (reader.isEnabled()) {
                reader.read(grid, state);
                gridWidth = reader.getWidth();
                gridHeight = reader.getHeight();
            } else {
                grid = null;
                state = null;
            }
        } else {
            grid = null;
            state = null;
        }
    }
    
    
    private void updateSolutionSelector() {
        ArrayList<String> list = solutionMap.get(currentProblem);
        
        String[] solutions = new String[list.size()];
        for (int i = 0; i < solutions.length; i++) {
            solutions[i] = Integer.toString(i);
        }
        
        solutionSelect.setModel(new DefaultComboBoxModel(solutions));
        solutionSelect.setSelectedIndex(0);
        
        currentSolutionIndex = 0;
    }
    
    
    
    private void updateProblem() {
        updateSolutionSelector();
        
        updateSolution();
    }
    
    private void updateSolution() {
        findCurrentProblemFile();
        readProblemFile();
        
        if (grid != null && state != null) {
            stateDisplay.updateGrid(grid);
            stateDisplay.updateState(new Node(null, state, "", 0));
            stateDisplay.updateGridSize(gridWidth, gridHeight);
        }
        
        currentSolution = solutionMap.get(currentProblem).get(currentSolutionIndex);
        solutionPointer = 0;
        
        stateDisplay.repaint();
//        repaint();
     }
    
   

    /*
        ITEM LISTENER METHODS
    */
    @Override
    public void itemStateChanged(ItemEvent e) {
        String item = (String) e.getItem();
        boolean solution;
        
        try {
            currentSolutionIndex = Integer.parseInt(item);
            solution = true;
        } catch(NumberFormatException ex) {
            currentProblem = item;
            solution = false;
        }
        
        if (solution)
            updateSolution();
        else
            updateProblem();
    }
    
    
    public boolean hasFinished() {
        return solutionPointer == currentSolution.length();
    }
    
    
    public void nextStep() {
        switch(currentSolution.charAt(solutionPointer)) {
            case 'u':
                // up
                state.setWorker(state.getWorker().getUp());
                if (state.hasCrate(state.getWorker())) {
                    state.removeCrate(state.getWorker());
                    state.addCrate(state.getWorker().getUp());
                }
                break;
                
            case 'd':
                // down
                state.setWorker(state.getWorker().getDown());
                if (state.hasCrate(state.getWorker())) {
                    state.removeCrate(state.getWorker());
                    state.addCrate(state.getWorker().getDown());
                }
                break;
                
            case 'l':
                // left
                state.setWorker(state.getWorker().getLeft());
                if (state.hasCrate(state.getWorker())) {
                    state.removeCrate(state.getWorker());
                    state.addCrate(state.getWorker().getLeft());
                }
                break;
                
            case 'r':
                // right
                state.setWorker(state.getWorker().getRight());
                if (state.hasCrate(state.getWorker())) {
                    state.removeCrate(state.getWorker());
                    state.addCrate(state.getWorker().getRight());
                }
                break;
            
            default:
                break;
        }
        
        solutionPointer++;
        stateDisplay.repaint();
    }
    
    public void prevStep() {
        if (solutionPointer > 0) {
            solutionPointer--;

            switch(currentSolution.charAt(solutionPointer)) {
                case 'u':
                    // down
                    if (state.hasCrate(state.getWorker().getUp())) {
                        state.removeCrate(state.getWorker().getUp());
                        state.addCrate(state.getWorker());
                    }
                    state.setWorker(state.getWorker().getDown());
                    break;

                case 'd':
                    // up
                    if (state.hasCrate(state.getWorker().getDown())) {
                        state.removeCrate(state.getWorker().getDown());
                        state.addCrate(state.getWorker());
                    }
                    state.setWorker(state.getWorker().getUp());
                    break;

                case 'l':
                    // right
                    if (state.hasCrate(state.getWorker().getLeft())) {
                        state.removeCrate(state.getWorker().getLeft());
                        state.addCrate(state.getWorker());
                    }
                    state.setWorker(state.getWorker().getRight());
                    break;

                case 'r':
                    // left
                    if (state.hasCrate(state.getWorker().getRight())) {
                        state.removeCrate(state.getWorker().getRight());
                        state.addCrate(state.getWorker());
                    }
                    state.setWorker(state.getWorker().getLeft());
                    break;

                default:
                    break;
            }

            stateDisplay.repaint();
        }
    }
    
    public void stop() {
        updateSolution();
    }
    
    
    
    
    // Variable declaration
    private JComboBox problemSelect;
    private JComboBox solutionSelect;
    private DisplayStatePanel stateDisplay;
    // End of variable declaration
}
