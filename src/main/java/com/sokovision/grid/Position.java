/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sokovision.grid;

import java.util.ArrayList;

/**
 *
 * @author anei
 */
public class Position {
    
    private int x;
    private int y;
    
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    
    
    public Position getUp() {
        return new Position(this.x, this.y - 1);
    }
    
    public Position getDown() {
        return new Position(this.x, this.y + 1);
    }
    
    public Position getLeft() {
        return new Position(this.x - 1, this.y);
    }
    
    public Position getRight() {
        return new Position(this.x + 1, this.y);
    }
    
    public ArrayList<Position> getAllDirections() {
        ArrayList<Position> directions = new ArrayList<>();
        directions.add(this.getUp());
        directions.add(this.getRight());
        directions.add(this.getDown());
        directions.add(this.getLeft());
        return directions;
    }
    
    
    
    
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        
        if (o.getClass() != this.getClass()) {
            return false;
        }
        
        Position pos = (Position) o;
        
        return this.x == pos.x && this.y == pos.y;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.x;
        hash = 79 * hash + this.y;
        return hash;
    }
    
    @Override
    public String toString() {
        return String.format("x: %d, y: %d", this.x, this.y);
    }
}
