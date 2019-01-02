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
    public static int NEW_FILE = 6;
    public static int NEW_PROJECT = 7;
    public static int NEW_SOLVER = 8;
    public static int NEXT_STATE = 9;
    public static int PREV_STATE = 10;
    public static int PAUSE = 11;
    public static int RESUME = 12;
    public static int START = 13;
    public static int STOP = 14;
    public static int PROJECT_FILE = 15;
    public static int PROBLEM_FILE = 16;
    public static int SOLVER_FILE = 17;
    public static int STAT_FILE = 18;
    public static int CSV_FILE = 19;
    
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
            BufferedImage newFileImage = ImageIO.read(new File(System.getProperty("user.dir") + "/resources/new_file.png"));
            BufferedImage newProjectImage = ImageIO.read(new File(System.getProperty("user.dir") + "/resources/new_project.png"));
            BufferedImage newSolverImage = ImageIO.read(new File(System.getProperty("user.dir") + "/resources/new_solver.png"));
            BufferedImage nextStateImage = ImageIO.read(new File(System.getProperty("user.dir") + "/resources/next_state.png"));
            BufferedImage prevStateImage = ImageIO.read(new File(System.getProperty("user.dir") + "/resources/prev_state.png"));
            BufferedImage pauseImage = ImageIO.read(new File(System.getProperty("user.dir") + "/resources/pause.png"));
            BufferedImage resumeImage = ImageIO.read(new File(System.getProperty("user.dir") + "/resources/resume.png"));
            BufferedImage startImage = ImageIO.read(new File(System.getProperty("user.dir") + "/resources/start.png"));
            BufferedImage stopImage = ImageIO.read(new File(System.getProperty("user.dir") + "/resources/stop.png"));
            BufferedImage projectFileImage = ImageIO.read(new File(System.getProperty("user.dir") + "/resources/project.png"));
            BufferedImage problemFileImage = ImageIO.read(new File(System.getProperty("user.dir") + "/resources/problem_file.png"));
            BufferedImage solverFileImage = ImageIO.read(new File(System.getProperty("user.dir") + "/resources/solver_file.png"));
            BufferedImage statFileImage = ImageIO.read(new File(System.getProperty("user.dir") + "/resources/stat_file.png"));
            BufferedImage csvFileImage = ImageIO.read(new File(System.getProperty("user.dir") + "/resources/csv_file.png"));
            
            // create mappings for images
            map.put(WALL, wallImage);
            map.put(FREE, floorImage);
            map.put(GOAL, goalImage);
            map.put(WORKER, workerImage);
            map.put(CRATE, crateOnFloorImage);
            map.put(CRATE_ON_GOAL, crateOnGoalImage);
            map.put(NEW_FILE, newFileImage);
            map.put(NEW_PROJECT, newProjectImage);
            map.put(NEW_SOLVER, newSolverImage);
            map.put(NEXT_STATE, nextStateImage);
            map.put(PREV_STATE, prevStateImage);
            map.put(PAUSE, pauseImage);
            map.put(RESUME, resumeImage);
            map.put(START, startImage);
            map.put(STOP, stopImage);
            map.put(PROJECT_FILE, projectFileImage);
            map.put(PROBLEM_FILE, problemFileImage);
            map.put(SOLVER_FILE, solverFileImage);
            map.put(STAT_FILE, statFileImage);
            map.put(CSV_FILE, csvFileImage);
        } catch (IOException e) {
            System.err.println("FATAL ERROR: ImagePacker@constructor -> Could not find and load tile images.");
            System.exit(1);
        }
    }
    
    public BufferedImage getImage(int id) {
        return map.get(id);
    }
}
