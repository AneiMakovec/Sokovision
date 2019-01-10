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
    private Node startState;
    private StatCollector statCollector;
    
    
    private Queue<Node> queue;
    private Set<State> seenStates;
    
    public BreadthFirstSolver(State firstState, Grid grid) {
        this.problem = new Problem(grid);
        this.state = null;
        this.treeState = new Node(null, firstState, "", 0);
        this.startState = new Node(null, firstState, "", 0);
        this.statCollector = new StatCollector();
    }
    
    public Node getState() {
        return state;
    }
    
    public Node getTreeState() {
        return treeState;
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
        if (!queue.isEmpty()) {
            if (state != null)
                state.type = Node.VISITED;
            
            state = queue.poll();
            seenStates.add(state.state);
            state.type = Node.CURRENT;
            
            state.childs = problem.getPossibleActions(state);
            for (Node nextNode : state.childs) {
                statCollector.increaseExaminedMoves();

                if (!seenStates.contains(nextNode.state) && !queue.contains(nextNode)) {
                    if (problem.isEnd(nextNode.state)) {
                        statCollector.parseSolution(nextNode);
                        statCollector.setStatesInFringe(queue.size());
                        //statCollector.setTime(System.currentTimeMillis() - startTime);
                        return;
                    }
                    
                    if (!problem.isDeadlock(nextNode.state)) {
                        queue.add(nextNode);
                    } else {
                        nextNode.type = Node.DEADLOCK;
                        statCollector.increaseDeadlocks();
                    }
                } else {
                    nextNode.type = Node.SEEN;
                }
            }
            
            //state.type = Node.UNSEEN;
        }
    }
    
    @Override
    public void prevState() {
        
    }
    
    @Override
    public void start() {
        
    }
    
    @Override
    public void stop() {
        
    }
    
    @Override
    public void pause() {
        
    }
    
    @Override
    public void resume() {
        
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
