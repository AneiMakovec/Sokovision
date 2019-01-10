/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solver;

import grid.Grid;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import plugin.Solver;
import problem.Node;
import problem.Problem;
import problem.StatCollector;
import problem.State;

/**
 *
 * @author anei
 */
public class BreadthFirstSolver implements Solver {
    
    private Problem problem;
    private Node state;
    private Node treeState;
    private State startState;
    private StatCollector statCollector;
    
    private boolean solutionFound;
    
    private Queue<Node> queue;
    private Set<State> seenStates;
    
    public BreadthFirstSolver(State firstState, Grid grid) {
        this.problem = new Problem(grid);
        this.state = null;
        this.treeState = new Node(null, firstState, "", 0);
        this.startState = firstState;
        this.statCollector = new StatCollector();
        this.solutionFound = false;
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
        queue = new LinkedList<>();
        queue.add(treeState);
        seenStates = new HashSet<>();
        nextState();
    }
    
    @Override
    public void nextState() {
        if (!solutionFound && !queue.isEmpty()) {
            long startTime = System.nanoTime();
            
            if (state != null)
                state.type = Node.VISITED;
            
            state = queue.poll();
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
                }
            }
            
            statCollector.increaseTime(System.nanoTime() - startTime);
            statCollector.setStatesInFringe(queue.size());
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
//        long startTime = System.currentTimeMillis();
//        
//        Queue<Node> queue = new LinkedList<>();
//        queue.add(state);
//        Set<State> seenStates = new HashSet<>();
//
//        do {
//            state = queue.poll();
//            seenStates.add(state.state);
//            
//            for (Node nextNode : problem.getPossibleActions(state)) {
//                statCollector.increaseExaminedMoves();
//
//                if (!seenStates.contains(nextNode.state) && !queue.contains(nextNode)) {
//                    if (problem.isEnd(nextNode.state)) {
//                        statCollector.parseSolution(nextNode);
//                        statCollector.setStatesInFringe(queue.size());
//                        statCollector.setTime(System.currentTimeMillis() - startTime);
//                        return;
//                    }
//                    
//                    if (!problem.isDeadlock(nextNode.state)) {
//                        queue.add(nextNode);
//                    } else {
//                        statCollector.increaseDeadlocks();
//                    }
//                }
//            }
//        } while (!queue.isEmpty());
//        
//        statCollector.setStatesInFringe(queue.size());
//        statCollector.setTime(System.currentTimeMillis() - startTime);
    }
    
}
