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

/**
 *
 * @author anei
 */
public class SolverWriter {
    
    private PrintWriter writer;
    
    public SolverWriter(File fileName) {
        this.writer = null;
        
        try {
            this.writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        } catch (IOException e) {}
    }
    
    public boolean isEnabled() {
        return this.writer != null;
    }
    
    public void write(String solverName) {
        
    }
}
