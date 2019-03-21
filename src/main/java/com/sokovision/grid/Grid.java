/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sokovision.grid;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Implements class StaticData.
 * @author anei
 */
public class Grid {
    private final HashMap<Position, Space> grid;
    private final HashSet<Position> goals;
    
    public Grid() {
        this.grid = new HashMap<>();
        this.goals = new HashSet<>();
    }
    
    /*
        Grid methods
    */
    public void setSpace(Position pos, Space s) {
        this.grid.put(pos, s);
    }
    
    public void removeSpace(Position pos) {
        this.grid.remove(pos);
    }
    
    public Space getSpace(Position pos) {
        return this.grid.get(pos);
    }
    
    public void replaceSpace(Position pos, Space s) {
        this.grid.replace(pos, s);
    }
    
    public Set<Position> getPositions() {
        return this.grid.keySet();
    }
    
    public boolean isWall(Position pos) {
        if (this.grid.containsKey(pos))
            return this.grid.get(pos).type == Space.WALL;
        else
            return false;
    }
    
    public boolean existsWall(Position pos) {
        if (this.grid.containsKey(pos))
            return this.grid.get(pos).type == Space.WALL;
        else
            return true;
    }
    
    public boolean isFree(Position pos) {
        if (this.grid.containsKey(pos))
            return this.grid.get(pos).type == Space.FREE;
        else
            return false;
    }
    
    public boolean isVoid(Position pos) {
        return !grid.containsKey(pos);
    }
    
    
    /*
        Goals methods
    */
    public void addGoal(Position pos) {
        this.goals.add(pos);
    }
    
    public void removeGoal(Position pos) {
        this.goals.remove(pos);
    }
    
    public boolean isGoal(Position pos) {
        return this.goals.contains(pos);
    }
    
    public Set<Position> getGoals() {
        return this.goals;
    }
    
    
    /*
        Class methods
    */
    public void clear() {
        this.grid.clear();
        this.goals.clear();
    }
}
