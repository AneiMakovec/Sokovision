/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.visualization;

import problem.State;
import graphics.support.ImagePacker;
import grid.Grid;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ContainerListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import plugin.Solver;
import solver.BreadthFirstSolver;
import support.reader.SokobanReader;

/**
 *
 * @author anei
 */
public class VisualizationPanel extends JPanel {
    
    private File problemFile;
    private final File solverFile;
    
    private BreadthFirstSolver solver;
    private Grid grid;
    private State state;
    
    private SokobanReader reader;
    
    private final boolean toolBarButtonsState[] = new boolean[5];
    
    public VisualizationPanel(File solverFile, File problemFile, ImagePacker packer) {
        this.problemFile = problemFile;
        this.solverFile = solverFile;
        initSolver();
        initComponents(packer);
    }
    
    @SuppressWarnings("unchecked")
    private void initComponents(ImagePacker packer) {
        // set button states
        
        
        statePanel = new DisplayStatePanel(packer, grid, reader.getWidth(), reader.getHeight(), solver);
        stateTreePanel = new DisplayStateTreePanel();
        statisticsPanel = new DisplayStatisticsPanel();
        memCpuPanel = new DisplayMemCpuPanel();
        
        // set up
        statePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        stateTreePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        statisticsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        memCpuPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        
        setLayout(new GridLayout(2, 2));
        
        add(statePanel);
        add(stateTreePanel);
        add(memCpuPanel);
        add(statisticsPanel);
    }
    
    private void initSolver() {
        grid = new Grid();
        state = new State();
        
        reader = new SokobanReader(problemFile);
        if (reader.isEnabled()) {
            reader.read(grid, state);
            
            solver = new BreadthFirstSolver(state, grid);
            solver.initialize();
        }
    }
    
    
    
    public void adjustSize() {
        statePanel.resizeGrid();
    }
    
    public void nextState() {
        solver.nextState();
    }
    
    public void repaintState() {
        statePanel.repaint();
        stateTreePanel.repaint();
    }
    
    
    // Variables declaration
    private DisplayStatePanel statePanel;
    private DisplayStateTreePanel stateTreePanel;
    private DisplayStatisticsPanel statisticsPanel;
    private DisplayMemCpuPanel memCpuPanel;
    // End of variable declaration
}
