/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grid;

/**
 *
 * @author anei
 */
public class Space {
    
    public static final int WALL = 0;
    public static final int FREE = 1;
    //public static final int EMPTY_GOAL = 1;
    //public static final int WORKER_ON_FREE = 2;
    //public static final int WORKER_ON_GOAL = 3;
    //public static final int CRATE_ON_FREE = 4;
    //public static final int CRATE_ON_GOAL =5;
    
    public int type;
    public boolean isDead;
    
    
    public Space(int type) {
        this.type = type;
        this.isDead = false;
    }
    
    @Override
    public String toString() {
        String s;
        
        switch(this.type) {
            case Space.WALL:
                s = "WALL";
                break;
            case Space.FREE:
                s = "FREE";
                break;
            /*
            case Space.EMPTY_GOAL:
                s = "EMPTY_GOAL";
                break;
            case Space.WORKER_ON_FREE:
                s = "WORKER_ON_FREE";
                break;
            case Space.WORKER_ON_GOAL:
                s = "WORKER_ON_GOAL";
                break;
            case Space.CRATE_ON_FREE:
                s = "CRATE_ON_FREE";
                break;
            case Space.CRATE_ON_GOAL:
                s = "CRATE_ON_GOAL";
                break;
            */
            default:
                s = "NULL";
                break;
        }
        
        if (this.isDead) {
            s = s + " and dead";
        }
        
        return s;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        
        if (o.getClass() != this.getClass())
            return false;
        
        Space s = (Space) o;
        
        return s.type == this.type && s.isDead == this.isDead;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.type;
        hash = 71 * hash + (this.isDead ? 1 : 0);
        return hash;
    }
}
