/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.awt.Graphics;
import javax.swing.JPanel;
import support.Coordinates;

/**
 *
 * @author anei
 */
public class DisplayImagePanel extends JPanel {
    
    private final ImagePacker packer;
    private final int imageSize = 32;
    
    /**
     * Creates new instance of class DisplayImagePanel.
     * @param packer the image packer to connect to the display panel
     */
    public DisplayImagePanel(ImagePacker packer) {
        this.packer = packer;
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        // call parent method
        super.paintComponent(g);
        
        /*
        
        // draw walls
        for (Coordinates c : packer.getWallCoordinates()) {
            g.drawImage(packer.getWallImage(), c.getX(), c.getY(), imageSize, imageSize, this);
        }
        
        // draw floor
        for (Coordinates c : packer.getFloorCoordinates()) {
            g.drawImage(packer.getFloorImage(), c.getX(), c.getY(), imageSize, imageSize, this);
        }
        
        // draw goals
        for (Coordinates c : packer.getGoalCoordinates()) {
            g.drawImage(packer.getGoalImage(), c.getX(), c.getY(), imageSize, imageSize, this);
        }
        
        // draw worker on floor
        for (Coordinates c : packer.getWorkerOnFloorCoordinates()) {
            g.drawImage(packer.getWorkerOnFloorImage(), c.getX(), c.getY(), imageSize, imageSize, this);
        }
        
        // draw worker on goal
        for (Coordinates c : packer.getWorkerOnGoalCoordinates()) {
            g.drawImage(packer.getWorkerOnGoalImage(), c.getX(), c.getY(), imageSize, imageSize, this);
        }
        
        // draw crates on floor
        for (Coordinates c : packer.getCrateOnFloorCoordinates()) {
            g.drawImage(packer.getCrateOnFloorImage(), c.getX(), c.getY(), imageSize, imageSize, this);
        }
        
        // draw crates on goals
        for (Coordinates c : packer.getCrateOnGoalCoordinates()) {
            g.drawImage(packer.getCrateOnGoalImage(), c.getX(), c.getY(), imageSize, imageSize, this);
        }
        */
    }
}
