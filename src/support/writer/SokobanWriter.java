/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support.writer;

import game.State;
import grid.Grid;
import grid.Position;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author anei
 */
public class SokobanWriter {
    
    private PrintWriter writer;
    
    public SokobanWriter(File fileName) {
        this.writer = null;
        
        try {
            this.writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        } catch (IOException e) {}
    }
    
    public boolean isEnabled() {
        return this.writer != null;
    }
    
    public void write(int width, int height, Grid grid, State state) {
        // build a string of characters rapresenting the tiles
        StringBuilder sb = new StringBuilder();
        Position pos;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pos = new Position(j, i);

                // check if wall
                if (grid.isWall(pos)) {
                    // check if wall is necesarry
                    if (grid.existsWall(pos.getUp()) && grid.existsWall(pos.getRight()) && grid.existsWall(pos.getDown()) && grid.existsWall(pos.getLeft())) {
                        sb.append(" ");
                    } else {
                        sb.append("#");
                    }
                } else if (grid.isFree(pos)) {
                    // check if goal
                    if (grid.isGoal(pos)) {
                        // if crate on goal
                        if (state.hasCrate(pos)) {
                            sb.append("*");
                        // if worker on goal
                        } else if (state.isWorker(pos)) {
                            sb.append("+");
                        // if empty goal
                        } else {
                            sb.append(".");
                        }
                    // check if crate
                    } else if (state.hasCrate(pos)) {
                        sb.append("$");
                    // check if worker
                    } else if (state.isWorker(pos)) {
                        sb.append("@");
                    // empty
                    } else {
                        sb.append(" ");
                    }
                }
            }
            
            sb.append("\n");
        }
        
        // write the string of tiles to file
        writer.print(sb.toString());
        writer.flush();
        writer.close();
    }
}
