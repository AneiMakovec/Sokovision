/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sokovision.solver;

import com.sokovision.grid.Grid;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;
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
public class AStarSolver implements Solver {
    
    private Problem problem;
    private Node state;
    private Node treeState;
    private State startState;
    private StatCollector statCollector;
    private boolean solutionFound;
    
    private PriorityQueue<Node> queue;
    private Set<State> seenStates;
    private static Heuristics heuristics;
    
    private Stack<Node> previousStates;
    
    
    public static Comparator<Node> heurComparator = new Comparator<Node>() {
        @Override
        public int compare(Node n1, Node n2) {
            return (int) ((n1.cost + heuristics.getHeuristic(n1.state)) - (n2.cost + heuristics.getHeuristic(n2.state)));
        }
    };
    
    
    public AStarSolver(State firstState, Grid grid, String heuristicsType) {
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
        return !queue.isEmpty();
    }

    @Override
    public void initialize() {
        queue = new  PriorityQueue<>(11, heurComparator);
        queue.add(treeState);
        seenStates = new HashSet<>();
        previousStates = new Stack<>();
        nextState();
    }

    @Override
    public void nextState() {
        if (!solutionFound && !queue.isEmpty()) {
            long startTime = System.nanoTime();
            
            if (state != null) {
                state.type = Node.VISITED;
                previousStates.push(state);
            }
            
            state = queue.remove();
            seenStates.add(state.state);
            state.type = Node.CURRENT;
            
            statCollector.setSolutionDepth(state.cost + 1);
            
            state.childs = problem.getPossibleActions(state);
            for (Node nextNode : state.childs) {
                statCollector.increaseExaminedMoves();

                if (!seenStates.contains(nextNode.state) && !queue.contains(nextNode)) {
                    if (problem.isEnd(nextNode.state)) {
                        state.type = Node.VISITED;
                        state = nextNode;
                        state.type = Node.CURRENT;
                        solutionFound = true;
                        statCollector.parseSolution(nextNode);
                        break;
                    }
                    
                    if (!problem.isDeadlock(nextNode.state)) {
                        queue.add(nextNode);
                    } else {
                        nextNode.type = Node.DEADLOCK;
                        statCollector.increaseDeadlocks();
                    }
                } else {
                    nextNode.type = Node.SEEN;
                    statCollector.increaseStatesAlreadySeen();
                    
                    // check if priority queue contains current state and change the cost if lower
                    for (Node temp : queue) {
                        if (temp.equals(nextNode))
                            if (nextNode.cost < temp.cost)
                                temp = nextNode;
                    }
                }
            }
            
            statCollector.increaseTime(System.nanoTime() - startTime);
            statCollector.setStatesInFringe(queue.size());
            
            if (state.parent == null)
                state.type = Node.VISITED;
        }
    }

    @Override
    public void prevState() {
        if (!previousStates.isEmpty() && !solutionFound && !queue.isEmpty()) {
            // remove all children's children and reset stat collector
            for (Node child : state.childs) {
                queue.remove(child);
                child.childs = null;
                
                if (child.type == Node.DEADLOCK)
                    statCollector.decreaseDeadlocks();
                else if (child.type == Node.SEEN)
                    statCollector.decreaseStatesAlreadySeen();
                
                statCollector.decreaseExaminedMoves();
            }
            
            // remove all children and add state back to fringe
            state.childs = null;
            state.type = Node.UNSEEN;
            seenStates.remove(state.state);
            queue.add(state);
            
            // load previous state
            state = previousStates.pop();
            state.type = Node.CURRENT;
            
            statCollector.setSolutionDepth(state.cost + 1);
            statCollector.setStatesInFringe(queue.size());
        }
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
