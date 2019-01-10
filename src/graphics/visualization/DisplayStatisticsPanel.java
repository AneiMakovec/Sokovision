/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.visualization;

import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import problem.StatCollector;

/**
 *
 * @author anei
 */
public class DisplayStatisticsPanel extends JPanel {
    
    private StatCollector stats;
    
    public DisplayStatisticsPanel(StatCollector stats) {
        this.stats = stats;
        initComponents();
    }
    
    
    @SuppressWarnings("unchecked")
    private void initComponents() {
        JLabel timeLabel = new JLabel("Solving time:");
        JLabel movesExaminedLabel = new JLabel("States examined:");
        JLabel statesAlreadyExaminedLabel = new JLabel("States already examined:");
        JLabel currentSolDepthLabel = new JLabel("Current solution depth:");
        JLabel deadlocksLabel = new JLabel("Deadlocks found:");
        JLabel statesInFringeLabel = new JLabel("States in fringe:");
        
        timeText = new JLabel(String.format("%.4f", stats.getTime()) + "s");
        movesExaminedText = new JLabel(Integer.toString(stats.getMovesExamined()));
        statesAlreadyExaminedText = new JLabel(Integer.toString(stats.getStatesAlreadySeen()));
        currentSolDepthText = new JLabel(Integer.toString(stats.getSolutionDepth()));
        deadlocksText = new JLabel(Integer.toString(stats.getDeadlocksFound()));
        statesInFringeText = new JLabel(Integer.toString(stats.getStatesInFringe()));
        
        setLayout(new GridLayout(6, 2));
        
        add(timeLabel);
        add(timeText);
        add(movesExaminedLabel);
        add(movesExaminedText);
        add(statesAlreadyExaminedLabel);
        add(statesAlreadyExaminedText);
        add(currentSolDepthLabel);
        add(currentSolDepthText);
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
    private JLabel timeText;
    private JLabel movesExaminedText;
    private JLabel statesAlreadyExaminedText;
    private JLabel currentSolDepthText;
    private JLabel deadlocksText;
    private JLabel statesInFringeText;
    // End of variable declaration
}
