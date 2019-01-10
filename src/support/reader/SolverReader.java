/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import support.writer.SolverWriter;

/**
 *
 * @author anei
 */
public class SolverReader {
    
    private BufferedReader reader;
    private int solverType;
    private int heurType;
    
    public SolverReader(File fileName) {
        this.reader = null;
        this.solverType = SolverWriter.BFS;
        this.heurType = -1;
        
        try {
            this.reader = new BufferedReader(new FileReader(fileName));
        } catch (IOException e) {}
    }
    
    public boolean isEnabled() {
        return this.reader != null;
    }
    
    public int getSolverType() {
        return solverType;
    }
    
    public int getHeurType() {
        return heurType;
    }
    
    public void read() {
        try {
            solverType = Integer.parseInt(reader.readLine());
            heurType = Integer.parseInt(reader.readLine());
            reader.close();
        } catch (IOException e) {}
    }
}
