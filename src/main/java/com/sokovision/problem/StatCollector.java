/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sokovision.problem;

import java.util.ArrayList;

/**
 *
 * @author anei
 */
public class StatCollector {
    
    private long time;
    private int movesExamined;
    private int solutionDepth;
    private StringBuilder solution;
    private int deadlocksFound;
    private int statesInFringe;
    private int statesAlreadySeen;
    
    private ArrayList<Stats> timeStamp;
    
    public StatCollector() {
        this.time = 0;
        this.movesExamined = 0;
        this.solutionDepth = 0;
        this.solution = new StringBuilder();
        this.deadlocksFound = 0;
        this.statesInFringe = 0;
        this.statesAlreadySeen = 0;
        
        this.timeStamp = new ArrayList<>();
    }

    public double getTime() {
        return 0.000000001f * time;
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

    public int getStatesAlreadySeen() {
        return statesAlreadySeen;
    }

    public void setStatesAlreadySeen(int statesAlreadySeen) {
        this.statesAlreadySeen = statesAlreadySeen;
    }
    
    
    
    public void increaseTime(long time) {
        this.time += time;
    }
    
    public void decreaseTime(long time) {
        this.time -= time;
        
        if (this.time < 0)
            this.time = 0;
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
        String sol = solution.toString();
        solution.reverse();
        return sol;
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
    
    public void increaseStatesAlreadySeen() {
        this.statesAlreadySeen++;
    }
    
    public void decreaseStatesAlreadySeen() {
        this.statesAlreadySeen--;
    }

    
    
    
    public ArrayList<Stats> getTimeStamp() {
        return timeStamp;
    }
    
    
    
    
    
    public void makeTimeStamp() {
        timeStamp.add(new Stats(getTime(), movesExamined, solutionDepth, deadlocksFound, statesInFringe, statesAlreadySeen));
    }
    
    
    
    public void parseSolution(Node endNode) {
        solutionDepth = 0;
        Node pathNode = endNode;
        while (pathNode != null) {
            increaseSolutionDepth();
            addToSolution(pathNode.action);
            pathNode = pathNode.parent;
        }
    }
    
    
    public void reset() {
        this.time = 0;
        this.movesExamined = 0;
        this.solutionDepth = 0;
        this.solution = new StringBuilder();
        this.deadlocksFound = 0;
        this.statesInFringe = 0;
        this.statesAlreadySeen = 0;
    }
}
