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
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import plugin.Solver;
import solver.AStarSolver;
import solver.BreadthFirstSolver;
import solver.DepthFirstSolver;
import solver.IDAStarSolver;
import support.reader.SokobanReader;
import support.reader.SolverReader;
import support.writer.SolverWriter;

/**
 *
 * @author anei
 */
public class VisualizationPanel extends JPanel {
    
    private File problemFile;
    private final File solverFile;
    
    private Solver solver;
    private Grid grid;
    private State state;
    
    private SokobanReader sokobanReader;
    
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
        
        
        statePanel = new DisplayStatePanel(packer, grid, sokobanReader.getWidth(), sokobanReader.getHeight(), solver);
        stateTreePanel = new DisplayStateTreePanel(solver);
        statisticsPanel = new DisplayStatisticsPanel(solver.getStats());
        memCpuPanel = new DisplayMemCpuPanel();
        
        // set up
        statePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        stateTreePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        statisticsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        memCpuPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        
        setLayout(new GridLayout(2, 2));
        
        add(statePanel);
        add(stateTreePanel);
        add(statisticsPanel);
        add(memCpuPanel);
    }
    
    private void initSolver() {
        grid = new Grid();
        state = new State();
        
        sokobanReader = new SokobanReader(problemFile);
        if (sokobanReader.isEnabled()) {
            sokobanReader.read(grid, state);
            
            SolverReader solverReader = new SolverReader(solverFile);
            if (solverReader.isEnabled()) {
                solverReader.read();
                int solverType = solverReader.getSolverType();
                
                if (solverType == SolverWriter.BFS) {
                    solver = new BreadthFirstSolver(state, grid);
                } else if (solverType == SolverWriter.DFS) {
                    solver = new DepthFirstSolver(state, grid);
                } else if (solverType == SolverWriter.ASTAR) {
                    int heurType = solverReader.getHeurType();
                    if (heurType == SolverWriter.HEUR_MANHATTAN)
                        solver = new AStarSolver(state, grid, "M");
                    else if (heurType == SolverWriter.HEUR_EUCLIDEAN)
                        solver = new AStarSolver(state, grid, "E");
                    else if (heurType == SolverWriter.HEUR_HUNGARIAN)
                        solver = new AStarSolver(state, grid, "H");
                } else if (solverType == SolverWriter.IDASTAR) {
                    int heurType = solverReader.getHeurType();
                    if (heurType == SolverWriter.HEUR_MANHATTAN)
                        solver = new IDAStarSolver(state, grid, "M");
                    else if (heurType == SolverWriter.HEUR_EUCLIDEAN)
                        solver = new IDAStarSolver(state, grid, "E");
                    else if (heurType == SolverWriter.HEUR_HUNGARIAN)
                        solver = new IDAStarSolver(state, grid, "H");
                }
                
                solver.initialize();
            }
        }
    }
    
    
    
    public void adjustSize() {
        statePanel.resizeGrid();
        stateTreePanel.resizeTree();
    }
    
    public void nextState() {
        solver.nextState();
        repaintState();
    }
    
    public void resetSolver() {
        solver.reset();
        repaintState();
    }
    
    public boolean isSolutionFound() {
        return solver.isSolutionFound();
    }
    
    public boolean isStillSolving() {
        return solver.isStillSolving();
    }
    
    private void repaintState() {
        statePanel.repaint();
        stateTreePanel.repaint();
        statisticsPanel.updateStats();
        memCpuPanel.updateMemUsage();
    }
    
    
    // Variables declaration
    private DisplayStatePanel statePanel;
    private DisplayStateTreePanel stateTreePanel;
    private DisplayStatisticsPanel statisticsPanel;
    private DisplayMemCpuPanel memCpuPanel;
    // End of variable declaration
}
