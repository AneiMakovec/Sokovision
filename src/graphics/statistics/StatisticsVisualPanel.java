/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.statistics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
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
    
    private int currentNumCrates;
    private String currentCommand;
    
    private final HashMap<String, ArrayList<StatData>> problemDataMap;
    private String[] crateNumbers;
    
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
            while (line != null) {
                // split line in data chunks
                String[] splitLine = line.split(":");
                
                // create a data object and collect data
                StatData data = new StatData(Integer.parseInt(splitLine[1]));
                data.parseData(splitLine);
                
                // if data map does not already contain data for this problem
                String problemName = splitLine[0].replace(".txt", "");
                if (!problemDataMap.containsKey(problemName)) {
                    // create a new data array
                    problemDataMap.put(problemName, new ArrayList<>());
                }
                
                // assign data to problem
                problemDataMap.get(problemName).add(data);
                
                // also add number of crates to available crate numbers
                set.add(splitLine[1]);
                
                // read next line
                line = reader.readLine();
            }
            
            // finish reading
            reader.close();
            
            // get available crate numbers
            crateNumbers = new String[set.size()];
            set.toArray(crateNumbers);
            Arrays.sort(crateNumbers);
            
            if (set.size() > 0)
                currentNumCrates = Integer.parseInt(crateNumbers[0]);
            
            currentCommand = ALL;
        }
    }
    
    
    @SuppressWarnings("unchecked")
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // setup toolbar
        JToolBar toolbar = new JToolBar();
        
        JLabel cratesNumText = new JLabel("Show problems with crates:");
        toolbar.add(cratesNumText);
        
        cratesNumSelect = new JComboBox(crateNumbers);
        cratesNumSelect.setSelectedIndex(0);
        cratesNumSelect.addItemListener(this);
        toolbar.add(cratesNumSelect);
        
        toolbar.addSeparator();
        
        JButton button = new JButton("All");
        button.setActionCommand(ALL);
        button.addActionListener(this);
        toolbar.add(button);
        
        button = new JButton("Solving time");
        button.setActionCommand(SOLVING_TIME);
        button.addActionListener(this);
        toolbar.add(button);
        
        button = new JButton("Solution length");
        button.setActionCommand(SOLUTION_LENGTH);
        button.addActionListener(this);
        toolbar.add(button);
        
        button = new JButton("States examined");
        button.setActionCommand(STATES_EXAMINED);
        button.addActionListener(this);
        toolbar.add(button);
        
        button = new JButton("States already seen");
        button.setActionCommand(STATES_ALREADY_SEEN);
        button.addActionListener(this);
        toolbar.add(button);
        
        button = new JButton("States in fringe");
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
        JFreeChart chart = ChartFactory.createBarChart("Solving data", "Problem", "Value", dataset, PlotOrientation.VERTICAL, true, true, false);
        chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
    }
    
    
    private void updateDisplay() {
        updateDataset();
        repaint();
    }
    
    private void updateDataset() {
        String time = "SOLVING TIME";
        String solutionLength = "SOLUTION LENGTH";
        String statesExamined = "STATES EXAMINED";
        String statesAlreadySeen = "STATES ALREADY SEEN";
        String statesInFringe = "STATES IN FRINGE";
        String deadlocksFound = "DEADLOCKS FOUND";
        
        dataset.clear();
        
        for (String problem : problemDataMap.keySet()) {
            for (StatData data : problemDataMap.get(problem)) {
                if (data.getNumCrates() == currentNumCrates) {
                    switch (currentCommand) {
                        case ALL:
                            dataset.addValue(data.getSolvingTime(), time, problem);
                            dataset.addValue(data.getSolutionLength(), solutionLength, problem);
                            dataset.addValue(data.getStatesExamined(), statesExamined, problem);
                            dataset.addValue(data.getStatesAlreadySeen(), statesAlreadySeen, problem);
                            dataset.addValue(data.getStatesInFringe(), statesInFringe, problem);
                            dataset.addValue(data.getDeadlocksFound(), deadlocksFound, problem);
                            break;

                        case SOLVING_TIME:
                            dataset.addValue(data.getSolvingTime(), time, problem);
                            break;

                        case SOLUTION_LENGTH:
                            dataset.addValue(data.getSolutionLength(), solutionLength, problem);
                            break;

                        case STATES_EXAMINED:
                            dataset.addValue(data.getStatesExamined(), statesExamined, problem);
                            break;

                        case STATES_ALREADY_SEEN:
                            dataset.addValue(data.getStatesAlreadySeen(), statesAlreadySeen, problem);
                            break;

                        case STATES_IN_FRINGE:
                            dataset.addValue(data.getStatesInFringe(), statesInFringe, problem);
                            break;

                        case DEADLOCKS_FOUND:
                            dataset.addValue(data.getDeadlocksFound(), deadlocksFound, problem);
                            break;

                        default:
                            break;
                    }
                }
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
        currentNumCrates = Integer.parseInt((String)e.getItem());
        updateDisplay();
    }
    
    
    // Variable declaration
    private JComboBox cratesNumSelect;
    private ChartPanel chartPanel;
    private DefaultCategoryDataset dataset;
    // End of variable declaration
}
