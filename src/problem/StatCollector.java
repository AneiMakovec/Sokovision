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
public class StatCollector {
    
    private long time;
    private int movesExamined;
    private int solutionDepth;
    private final StringBuilder solution;
    private int deadlocksFound;
    private int statesInFringe;
    
    public StatCollector() {
        this.movesExamined = 0;
        this.solutionDepth = 0;
        this.solution = new StringBuilder();
        this.deadlocksFound = 0;
        this.statesInFringe = 0;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
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

    public int getStatesInFringe() {
        return statesInFringe;
    }

    public void setStatesInFringe(int statesInFringe) {
        this.statesInFringe = statesInFringe;
    }
    
    
    
    public void increaseExaminedMoves() {
        this.movesExamined++;
    }
    
    public void decreaseExaminedMoves() {
        this.movesExamined--;
    }
    
    public void increaseSolutionDepth() {
        this.solutionDepth++;
    }
    
    public void decreaseSolutionDepth() {
        this.solutionDepth--;
    }
    
    public void addToSolution(String action) {
        this.solution.append(action);
    }
    
    public String getSolution() {
        solution.reverse();
        return solution.toString();
    }
    
    public int getSolutionLength() {
        return solution.length();
    }

    public int getDeadlocksFound() {
        return deadlocksFound;
    }

    public void increaseDeadlocks() {
        this.deadlocksFound++;
    }
    
    public void decreaseDeadlocks() {
        this.deadlocksFound--;
    }
    
    
    
    public void parseSolution(Node endNode) {
        Node pathNode = endNode;
        while (pathNode != null) {
            increaseSolutionDepth();
            addToSolution(pathNode.action);
            pathNode = pathNode.parent;
        }
    }
}
