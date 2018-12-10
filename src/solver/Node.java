/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solver;

import game.State;
import java.util.Objects;

/**
 *
 * @author anei
 */
public class Node {
    
    public Node parent;
    public State state;
    public String action;
    public int cost;
    public double bound;
    public int pnum;
    
    public Node(Node parent, State state, String action, int cost) {
        this.parent = parent;
        this.state = state;
        this.action = action;
        this.cost = cost;
        this.bound = 0;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        
        if (this.getClass() != o.getClass())
            return false;
        
        Node n = (Node) o;
        
        return this.state.equals(n.state);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.state);
        return hash;
    }
    
    @Override
    public String toString() {
        return Integer.toString(this.pnum);
    }
}