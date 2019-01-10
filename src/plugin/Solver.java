/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plugin;

import problem.Node;
import problem.StatCollector;

/**
 *
 * @author anei
 */
public interface Solver {
    
    public Node getState();
    
    public Node getTreeState();
    
    public StatCollector getStats();
    
    public boolean isSolutionFound();
    
    public boolean isStillSolving();
    
    public void initialize();
    
    public void nextState();
    
    public void prevState();
    
    public void reset();
      
    public void solve();
}
