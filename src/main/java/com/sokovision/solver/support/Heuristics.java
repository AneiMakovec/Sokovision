/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sokovision.solver.support;

import com.sokovision.grid.Position;
import java.util.Set;
import com.sokovision.problem.State;

/**
 *
 * @author anei
 */
public class Heuristics {
    private final Set<Position> goals;
    private final String type;
    private final double[][] costTable;
    private final HungarianAlgorithm h;
    
    
    public Heuristics(String type, Set<Position> goals) {
        this.type = type;
        this.goals = goals;
        this.costTable = new double[this.goals.size()][this.goals.size()];
        this.h = new HungarianAlgorithm(costTable.length);
    }
    
    
    public double getHeuristic(State state) {
        switch (this.type) {
            case "M":
                return calculate(state, this.type);
            case "E":
                return calculate(state, this.type);
            case "H":
                return calculateHungarian(state);
            case "G":
                //return calculateGreedy(state);
            default:
                break;
        }
        
        return 0;
    }
    
    
    
    private double calculate(State state, String type) {
        double sum = 0;
        
        // add to sum the minimum distance from worker to any box
        sum += getMinDist(state.getWorker(), state.getCrates(), type);
        
        // add to sum the minumum distance from crates to goals
        for (Position pos : state.getCrates()) {
            sum += getMinDist(pos, this.goals, type);
        }
        
        return sum;
    }
    
    private double getMinDist(Position pos, Set<Position> set, String type) {
        double minDist = 1000000;
        
        // calculate the minimum distance from pos to any position in the set
        double dist;
        for (Position p : set) {
            if (type.equals("M"))
                dist = getManhattan(pos, p);
            else
                dist = getEuclidean(pos, p);
            
            if (dist < minDist)
                minDist = dist;
        }
        
        return minDist;
    }
    
    private double getManhattan(Position p1, Position p2) {
        return Math.abs(p1.getY() - p2.getY()) + Math.abs(p1.getX() - p2.getX());
    }
    
    private double getEuclidean(Position p1, Position p2) {
        return Math.sqrt((double) ((p1.getY() - p2.getY()) * (p1.getY() - p2.getY()) + (p1.getX() - p2.getX()) * (p1.getX() - p2.getX())));
    }
    
    
    private double calculateHungarian(State state) {
        double[][] cost = new double[this.goals.size()][this.goals.size()];
        
        int i = 0;
        for (Position crate : state.getCrates()) {
            int j = 0;
            double workerCost = getManhattan(state.getWorker(), crate);
            for (Position goal : this.goals) {
                this.costTable[i][j] = getManhattan(crate, goal);
                this.costTable[i][j] += workerCost;
                cost[i][j] = getManhattan(crate, goal);
                cost[i][j] += workerCost;
                j++;
            }
            i++;
        }
        
        int[] result = h.execute(cost);
        
        double maxSum = 0;
        for (int k = 0; k < this.goals.size(); k++) {
            int goalColumn = result[k];
            
            if (goalColumn > -1)
                maxSum += costTable[k][goalColumn];
        }
        
        return maxSum;
    }
}
