/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.visualization;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import javax.swing.BorderFactory;

/**
 *
 * @author anei
 */
public class VisualizationPane extends javax.swing.JInternalFrame {
    
    private File problemFile;
    private int solverId;
    private String solverName;
    
    public VisualizationPane(int solverId, File problemFile) {
        this.problemFile = problemFile;
        this.solverId = solverId;
        initSolverName();
        initComponents();
    }
    
    @SuppressWarnings("unchecked")
    private void initComponents() {
        statePanel = new DisplayStatePanel();
        stateTreePanel = new DisplayStateTreePanel();
        statisticsPanel = new DisplayStatisticsPanel();
        memCpuPanel = new DisplayMemCpuPanel();
        
        // set up
        setDefaultCloseOperation(javax.swing.JInternalFrame.DISPOSE_ON_CLOSE);
        setTitle(solverName + ": ~" +problemFile.getName());
        setPreferredSize(new java.awt.Dimension(400, 400));
        setResizable(true);
        
        statePanel.setPreferredSize(new java.awt.Dimension(200, 200));
        stateTreePanel.setPreferredSize(new java.awt.Dimension(200, 200));
        statisticsPanel.setPreferredSize(new java.awt.Dimension(200, 200));
        memCpuPanel.setPreferredSize(new java.awt.Dimension(200, 200));
        
        statePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        stateTreePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        statisticsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        memCpuPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        
        GridLayout grid = new GridLayout(2, 2);
        setLayout(grid);
        
        add(statePanel);
        add(stateTreePanel);
        add(memCpuPanel);
        add(statisticsPanel);
        
        pack();
    }
    
    
    private void initSolverName() {
        switch (solverId) {
            case main.VisualizationFrame.SOLVER_BFS:
                solverName = "BFS";
                break;
            case main.VisualizationFrame.SOLVER_DFS:
                solverName = "DFS";
                break;
            case main.VisualizationFrame.SOLVER_A:
                solverName = "A*";
                break;
            case main.VisualizationFrame.SOLVER_IDA:
                solverName = "IDA*";
                break;
            default:
                break;
        }
    }
    
    
    // Variables declaration
    private DisplayStatePanel statePanel;
    private DisplayStateTreePanel stateTreePanel;
    private DisplayStatisticsPanel statisticsPanel;
    private DisplayMemCpuPanel memCpuPanel;
    // End of variable declaration
}
