/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.support;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 *
 * @author anei
 */
public class ImagePacker {
    
    private final HashMap<Integer, BufferedImage> map;
    
    public static int WALL = 0;
    public static int FREE = 1;
    public static int GOAL = 2;
    public static int WORKER = 3;
    public static int CRATE = 4;
    public static int CRATE_ON_GOAL = 5;
    
    public ImagePacker() {
        map = new HashMap<>();
        
        try {
            // load images
            BufferedImage wallImage = ImageIO.read(new File(System.getProperty("user.dir") + "/resources/wall.png"));
            BufferedImage floorImage = ImageIO.read(new File(System.getProperty("user.dir") + "/resources/floor.png"));
            BufferedImage goalImage = ImageIO.read(new File(System.getProperty("user.dir") + "/resources/goal.png"));
            BufferedImage workerImage = ImageIO.read(new File(System.getProperty("user.dir") + "/resources/worker.png"));
            BufferedImage crateOnFloorImage = ImageIO.read(new File(System.getProperty("user.dir") + "/resources/crate.png"));
            BufferedImage crateOnGoalImage = ImageIO.read(new File(System.getProperty("user.dir") + "/resources/crate_on_goal.png"));
            
            // create mappings for images
            map.put(WALL, wallImage);
            map.put(FREE, floorImage);
            map.put(GOAL, goalImage);
            map.put(WORKER, workerImage);
            map.put(CRATE, crateOnFloorImage);
            map.put(CRATE_ON_GOAL, crateOnGoalImage);
        } catch (IOException e) {
            System.err.println("FATAL ERROR: ImagePacker@constructor -> Could not find and load tile images.");
            System.exit(1);
        }
    }
    
    public BufferedImage getImage(int id) {
        return map.get(id);
    }
}
