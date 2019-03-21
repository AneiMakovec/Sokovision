/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sokovision.solver;

import com.sokovision.grid.Grid;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import com.sokovision.plugin.Solver;
import com.sokovision.problem.Node;
import com.sokovision.problem.Problem;
import com.sokovision.problem.StatCollector;
import com.sokovision.problem.State;
import com.sokovision.solver.support.Heuristics;

/**
 *
 * @author anei
 */
public class IDAStarSolver implements Solver {
    
    
    private Problem problem;
    private Node state;
    private Node treeState;
    private State startState;
    private StatCollector statCollector;
    private boolean solutionFound;
    
    private LinkedList<Node> stack;
    private Set<State> seenStates;
    private Set<State> closedStates;
    private LinkedList<Node> visitStates;
    private static Heuristics heuristics;
    private double bound;
    
    
    public IDAStarSolver(State firstState, Grid grid, String heuristicsType) {
        this.problem = new Problem(grid);
        this.state = null;
        this.treeState = new Node(null, firstState, "", 0);
        this.startState = firstState;
        this.statCollector = new StatCollector();
        this.solutionFound = false;
        heuristics = new Heuristics(heuristicsType, problem.getGoals());
    }
    
    

    @Override
    public Node getState() {
        return state;
    }

    @Override
    public Node getTreeState() {
        return treeState;
    }

    @Override
    public StatCollector getStats() {
        return statCollector;
    }

    @Override
    public boolean isSolutionFound() {
        return solutionFound;
    }

    @Override
    public boolean isStillSolving() {
        return !stack.isEmpty();
    }

    @Override
    public void initialize() {
        stack = new LinkedList<>();
        treeState.bound = heuristics.getHeuristic(treeState.state);
        stack.push(treeState);
        seenStates = new HashSet<>();
        seenStates.add(treeState.state);
        closedStates = new HashSet<>();
        visitStates = new LinkedList<>();
        bound = treeState.bound;
        nextState();
    }

    @Override
    public void nextState() {
        if (!solutionFound && !stack.isEmpty()) {
            long startTime = System.nanoTime();
            
            while (!stack.isEmpty()) {
                if (state != null)
                    state.type = Node.VISITED;
                
                state = stack.pop();
                state.type = Node.CURRENT;
                statCollector.setSolutionDepth(state.cost + 1);
                
                if (problem.isEnd(state.state)) {
                    solutionFound = true;
                    state.type = Node.END;
                    statCollector.parseSolution(state);
                    return;
                }

                if (state.bound <= bound) {
                    closedStates.add(state.state);
                    
                    state.childs = problem.getPossibleActions(state);
                    for (Node child : state.childs) {
                        statCollector.increaseExaminedMoves();
                        
                        if (problem.isDeadlock(child.state)) {
                            child.type = Node.DEADLOCK;
                            statCollector.increaseDeadlocks();
                            continue;
                        }
                        
                        if (closedStates.contains(child.state)) {
                            child.type = Node.SEEN;
                            statCollector.increaseStatesAlreadySeen();
                            continue;
                        }
                        
                        if (seenStates.contains(child.state)) {
                            child.type = Node.SEEN;
                            statCollector.increaseStatesAlreadySeen();
                            continue;
                        } else {
                            seenStates.add(child.state);
                        }
                        
                        child.bound = child.cost + heuristics.getHeuristic(state.state);
                        
                        stack.push(child);
                    }
                } else {
                    visitStates.push(state);
                }
            }
            
            if (visitStates.isEmpty()) {
                return;
            }
            
            double minBound = visitStates.getFirst().bound;
            for (Node node : visitStates) {
                if (node.bound < minBound)
                    minBound = node.bound;
            }
            
            bound = minBound;
            
            stack.addAll(visitStates);
            visitStates.clear();
            closedStates.clear();
            
            statCollector.increaseTime(System.nanoTime() - startTime);
            statCollector.setSolutionDepth(state.cost);
            statCollector.setStatesInFringe(stack.size());
        }
    }

    @Override
    public void prevState() {
        
    }

    @Override
    public void reset() {
        solutionFound = false;
        state = null;
        treeState = new Node(null, startState, "", 0);
        statCollector.reset();
        initialize();
    }

    @Override
    public void solve() {
        
    }

}
