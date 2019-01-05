/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plugin;

/**
 *
 * @author anei
 */
public interface Solver {
    
    public void initialize();
    
    public void start();
    
    public void stop();
    
    public void pause();
    
    public void resume();
    
    public void nextState();
    
    public void prevState();
    
    public void solve();
}
