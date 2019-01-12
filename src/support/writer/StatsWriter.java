/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import problem.StatCollector;
import problem.State;

/**
 *
 * @author anei
 */
public class StatsWriter {
    
    private PrintWriter writer;
    private boolean isWritingStats;
    
    public StatsWriter(File fileName) {
        this.writer = null;
        
        if (fileName.getName().endsWith(".stat")) {
            isWritingStats = true;
        } else if (fileName.getName().endsWith(".csv")) {
            isWritingStats = false;
        } else {
            return;
        }
        
        try {
            if (isWritingStats)
                this.writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
            else 
                this.writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, false)));
        } catch (IOException e) {}
    }
    
    public boolean isEnabled() {
        return this.writer != null;
    }
    
    public void setUpCsv() {
        if (!isWritingStats) {
            StringBuilder sb = new StringBuilder();
        
            sb.append("Problem_name");
            sb.append(",");
            sb.append("Crates");
            sb.append(",");
            sb.append("Solving_time");
            sb.append(",");
            sb.append("Solution_length");
            sb.append(",");
            sb.append("States_examined");
            sb.append(",");
            sb.append("States_already_seen");
            sb.append(",");
            sb.append("States_in_fringe");
            sb.append(",");
            sb.append("Deadlocks");

            writer.println(sb.toString());
        }
    }
    
    
    public void writeToStatFile(String problemName, StatCollector statCollector, State state) {
        if (isWritingStats) {
            StringBuilder sb = new StringBuilder();

            sb.append(problemName);
            sb.append(":");
            sb.append(state.getCrates().size());
            sb.append(":");
            sb.append(statCollector.getTime());
            sb.append(":");
            sb.append(statCollector.getSolutionLength());
            sb.append(":");
            sb.append(statCollector.getMovesExamined());
            sb.append(":");
            sb.append(statCollector.getStatesAlreadySeen());
            sb.append(":");
            sb.append(statCollector.getStatesInFringe());
            sb.append(":");
            sb.append(statCollector.getDeadlocksFound());

            writer.println(sb.toString());
            close();
        }
    }
    
    public void writeToCsvFile(String line) {
        if (!isWritingStats)
            writer.println(line);
    }
    
    public void close() {
        writer.flush();
        writer.close();
    }
    
}
