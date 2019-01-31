/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem;

/**
 *
 * @author anei
 */
public class Stats {
    
    private double time;
    private int movesExamined;
    private int solutionDepth;
    private int deadlocksFound;
    private int statesInFringe;
    private int statesAlreadySeen;
    
    
    public Stats(double time, int movesExamined, int solutionDepth, int deadlocksFound, int statesInFringe, int statesAlreadySeen) {
        this.time = time;
        this.movesExamined = movesExamined;
        this.solutionDepth = solutionDepth;
        this.deadlocksFound = deadlocksFound;
        this.statesInFringe = statesInFringe;
        this.statesAlreadySeen = statesAlreadySeen;
    }
    

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getMovesExamined() {
        return movesExamined;
    }

    public void setMovesExamined(int movesExamined) {
        this.movesExamined = movesExamined;
    }

    public int getSolutionDepth() {
        return solutionDepth;
    }

    public void setSolutionDepth(int solutionDepth) {
        this.solutionDepth = solutionDepth;
    }

    public int getDeadlocksFound() {
        return deadlocksFound;
    }

    public void setDeadlocksFound(int deadlocksFound) {
        this.deadlocksFound = deadlocksFound;
    }

    public int getStatesInFringe() {
        return statesInFringe;
    }

    public void setStatesInFringe(int statesInFringe) {
        this.statesInFringe = statesInFringe;
    }

    public int getStatesAlreadySeen() {
        return statesAlreadySeen;
    }

    public void setStatesAlreadySeen(int statesAlreadySeen) {
        this.statesAlreadySeen = statesAlreadySeen;
    }
}
