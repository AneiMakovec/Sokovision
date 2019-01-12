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
    
    public static int BFS = 0;
    public static int DFS = 1;
    public static int ASTAR = 2;
    public static int IDASTAR = 3;
    
    public static int HEUR_MANHATTAN = 4;
    public static int HEUR_EUCLIDEAN = 5;
    public static int HEUR_HUNGARIAN = 6;
    
    private PrintWriter writer;
    
    public SolverWriter(File fileName) {
        this.writer = null;
        
        if (!fileName.getName().endsWith(".slvr"))
            return;
        
        try {
            this.writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        } catch (IOException e) {}
    }
    
    public boolean isEnabled() {
        return this.writer != null;
    }
    
    public void write(String solverName, String heurType) {
        int solverBuffer = -1;
        switch (solverName) {
            case "Breadth-first search":
                solverBuffer = BFS;
                break;
            case "Depth-first search":
                solverBuffer = DFS;
                break;
            case "A* search":
                solverBuffer = ASTAR;
                break;
            case "Iterative deepening A* search":
                solverBuffer = IDASTAR;
                break;
            
            default:
                break;
        }
        
        int heurBuffer = -1;
        switch (heurType) {
            case "Manhattan":
                heurBuffer = HEUR_MANHATTAN;
                break;
            case "Euclidean":
                heurBuffer = HEUR_EUCLIDEAN;
                break;
            case "Hungarian":
                heurBuffer = HEUR_HUNGARIAN;
                break;
            
            default:
                break;
        }
        
        writer.println(solverBuffer);
        writer.println(heurBuffer);
        writer.flush();
        writer.close();
    }
}
