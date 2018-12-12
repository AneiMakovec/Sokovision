/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import grid.Position;
import java.util.HashSet;
import java.util.Objects;

/**
 *
 * @author anei
 */
public class State {
    private Position worker;
    private final HashSet<Position> crates;
    
    
    public State() {
        this.crates = new HashSet<>();
    }
    

    public Position getWorker() {
        return worker;
    }

    public void setWorker(Position worker) {
        this.worker = worker;
    }
    
    public boolean isWorker(Position pos) {
        if (this.worker != null) {
            if (this.worker.equals(pos))
                return true;
            else
                return false;
        } else {
            return false;
        }
    }
    
    public void removeWorker() {
        this.worker = null;
    }
    
    
    public void addCrate(Position pos) {
        this.crates.add(pos);
    }
    
    public void removeCrate(Position pos) {
        this.crates.remove(pos);
    }
    
    public boolean hasCrate(Position pos) {
        return this.crates.contains(pos);
    }
    
    public HashSet<Position> getCrates() {
        return this.crates;
    }
    
    
    public State copy() {
        State state = new State();
        
        state.setWorker(new Position(this.worker.getX(), this.worker.getY()));
        
        this.crates.forEach((p) -> {
            state.addCrate(new Position(p.getX(), p.getY()));
        });
        
        return state;
    }
    
    public void clear() {
        this.crates.clear();
        this.worker = null;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        
        if (this.getClass() != o.getClass())
            return false;
        
        State s = (State) o;
        
        if (!this.worker.equals(s.getWorker()))
            return false;
        
        return this.crates.stream().noneMatch((p) -> (!s.hasCrate(p)));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.worker);
        hash = 13 * hash + Objects.hashCode(this.crates);
        return hash;
    }
}
