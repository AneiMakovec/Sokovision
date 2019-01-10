/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solver;

import grid.Grid;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import plugin.Solver;
import problem.Node;
import problem.Problem;
import problem.StatCollector;
import problem.State;
import static solver.AStarSolver.heurComparator;
import solver.support.Heuristics;

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
    
    private Stack<Node> stack;
    private Set<State> seenStates;
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
        stack = new Stack<>();
        stack.push(treeState);
        seenStates = new HashSet<>();
        bound = heuristics.getHeuristic(treeState.state);
        nextState();
    }

    @Override
    public void nextState() {
        if (!solutionFound && !stack.isEmpty()) {
            long startTime = System.nanoTime();
            
            if (state != null)
                state.type = Node.VISITED;
            
            state = searchDFS(0, bound);
            
            if (state.bound == -1) {
                state.type = Node.CURRENT;
                solutionFound = true;
                statCollector.parseSolution(state);
                return;
            }
            
            if (state.bound == Integer.MAX_VALUE) {
                return;
            }
            
            bound = state.bound;
            
            statCollector.increaseTime(System.nanoTime() - startTime);
            statCollector.setStatesInFringe(stack.size());
        }
    }
    
    private Node searchDFS(double g, double bound) {
        Node node = stack.peek();
        
        double f = g + heuristics.getHeuristic(node.state);
        
        if (f > bound) {
            node.bound = f;
            return node;
        }
        
        if (problem.isEnd(node.state)) {
            node.bound = -1;
            return node;
        }
        
        Node minNode = new Node(null, null, "", 0);
        minNode.bound = Integer.MAX_VALUE;
        
        if (!problem.isDeadlock(node.state)) {
            Node resultNode;
            node.childs = problem.getPossibleActions(node);
            for (Node n : node.childs) {
                statCollector.increaseExaminedMoves();
                
                if (!seenStates.contains(n.state) && !stack.contains(n)) {
                    stack.push(n);
                    resultNode = searchDFS(g + 1, bound);

                    if (resultNode.bound == -1)
                        return resultNode;

                    if (resultNode.bound < minNode.bound) {
                        //minNode = new Node(resultNode.parent, resultNode.state, resultNode.action, resultNode.cost);
                        //minNode.bound = resultNode.bound;
                        minNode = resultNode;
                    }

                    stack.pop();
                } else {
                    n.type = Node.SEEN;
                    statCollector.increaseStatesAlreadySeen();
                }
            }
        } else {
            statCollector.increaseDeadlocks();
            node.type = Node.DEADLOCK;
        }
        
        return minNode;
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
