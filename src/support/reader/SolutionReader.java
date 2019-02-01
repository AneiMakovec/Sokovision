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

/**
 *
 * @author anei
 */
public class SolutionReader {
    
    private BufferedReader reader;
    
    public SolutionReader(File solutionFile) {
        this.reader = null;
        
        try {
            this.reader = new BufferedReader(new FileReader(solutionFile));
        } catch (IOException e) {}
    }
    
    public boolean isEnabled() {
        return this.reader != null;
    }
    
    
    public String readLine() {
        if (reader != null) {
            try {
                return reader.readLine();
            } catch (IOException e) {}
        }
        
        return null;
    }
    
    public void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {}
        }
    }
}
