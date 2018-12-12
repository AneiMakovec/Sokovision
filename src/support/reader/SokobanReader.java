/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support.reader;

import game.State;
import grid.Grid;
import grid.Position;
import grid.Space;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author anei
 */
public class SokobanReader {
    
    private BufferedReader reader;
    private int width;
    private int height;
    
    public SokobanReader(File fileName) {
        this.width = 0;
        this.height = 0;
        this.reader = null;
        
        try {
            this.reader = new BufferedReader(new FileReader(fileName));
        } catch (IOException e) {}
    }
    
    public boolean isEnabled() {
        return this.reader != null;
    }
    
    
    
    public void read(Grid grid, State state) {
        // remove all tiles
        grid.clear();
        state.clear();
        
        try {
            String[] line;
            String s;
            int w = 0, h = 0;
            while ((s = reader.readLine()) != null) {
                line = s.split("");
                
                if (width == 0)
                    width = line.length;
                
                Position pos;
                for (String symbol : line) {
                    switch (symbol) {
                        case "#": // WALL
                            pos = new Position(w, h);
                            grid.setSpace(pos, new Space(Space.WALL));
                            break;
                        case " ": // FREE
                            pos = new Position(w, h);
                            grid.setSpace(pos, new Space(Space.FREE));
                            break;
                        case "$": // CRATE
                            pos = new Position(w, h);
                            grid.setSpace(pos, new Space(Space.FREE));
                            state.addCrate(pos);
                            break;
                        case ".": // GOAL
                            pos = new Position(w, h);
                            grid.setSpace(pos, new Space(Space.FREE));
                            grid.addGoal(pos);
                            break;
                        case "@": // WORKER
                            pos = new Position(w, h);
                            grid.setSpace(pos, new Space(Space.FREE));
                            state.setWorker(pos);
                            break;
                        case "+": // WORKER ON GOAL
                            pos = new Position(w, h);
                            grid.setSpace(pos, new Space(Space.FREE));
                            grid.addGoal(pos);
                            state.setWorker(pos);
                            break;
                        case "*": // CRATE ON GOAL
                            pos = new Position(w, h);
                            grid.setSpace(pos, new Space(Space.FREE));
                            grid.addGoal(pos);
                            state.addCrate(pos);
                            break;
                        default:
                            break;
                    }
                    
                    w++;
                }
                
                w = 0;
                h++;
                height++;
            }
        } catch (IOException e) {
            width = 0;
            height = 0;
        }
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
}
