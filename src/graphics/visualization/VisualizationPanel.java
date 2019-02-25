/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.visualization;

import problem.State;
import graphics.support.ImagePacker;
import graphics.ui.SolveSettingsPanel;
import grid.Grid;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;
import plugin.Solver;
import solver.AStarSolver;
import solver.BreadthFirstSolver;
import solver.DepthFirstSolver;
import solver.IDAStarSolver;
import support.reader.SokobanReader;
import support.reader.SolverReader;
import support.reader.StatsReader;
import support.writer.SolutionWriter;
import support.writer.SolverWriter;
import support.writer.StatsWriter;

/**
 *
 * @author anei
 */
public class VisualizationPanel extends JPanel {
    
    private File problemFile;
    private final File solverFile;
    private final File statsFile;
    private final File solutionFile;
    
    private Solver solver;
    private Grid grid;
    private State state;
    
    private SokobanReader sokobanReader;
    
    private final boolean toolBarButtonsState[] = new boolean[5];
    
    private boolean done;
    
    public VisualizationPanel(File solverFile, File problemFile, File statsFile, File solutionFile, ImagePacker packer, SolveSettingsPanel settingsPanel, ChangeListener listener) {
        this.problemFile = problemFile;
        this.solverFile = solverFile;
        this.statsFile = statsFile;
        this.solutionFile = solutionFile;
        this.done = true;
        initSolver();
        initComponents(packer, settingsPanel, listener);
    }
    
    @SuppressWarnings("unchecked")
    private void initComponents(ImagePacker packer, SolveSettingsPanel settingsPanel, ChangeListener listener) {
        // set button states
        // TODO
        
        layers = new JLayeredPane();
        statePanel = new DisplayStatePanel(packer, grid, sokobanReader.getWidth(), sokobanReader.getHeight(), solver.getState());
        stateTreePanel = new DisplayStateTreePanel(solver);
        statisticsPanel = new DisplayStatisticsPanel(solver.getStats(), listener);
        
        // set up
        statePanel.setBorder(BorderFactory.createLineBorder(Color.black));

        setLayout(new BorderLayout());
        
        add(layers, BorderLayout.CENTER);

        layers.add(stateTreePanel, 0, 0);
        layers.add(statePanel, 1, 0);
        
        
        // set up settingsPanel
        settingsPanel.add(statisticsPanel, BorderLayout.CENTER);
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

    public boolean isDone() {
        return done;
    }
    
    
    
    
    public void adjustSize() {
        statePanel.setBounds(0, 0, layers.getWidth() / 3, layers.getHeight() / 3);
        stateTreePanel.setBounds(layers.getBounds());
        
        statePanel.resizeGrid();
        stateTreePanel.resizeTree();
    }
    
    public void nextState(boolean repaint) {
        done = false;
        solver.nextState();
        repaintState(repaint);
        done = true;
    }
    
    public void prevState() {
        solver.prevState();
        repaintState(true);
    }
    
    public void saveStats() {
        StatsWriter statWriter = new StatsWriter(statsFile);
        if (statWriter.isEnabled()) {
            statWriter.writeToStatFile(problemFile.getName(), solver.getStats(), state);
        }
        
        SolutionWriter solutionWriter = new SolutionWriter(solutionFile);
        if (solutionWriter.isEnabled()) {
            solutionWriter.write(problemFile.getName(), solver.getStats().getSolution());
        }
    }
    
    public boolean exportStats(File csvFile) {
        StatsReader reader = new StatsReader(statsFile);
        StatsWriter writer = new StatsWriter(csvFile);
        
        if (reader.isEnabled() && writer.isEnabled()) {
            writer.setUpCsv();
            
            String line = reader.readLine();
            while (line != null) {
                line = line.replace(":", ",");
                writer.writeToCsvFile(line);
                line = reader.readLine();
            }
            
            reader.close();
            writer.close();
            
            return true;
        }
        
        return false;
    }
    
    public void resetSolver() {
        solver.reset();
        repaintState(true);
    }
    
    public boolean isSolutionFound() {
//        if (solver.isSolutionFound()) {
//            solver.getStats().makeTimeStamp();
//            return true;
//        } else {
//            return false;
//        }
        return solver.isSolutionFound();
    }
    
    public boolean isStillSolving() {
        return solver.isStillSolving();
    }
    
    public String getSolution() {
        return solver.getStats().getSolution();
    }
    
    private void repaintState(boolean repaint) {
        if (repaint) {
            statePanel.updateState(solver.getState());
            stateTreePanel.repaint();
        }
        
        statisticsPanel.updateStats();
        
        // store data with time
        solver.getStats().makeTimeStamp();
    }
    
    
    // Variables declaration
    private JLayeredPane layers;
    private DisplayStatePanel statePanel;
    private DisplayStateTreePanel stateTreePanel;
    private DisplayStatisticsPanel statisticsPanel;
    // End of variable declaration
}
