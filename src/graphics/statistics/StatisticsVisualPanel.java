/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.statistics;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import support.reader.StatsReader;

/**
 *
 * @author anei
 */
public class StatisticsVisualPanel extends JPanel implements ActionListener, ItemListener {
    
    private static final String ALL = "all";
    private static final String SOLVING_TIME = "time";
    private static final String SOLUTION_LENGTH = "sol_length";
    private static final String STATES_EXAMINED = "states_examined";
    private static final String STATES_ALREADY_SEEN = "states_seen";
    private static final String STATES_IN_FRINGE = "states_in_fringe";
    private static final String DEADLOCKS_FOUND = "deadlocks";
    
    private final File statFile;
    
    private String currentProblem;
    private String currentCommand;
    
    private final HashMap<String, ArrayList<StatData>> problemDataMap;
    private String[] problemNames;
    
    public StatisticsVisualPanel(File statFile) {
        this.statFile = statFile;
        this.problemDataMap = new HashMap<>();
        parseData();
        initComponents();
    }
    
    
    
    private void parseData() {
        StatsReader reader = new StatsReader(statFile);
        if (reader.isEnabled()) {
            HashSet<String> set = new HashSet<>();
            
            // start reading
            String line = reader.readLine();
            while (line != null && !line.equals("")) {
                // split line in data chunks
                String[] splitLine = line.split(":");
                
                // create a data object and collect data
                StatData data = new StatData(splitLine);
//                data.parseData(splitLine);
                
                // if data map does not already contain data for this problem
                String problemName = splitLine[0].replace(".txt", "");
                if (!problemDataMap.containsKey(problemName)) {
                    // create a new data array
                    problemDataMap.put(problemName, new ArrayList<>());
                }
                
                // assign data to problem
                problemDataMap.get(problemName).add(data);
                
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
            
            currentCommand = ALL;
        }
    }
    
    
    @SuppressWarnings("unchecked")
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // setup toolbar
        JToolBar toolbar = new JToolBar();
        
        JLabel cratesNumText = new JLabel("Show data for problem:");
        toolbar.add(cratesNumText);
        
        problemSelect = new JComboBox(problemNames);
        problemSelect.setSelectedIndex(0);
        problemSelect.addItemListener(this);
        toolbar.add(problemSelect);
        
        toolbar.addSeparator();
        
        JButton button = new JButton("All");
        button.setActionCommand(ALL);
        button.addActionListener(this);
        toolbar.add(button);
        
//        button = new JButton("Solving time");
//        button.setActionCommand(SOLVING_TIME);
//        button.addActionListener(this);
//        toolbar.add(button);
        
        button = new JButton("Solution depth");
        button.setActionCommand(SOLUTION_LENGTH);
        button.addActionListener(this);
        toolbar.add(button);
        
        button = new JButton("States examined");
        button.setActionCommand(STATES_EXAMINED);
        button.addActionListener(this);
        toolbar.add(button);
        
        button = new JButton("Duplicate states");
        button.setActionCommand(STATES_ALREADY_SEEN);
        button.addActionListener(this);
        toolbar.add(button);
        
        button = new JButton("Frontier states");
        button.setActionCommand(STATES_IN_FRINGE);
        button.addActionListener(this);
        toolbar.add(button);
        
        button = new JButton("Deadlocks found");
        button.setActionCommand(DEADLOCKS_FOUND);
        button.addActionListener(this);
        toolbar.add(button);
        
        add(toolbar, BorderLayout.PAGE_START);
        
        
        // setup chart panel
        dataset = new DefaultCategoryDataset();
        updateDataset();
        JFreeChart chart = ChartFactory.createLineChart("Solving data", "Time", "States", dataset, PlotOrientation.VERTICAL, true, true, false);
        chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
    }
    
    
    private void updateDisplay() {
        updateDataset();
        repaint();
    }
    
    private void updateDataset() {
//        String time = "SOLVING TIME";
        String solutionLength = "SOLUTION DEPTH";
        String statesExamined = "STATES EXAMINED";
        String statesAlreadySeen = "DUPLICATE STATES";
        String statesInFringe = "FRONTIER STATES";
        String deadlocksFound = "DEADLOCKS FOUND";
        
        dataset.clear();
        
        for (StatData data : problemDataMap.get(currentProblem)) {
            switch (currentCommand) {
                case ALL:
                    dataset.addValue(data.getSolutionLength(), solutionLength, Double.toString(data.getSolvingTime()));
                    dataset.addValue(data.getStatesExamined(), statesExamined, Double.toString(data.getSolvingTime()));
                    dataset.addValue(data.getStatesAlreadySeen(), statesAlreadySeen, Double.toString(data.getSolvingTime()));
                    dataset.addValue(data.getStatesInFringe(), statesInFringe, Double.toString(data.getSolvingTime()));
                    dataset.addValue(data.getDeadlocksFound(), deadlocksFound, Double.toString(data.getSolvingTime()));
                    break;

                case SOLUTION_LENGTH:
                    dataset.addValue(data.getSolutionLength(), solutionLength, Double.toString(data.getSolvingTime()));
                    break;

                case STATES_EXAMINED:
                    dataset.addValue(data.getStatesExamined(), statesExamined, Double.toString(data.getSolvingTime()));
                    break;

                case STATES_ALREADY_SEEN:
                    dataset.addValue(data.getStatesAlreadySeen(), statesAlreadySeen, Double.toString(data.getSolvingTime()));
                    break;

                case STATES_IN_FRINGE:
                    dataset.addValue(data.getStatesInFringe(), statesInFringe, Double.toString(data.getSolvingTime()));
                    break;

                case DEADLOCKS_FOUND:
                    dataset.addValue(data.getDeadlocksFound(), deadlocksFound, Double.toString(data.getSolvingTime()));
                    break;

                default:
                break;
            }
        }
    }
    
    

    
    /*
        ACTION LISTENER METHODS
    */
    @Override
    public void actionPerformed(ActionEvent e) {
        currentCommand = e.getActionCommand();
        updateDisplay();
    }
    
    
    
    /*
        ITEM LISTENER METHODS
    */
    @Override
    public void itemStateChanged(ItemEvent e) {
        currentProblem = (String)e.getItem();
        updateDisplay();
    }
    
    
    // Variable declaration
    private JComboBox problemSelect;
    private ChartPanel chartPanel;
    private DefaultCategoryDataset dataset;
    // End of variable declaration
}
