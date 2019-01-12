/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support.writer;

import problem.State;
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
        
        if (!fileName.getName().endsWith(".txt"))
            return;
        
        try {
            this.writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        } catch (IOException e) {}
    }
    
    public boolean isEnabled() {
        return this.writer != null;
    }
    
    public void write(int width, int height, Grid grid, State state) {
        // preprocess the grid to find the leftmost wall
        Position pos;
        int leftmost = width;
        int x = 0, y;
        for (y = 0; y < height; y++) {
            pos = new Position(x, y);
            
            while (x < width && grid.isVoid(pos)) {
                x++;
                pos.setX(x);
            }
            
            if (x == width) {
                x = 0;
                continue;
            }
            
            if (grid.isWall(pos)) {
                if (x < leftmost) {
                    leftmost = x;
                    
                    if (leftmost == 0)
                        break;
                }
            }
            
            x = 0;
        }
        
        // preprocess the grid to find the rightmost wall
        int rightmost = 0;
        x = width - 1;
        for (y = 0; y < height; y++) {
            pos = new Position(x, y);
            
            while (x >= 0 && grid.isVoid(pos)) {
                x--;
                pos.setX(x);
            }
            
            if (x < 0) {
                x = width - 1;
                continue;
            }
            
            if (grid.isWall(pos)) {
                if (x > rightmost) {
                    rightmost = x;
                    
                    if (rightmost == width - 1)
                        break;
                }
            }
            
            x = width - 1;
        }
        
        // preprocess the grid to find the upper wall
        int upper = height;
        y = 0;
        for (x = 0; x < width; x++) {
            pos = new Position(x, y);
            
            while (y < height && grid.isVoid(pos)) {
                y++;
                pos.setY(y);
            }
            
            if (y == height) {
                y = 0;
                continue;
            }
            
            if (grid.isWall(pos)) {
                if (y < upper) {
                    upper = y;
                    
                    if (upper == 0)
                        break;
                }
            }
            
            y = 0;
        }
        
        // preprocess the grid to find the lower wall
        int lower = 0;
        y = height - 1;
        for (x = 0; x < width; x++) {
            pos = new Position(x, y);
            
            while (y >= 0 && grid.isVoid(pos)) {
                y--;
                pos.setY(y);
            }
            
            if (y < 0) {
                y = height - 1;
                continue;
            }
            
            if (grid.isWall(pos)) {
                if (y > lower) {
                    lower = y;
                    
                    if (lower == height - 1)
                        break;
                }
            }
            
            y = height - 1;
        }
        
        // build a string of characters rapresenting the tiles
        StringBuilder sb = new StringBuilder();
        
        for (int i = upper; i <= lower; i++) {
            for (int j = leftmost; j <= rightmost; j++) {
                pos = new Position(j, i);

                // check if wall
                if (grid.isWall(pos)) {
                    sb.append("#");
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
                // no tile here
                } else if (grid.isVoid(pos)) {
                    sb.append("o");
                }
            }
            
            sb.append("\n");
        }
        
        sb.deleteCharAt(sb.length() - 1);
        
        // write the string of tiles to file
        writer.print(sb.toString());
        writer.flush();
        writer.close();
    }
}
