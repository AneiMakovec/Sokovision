/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solver;

import grid.Grid;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import plugin.Solver;
import problem.Node;
import problem.Problem;
import problem.StatCollector;
import problem.State;

/**
 *
 * @author anei
 */
public class DepthFirstSolver implements Solver {
    
    private Problem problem;
    private Node state;
    private Node treeState;
    private State startState;
    private StatCollector statCollector;
    
    private boolean solutionFound;
    
    private Stack<Node> stack;
    private Set<State> seenStates;
    
    
    
    public DepthFirstSolver(State firstState, Grid grid) {
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
        return !stack.isEmpty();
    }

    @Override
    public void initialize() {
        stack = new Stack<>();
        stack.push(treeState);
        seenStates = new HashSet<>();
        nextState();
    }

    @Override
    public void nextState() {
        if (!solutionFound && !stack.isEmpty()) {
            long startTime = System.nanoTime();
            
            if (state != null)
                state.type = Node.VISITED;
            
            state = stack.pop();
            seenStates.add(state.state);
            state.type = Node.CURRENT;
            
            statCollector.setSolutionDepth(state.cost + 1);
            
            state.childs = problem.getPossibleActions(state);
            for (Node nextNode : state.childs) {
                statCollector.increaseExaminedMoves();

                if (!seenStates.contains(nextNode.state) && !stack.contains(nextNode)) {
                    if (problem.isEnd(nextNode.state)) {
                        state.type = Node.VISITED;
                        state = nextNode;
                        state.type = Node.CURRENT;
                        solutionFound = true;
                        statCollector.parseSolution(nextNode);
                        break;
                    }
                    
                    if (!problem.isDeadlock(nextNode.state)) {
                        stack.push(nextNode);
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
