/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.editor;

import graphics.support.ImagePacker;
import graphics.support.GridRectangle;
import grid.Grid;
import grid.Position;
import grid.Space;
import problem.State;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import main.MainFrame;
import support.reader.SokobanReader;
import support.writer.SokobanWriter;

/**
 *
 * @author anei
 */
public class EditProblemPanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener, ChangeListener {
    
    // rectangle in which the grid is displayed
    private final GridRectangle gridRectangle;
    
    // the width and height of the grid in tiles
    private int gridWidth;
    private int gridHeight;
    
    // zoom of the grid
    private int zoom = 10;
    private JSlider zoomSlider;
    
    // default tileSize of a grid tile
    private final int DEFAULT_TILE_SIZE = 5;
    
    // class to store grid tile values and goal locations
    Grid grid;
    
    // class to store worker and crate positions
    State state;
    
    // pointer to image repository
    ImagePacker packer;
    
    // type of current tile
    private int currentSpace;
    
    // mouse coordinates during dragging
    private int mouseX, mouseY;
    
    // file from which the grid was loaded
    private final File file;
    
    // tells if the current file was edited
    private boolean edited;
    
    // index of tab
    private int tabIndex;
    
    // pointer to main frame
    private MainFrame parent;
    
    // toolbar buttons click values
    static final private String WALL = "wall";
    static final private String FREE = "free";
    static final private String CRATE = "crate";
    static final private String WORKER = "worker";
    static final private String GOAL = "goal";
    
    
    public EditProblemPanel(int width, int height, ImagePacker packer) {
        super(new BorderLayout());
        this.gridRectangle = new GridRectangle(0, 0, 0, 0);
        this.grid = new Grid();
        this.state = new State();
        this.mouseX = 0;
        this.mouseY = 0;
        this.packer = packer;
        this.currentSpace = -1;
        this.file = null;
        this.edited = false;
        initToolbar();
        initListeners();
        setParameters(width, height);
    }
    
    public EditProblemPanel(MainFrame parent, File problemFile, int tabIndex, ImagePacker packer) {
        super(new BorderLayout());
        this.gridRectangle = new GridRectangle(0, 0, 0, 0);
        this.grid = new Grid();
        this.state = new State();
        this.mouseX = 0;
        this.mouseY = 0;
        this.packer = packer;
        this.currentSpace = -1;
        this.file = problemFile;
        this.edited = false;
        this.parent = parent;
        this.tabIndex = tabIndex;
        initToolbar();
        initListeners();
        setParameters(0, 0);
        
        importFromFile(file);
    }
    
    public EditProblemPanel(MainFrame parent, File problemFile, int tabIndex, int width, int height, ImagePacker packer) {
        super(new BorderLayout());
        this.gridRectangle = new GridRectangle(0, 0, 0, 0);
        this.grid = new Grid();
        this.state = new State();
        this.mouseX = 0;
        this.mouseY = 0;
        this.packer = packer;
        this.currentSpace = -1;
        this.file = problemFile;
        this.edited = false;
        this.parent = parent;
        this.tabIndex = tabIndex;
        initToolbar();
        initListeners();
        setParameters(width, height);
    }
    
    
    
    /*
        Initialization methods
    */  
    private void initListeners() {
        // mouse listener
        this.addMouseListener(this);
        
        // mouse motion listener
        this.addMouseMotionListener(this);
        
        // keyboard listener
        //KeyboardManager.init();
    }
    
    private void initToolbar() {
        JToolBar toolBar = new JToolBar("Tiles");
        addButtons(toolBar);
        addSlider(toolBar);
        add(toolBar, BorderLayout.PAGE_START);
    }
    
    
    
    
    /*
        Private methods
    */
    private void importFromFile(File file) {
        SokobanReader reader = new SokobanReader(file);
        
        // if read successfuly
        if (reader.isEnabled()) {
            reader.read(grid, state);
                
            // resize the display grid
            resizeGrid(reader.getWidth(), reader.getHeight());
        }
    }
    
    private boolean canSaveGrid() {
        if (!isWallConnected()) {
            JOptionPane.showMessageDialog(this, "The outer walls must be connected!", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (hasAnyEmptySpaces()) {
            JOptionPane.showMessageDialog(this, "There must not be an empty space inside the problem!", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (!isWorkerPresent()) {
            JOptionPane.showMessageDialog(this, "There is no worker!", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (!isCrateGoalNumOk()) {
            JOptionPane.showMessageDialog(this, "The number of crates and goals must be the same and also higher than 1!", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    
    
    /*
        Public methods
    */
    public void resize(int zoomValue) {
        zoom = zoomValue;
        
        gridRectangle.width = DEFAULT_TILE_SIZE * gridWidth * zoom;
        gridRectangle.height = DEFAULT_TILE_SIZE * gridHeight * zoom;
        
        this.repaint();
    }
    
    public void resizeGrid(int width, int height) {
        this.gridWidth = width;
        this.gridHeight = height;
        
        gridRectangle.width = DEFAULT_TILE_SIZE * gridWidth * zoom;
        gridRectangle.height = DEFAULT_TILE_SIZE * gridHeight * zoom;
        
        this.repaint();
    }
    
    public Grid getGrid() {
        return grid;
    }
    
    public State getGameState() {
        return state;
    }
    
    public int getGridWidth() {
        return this.gridWidth;
    }
    
    public int getGridHeight() {
        return this.gridHeight;
    }
    
    public boolean isCrateGoalNumOk() {
        if (grid.getGoals().isEmpty() || state.getCrates().isEmpty())
            return false;
        
        return grid.getGoals().size() == state.getCrates().size();
    }
    
    public boolean isWorkerPresent() {
        return state.getWorker() != null;
    }
    
    public boolean isWallConnected() {
        // check upper wall
        if (!checkWallConnectionUp())
            return false;
        
        // check right wall
        if (!checkWallConnectionRight())
            return false;
        
        // check down wall
        if (!checkWallConnectionDown())
            return false;
        
        // check left wall
        return checkWallConnectionLeft();
    }
    
    public boolean hasAnyEmptySpaces() {
        for (Position pos : grid.getPositions()) {
            Space space = grid.getSpace(pos);
            if (space != null && space.type == Space.FREE) {
                if (grid.isVoid(pos.getUp()) || grid.isVoid(pos.getRight()) || grid.isVoid(pos.getDown()) || grid.isVoid(pos.getLeft())) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public void optimizeGrid() {
        Position pos;
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                pos = new Position(x, y);
                
                if (grid.existsWall(pos.getUp()) && grid.existsWall(pos.getRight()) && grid.existsWall(pos.getDown()) && grid.existsWall(pos.getLeft())) {
                    grid.removeSpace(pos);
                }
            }
        }
        
        this.repaint();
    }
    
    public boolean wasEdited() {
        return edited;
    }
    
    public String getFileName() {
        return file.getName();
    }
    
    public void save() {
        if (edited && canSaveGrid()) {
            SokobanWriter sw = new SokobanWriter(file);
            if (sw.isEnabled()) {
                optimizeGrid();
                sw.write(gridWidth, gridHeight, grid, state);
                edited = false;
                parent.stateChanged(tabIndex);
            }
        }
    }
    
    public boolean export(File sFile) {
        if (canSaveGrid()) {
            SokobanWriter sw = new SokobanWriter(sFile);
            if (sw.isEnabled()) {
                optimizeGrid();
                sw.write(gridWidth, gridHeight, grid, state);
                return true;
            }
        }
        
        return false;
    }

    

    
    
    
    
    /**
     * Paints the grid with all the tiles.
     * @param g graphics component which is used to paint
     */
    //<editor-fold defaultstate="collapsed" desc="Paint Component">
    @Override
    protected void paintComponent(Graphics g) {
        // super call
        super.paintComponent(g);
        
        int tileSize = DEFAULT_TILE_SIZE * zoom;
        
        // draw vertical lines
        for (int i = 0; i <= gridWidth; i++) {
            g.drawLine(gridRectangle.x + i * tileSize, gridRectangle.y, gridRectangle.x + i * tileSize, gridRectangle.y + gridRectangle.height);
        }
        
        // draw horizontal lines
        for (int i = 0; i <= gridHeight; i++) {
            g.drawLine(gridRectangle.x, gridRectangle.y + i * tileSize, gridRectangle.x + gridRectangle.width, gridRectangle.y + i * tileSize);
        }
        
        // draw walls and free spaces
        for (Position pos : grid.getPositions()) {
            Space s = grid.getSpace(pos);
            Position p = calcTilePos(pos);
            
            if (s.type == Space.WALL || s.type == Space.FREE) {
                g.drawImage(packer.getImage(s.type), p.getX(), p.getY(), tileSize + 1, tileSize + 1, this);
            } else {
                System.err.println("FATAL ERROR: EditImagePanel@paintComponent -> Space on grid has illegal type.");
                System.exit(1);
            }
        }
        
        // draw goals
        for (Position pos : grid.getGoals()) {
            Position p = calcTilePos(pos);
            g.drawImage(packer.getImage(ImagePacker.GOAL), p.getX(), p.getY(), tileSize + 1, tileSize + 1, this);
        }
        
        // draw crates
        for (Position pos : state.getCrates()) {
            Position p = calcTilePos(pos);
            if (grid.isGoal(pos))
                g.drawImage(packer.getImage(ImagePacker.CRATE_ON_GOAL), p.getX(), p.getY(), tileSize + 1, tileSize + 1, this);
            else
                g.drawImage(packer.getImage(ImagePacker.CRATE), p.getX(), p.getY(), tileSize + 1, tileSize + 1, this);
        }
        
        // draw worker
        if (state.getWorker() != null) {
            Position p = calcTilePos(state.getWorker());
            g.drawImage(packer.getImage(ImagePacker.WORKER), p.getX(), p.getY(), tileSize + 1, tileSize + 1, this);
        }
    }
//</editor-fold>
    
    /**
     * Calculates the drawing position of a tile.
     * @param pos grid position of chosen tile
     * @return drawing position of chosen tile
     */
    //<editor-fold defaultstate="collapsed" desc="Calculate Tile Position">
    private Position calcTilePos(Position pos) {
        int tileSize = DEFAULT_TILE_SIZE * zoom;
        
        int tileX = gridRectangle.x + (pos.getX() * tileSize);
        int tileY = gridRectangle.y + (pos.getY() * tileSize);
        
        return new Position(tileX, tileY);
    }
//</editor-fold>
    
    /**
     * Calculates the grid position of the tile which was clicked on.
     * @param x x coordinate of the mouse click
     * @param y y coordinate of the mouse click
     * @return grid position of clicked tile
     */
    //<editor-fold defaultstate="collapsed" desc="Calculate Mouse Click Position">
    private Position calcMouseToTile(int x, int y) {
        if (x <= gridRectangle.x || y <= gridRectangle.y)
            return null;
        
        if (x >= gridRectangle.x + gridRectangle.width || y >= gridRectangle.y + gridRectangle.height)
            return null;
        
        int tileSize = DEFAULT_TILE_SIZE * zoom;
        
        // x
        int tileX = x - gridRectangle.x;
        tileX = tileX / tileSize;
        
        // y
        int tileY = y - gridRectangle.y;
        tileY = tileY / tileSize;
        
        return new Position(tileX, tileY);
    }
//</editor-fold>
    
    /**
     * Removes the goal, crate and worker tile type from the grid if present on given position.
     * @param pos position to check
     */
    //<editor-fold defaultstate="collapsed" desc="Check For Tiles To Remove">
    private void checkForTilesToRemove(Position pos) {
        if (grid.isGoal(pos)) {
            grid.removeGoal(pos);
        }
        
        if (state.hasCrate(pos)) {
            state.removeCrate(pos);
        }
        
        if (state.isWorker(pos)) {
            state.removeWorker();
        }
    }
//</editor-fold>
    
    /**
     * Adds the selected tile type to the grid to be displayed.
     * @param e mouse event used to draw
     */
    //<editor-fold defaultstate="collapsed" desc="Draw">
    private void draw(MouseEvent e) {
        // grid was edited
        edited = true;
        parent.stateChanged(tabIndex);
        
        // pressed mouse button
        int button = e.getButton();
        
        // calculate which grid tile the mouse has clicked
        Position tilePos = calcMouseToTile(e.getX(), e.getY());
        
        // if mouse has clicked a grid tile
        if (tilePos != null) {
            // add the selected tile type to grid
            if (button == MouseEvent.BUTTON1) {
                // WALL TILE
                if (currentSpace == ImagePacker.WALL) {
                    // add tile to grid
                    grid.setSpace(tilePos, new Space(Space.WALL));
                    
                    // check if any other tile is on this position and remove it from the grid
                    checkForTilesToRemove(tilePos);
                    // FREE TILE
                } else if (currentSpace == ImagePacker.FREE) {
                    // add tile to grid
                    grid.setSpace(tilePos, new Space(Space.FREE));
                    
                    // check if any other tile is on this position and remove it from the grid
                    checkForTilesToRemove(tilePos);
                } else {
                    // all the other tile types need to check if there is a wall on the clicked position
                    if (!grid.isWall(tilePos)) {
                        // GOAL TILE
                        if (currentSpace == ImagePacker.GOAL) {
                            // add tile to grid
                            grid.addGoal(tilePos);
                            
                            // also add an empty space under
                            grid.setSpace(tilePos, new Space(Space.FREE));
                            // WORKER TILE
                        } else if (currentSpace == ImagePacker.WORKER) {
                            // check if clicked tile has no crate
                            if (!state.hasCrate(tilePos)) {
                                // add tile to grid
                                state.setWorker(tilePos);
                                
                                // also add an empty space under
                                grid.setSpace(tilePos, new Space(Space.FREE));
                            }
                            // CRATE TILE
                        } else if (currentSpace == ImagePacker.CRATE) {
                            // check if tile has no worker
                            if (!state.isWorker(tilePos)) {
                                // add tile to grid
                                state.addCrate(tilePos);
                                
                                // also add an empty space under
                                grid.setSpace(tilePos, new Space(Space.FREE));
                            }
                        }
                    }
                }
                // or remove the clicked tile from the grid
            } else if (button == MouseEvent.BUTTON3) {
                if (state.hasCrate(tilePos)) {
                    state.removeCrate(tilePos);
                } else if (state.isWorker(tilePos)) {
                    state.removeWorker();
                } else if (grid.isGoal(tilePos)) {
                    grid.removeGoal(tilePos);
                } else {
                    grid.removeSpace(tilePos);
                }
            }
            
            // repaint the images
            this.repaint();
        }
    }
//</editor-fold>
    
    /**
     * Sets the grid width and height and calculates the initial position and drawing tileSize of the grid.
     * @param width grid width in tiles
     * @param height grid height in tiles
     */
    //<editor-fold defaultstate="collapsed" desc="Set Parameters">
    private void setParameters(int width, int height) {
        // first we set the grid width and height
        this.gridWidth = width;
        this.gridHeight = height;
        
        // then we calculate the grid rectangle tileSize and position
        gridRectangle.x = (int) (this.getWidth() * 0.2);
        gridRectangle.y = (int) (this.getHeight() * 0.2);
        gridRectangle.width = DEFAULT_TILE_SIZE * gridWidth * zoom;
        gridRectangle.height = DEFAULT_TILE_SIZE * gridHeight * zoom;
    }
//</editor-fold>
    
    
    private boolean checkWallConnectionUp() {
        Position pos;
        int y = 0;
        for (int x = 0; x < gridWidth; x++) {
            pos = new Position(x, y);
            
            while (y < gridHeight && grid.isVoid(pos)) {
                y++;
                pos.setY(y);
            }
            
            if (y == gridHeight) {
                y = 0;
                continue;
            }
                
            if (!grid.isWall(pos))
                return false;
            
            y = 0;
        }
        
        return true;
    }
    
    private boolean checkWallConnectionRight() {
        Position pos;
        int x = gridWidth - 1;
        for (int y = 0; y < gridHeight; y++) {
            pos = new Position(x, y);
            
            while (x >= 0 && grid.isVoid(pos)) {
                x--;
                pos.setX(x);
            }
            
            if (x < 0) {
                x = gridWidth - 1;
                continue;
            }
            
            if (!grid.isWall(pos))
                return false;
            
            x = gridWidth - 1;
        }
        
        return true;
    }
    
    private boolean checkWallConnectionDown() {
        Position pos;
        int y = gridHeight - 1;
        for (int x = 0; x < gridWidth; x++) {
            pos = new Position(x, y);
            
            while (y >= 0 && grid.isVoid(pos)) {
                y--;
                pos.setY(y);
            }
            
            if (y < 0) {
                y = gridHeight - 1;
                continue;
            }
                
            if (!grid.isWall(pos))
                return false;
            
            y = gridHeight - 1;
        }
        
        return true;
    }
    
    private boolean checkWallConnectionLeft() {
        Position pos;
        int x = 0;
        for (int y = 0; y < gridHeight; y++) {
            pos = new Position(x, y);
            
            while (x < gridWidth && grid.isVoid(pos)) {
                x++;
                pos.setX(x);
            }
            
            if (x == gridWidth) {
                x = 0;
                continue;
            }
            
            if (!grid.isWall(pos))
                return false;
            
            x = 0;
        }
        
        return true;
    }
    
    
    
    // Toolbar setup methods
    protected void addButtons(JToolBar toolBar) {
        JButton button;
        
        // wall button
        button = addButton(ImagePacker.WALL, WALL, "Wall tile");
        toolBar.add(button);
        
        // free button
        button = addButton(ImagePacker.FREE, FREE, "Empty tile");
        toolBar.add(button);
        
        // goal button
        button = addButton(ImagePacker.GOAL, GOAL, "Goal tile");
        toolBar.add(button);
        
        // crate button
        button = addButton(ImagePacker.CRATE, CRATE, "Crate tile");
        toolBar.add(button);
        
        // worker button
        button = addButton(ImagePacker.WORKER, WORKER, "Worker tile");
        toolBar.add(button);
    }
    
    protected JButton addButton(int imageId, String action, String toolTipText) {
        JButton button = new JButton();
        button.setActionCommand(action);
        button.setToolTipText(toolTipText);
        button.addActionListener(this);
        button.setIcon(new ImageIcon(packer.getImage(imageId)));
        
        return button;
    }
    
    protected void addSlider(JToolBar toolBar) {
        toolBar.add(new javax.swing.JLabel("     Zoom:   1x "));
        
        zoomSlider = new JSlider();
        zoomSlider.setMaximum(10);
        zoomSlider.setMinimum(1);
        zoomSlider.setValue(10);
        zoomSlider.setPreferredSize(new java.awt.Dimension(100, 20));
        zoomSlider.addChangeListener(this);
        toolBar.add(zoomSlider);
        
        toolBar.add(new javax.swing.JLabel(" 10x"));
    }
    // End toolbar setup methods
    
    
    /*
        MouseListener implemented methods
    */
    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        // draw on the grid
        draw(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseX = 0;
        mouseY = 0;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}
    
    
    
    
    /*
        MouseMotionListener implemented methods
    */
    @Override
    public void mouseDragged(MouseEvent e) {
        // if ctrl is pressed move the grid arround
        if (e.isControlDown()) {  
            // set the previous mouse click location
            if (mouseX == 0 && mouseY == 0) {
                mouseX = e.getX();
                mouseY = e.getY();
            // then add the difference between the current and previous mouse click to the grid position
            } else {
                gridRectangle.x += e.getX() - mouseX;
                gridRectangle.y += e.getY() - mouseY;

                mouseX = e.getX();
                mouseY = e.getY();
            }
            
            // repaint the grid
            this.repaint();
        } else {
            // draw on the grid
            draw(e);
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {}
    
    
    
    /*
        ActionListener implemented methods
    */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case WALL:
                currentSpace = ImagePacker.WALL;
                break;
            case FREE:
                currentSpace = ImagePacker.FREE;
                break;
            case GOAL:
                currentSpace = ImagePacker.GOAL;
                break;
            case CRATE:
                currentSpace = ImagePacker.CRATE;
                break;
            case WORKER:
                currentSpace = ImagePacker.WORKER;
                break;
            default:
                break;
        }
    }
    
    
    /*
        ChangeListener implemented methods
    */
    @Override
    public void stateChanged(ChangeEvent e) {
        resize(zoomSlider.getValue());
    }
}
