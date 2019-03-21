/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sokovision.graphics.visualization;

import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import com.sokovision.problem.StatCollector;

/**
 *
 * @author anei
 */
public class DisplayStatisticsPanel extends JPanel {
    
    private StatCollector stats;
    
    public DisplayStatisticsPanel(StatCollector stats, ChangeListener listener) {
        this.stats = stats;
        initComponents(listener);
    }
    
    
    @SuppressWarnings("unchecked")
    private void initComponents(ChangeListener listener) {
        JLabel solvingSpeedLabel = new JLabel("Solving speed:");
        JLabel timeLabel = new JLabel("Solving time:");
        JLabel movesExaminedLabel = new JLabel("States examined (green):");
        JLabel statesAlreadyExaminedLabel = new JLabel("Duplicate states (yellow):");
        JLabel currentSolDepthLabel = new JLabel("Current solution depth:");
        JLabel deadlocksLabel = new JLabel("Deadlocks found (red):");
        JLabel statesInFringeLabel = new JLabel("Frontier states (black):");
        
        spinner = new JSpinner();
        timeText = new JLabel(String.format("%.4f", stats.getTime()) + "s");
        movesExaminedText = new JLabel(Integer.toString(stats.getMovesExamined()));
        statesAlreadyExaminedText = new JLabel(Integer.toString(stats.getStatesAlreadySeen()));
        currentSolDepthText = new JLabel(Integer.toString(stats.getSolutionDepth()));
        deadlocksText = new JLabel(Integer.toString(stats.getDeadlocksFound()));
        statesInFringeText = new JLabel(Integer.toString(stats.getStatesInFringe()));
        
        // set up spinner
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 20, 1);
        spinner.setModel(model);
        spinner.addChangeListener(listener);
        
        setLayout(new GridLayout(7, 2));
        
        add(solvingSpeedLabel);
        add(spinner);
        add(timeLabel);
        add(timeText);
        add(currentSolDepthLabel);
        add(currentSolDepthText);
        add(movesExaminedLabel);
        add(movesExaminedText);
        add(statesAlreadyExaminedLabel);
        add(statesAlreadyExaminedText);
        add(deadlocksLabel);
        add(deadlocksText);
        add(statesInFringeLabel);
        add(statesInFringeText);
    }
    
    
    
    public void updateStats() {
        timeText.setText(String.format("%.4f", stats.getTime()) + "s");
        movesExaminedText.setText(Integer.toString(stats.getMovesExamined()));
        statesAlreadyExaminedText.setText(Integer.toString(stats.getStatesAlreadySeen()));
        currentSolDepthText.setText(Integer.toString(stats.getSolutionDepth()));
        deadlocksText.setText(Integer.toString(stats.getDeadlocksFound()));
        statesInFringeText.setText(Integer.toString(stats.getStatesInFringe()));
    }
    
    
    // Variable declaration
    private JSpinner spinner;
    private JLabel timeText;
    private JLabel movesExaminedText;
    private JLabel statesAlreadyExaminedText;
    private JLabel currentSolDepthText;
    private JLabel deadlocksText;
    private JLabel statesInFringeText;
    // End of variable declaration
}
