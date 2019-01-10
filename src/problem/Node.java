/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author anei
 */
public class Node {
    
    public static final int UNSEEN = 0;
    public static final int SEEN = 1;
    public static final int DEADLOCK = 2;
    public static final int START = 3;
    public static final int END = 4;
    public static final int CURRENT = 5;
    public static final int VISITED = 6;
    
    public Node parent;
    public List<Node> childs;
    
    public State state;
    public String action;
    public int cost;
    public double bound;
    public int pnum;
    
    public int type;
    
    public Node(Node parent, State state, String action, int cost) {
        this.parent = parent;
        this.state = state;
        this.action = action;
        this.cost = cost;
        this.bound = 0;
        this.childs = null;
        this.type = UNSEEN;
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