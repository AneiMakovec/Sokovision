/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem;

import grid.Grid;
import grid.Position;
import grid.Space;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author anei
 */
public class Problem {
    
    private final Grid grid;
    
    public Problem() {
        this.grid = new Grid();
    }
    
    public Problem(Grid grid) {
        this.grid = grid;
        scanGrid();
    }

    public Set<Position> getGoals() {
        return this.grid.getGoals();
    }
    
    
    
//    public State initState(String stateString) {
//        State state = new State();
//        
//        int x = 0, y = 0, maxRowLength = 0;
//        String[] rows = stateString.split("\n");
//        
//        boolean gridStarted;
//        for (String row : rows) {
//            String[] symbols = row.split("");
//            gridStarted = false;
//            Position pos;
//            
//            for (String symbol : symbols) {
//                if (gridStarted) {
//                    switch(symbol) {
//                        case "#":   // wall
//                            this.grid.setSpace(new Position(x, y), new Space(Space.WALL));
//                            break;
//                        case " ":   // free space
//                            this.grid.setSpace(new Position(x, y), new Space(Space.FREE));
//                            break;
//                        case ".":   // empty goal 
//                            pos = new Position(x, y);
//                            this.grid.setSpace(pos, new Space(Space.FREE));
//                            this.grid.addGoal(pos);
//                            break;
//                        case "@":   // worker on free space
//                            pos = new Position(x, y);
//                            this.grid.setSpace(pos, new Space(Space.FREE));
//                            state.setWorker(pos);
//                            break;
//                        case "+":   // worker on goal
//                            pos = new Position(x, y);
//                            this.grid.setSpace(pos, new Space(Space.FREE));
//                            state.setWorker(pos);
//                            this.grid.addGoal(pos);
//                            break;
//                        case "$":   // crate on free space
//                            pos = new Position(x, y);
//                            this.grid.setSpace(pos, new Space(Space.FREE));
//                            state.addCrate(pos);
//                            break;
//                        case "*":   // crate on goal
//                            pos = new Position(x, y);
//                            this.grid.setSpace(pos, new Space(Space.FREE));
//                            state.addCrate(pos);
//                            this.grid.addGoal(pos);
//                            break;
//                        default:
//                            break;
//                    }
//                } else {
//                    if (symbol.equals("#")) {
//                        gridStarted = true;
//                        this.grid.setSpace(new Position(x, y), new Space(Space.WALL));
//                    }
//                }
//                
//                x++;
//            }
//            
//            if (x > maxRowLength)
//                maxRowLength = x;
//            
//            y++;
//            x = 0;
//        }
//        
//        // go backwards through the grid and check for dead spaces
//        while (y >= 0) {
//            y--;
//            x = maxRowLength;
//            
//            while (x >= 0) {
//                x--;
//                
//                this.checkCornerAt(new Position(x, y));
//            }
//        }
//        
//        return state;
//    }
    
    private void scanGrid() {
        for (Position pos : grid.getPositions()) {
            checkCornerAt(pos);
            checkWallLineAt(pos);
        }
    }
    
    private void checkCornerAt(Position position) {
        Space space = this.grid.getSpace(position);
        
        if (space != null) {
            if (space.type == Space.FREE) {
                ArrayList<Space> neighbours = new ArrayList<>();
                neighbours.add(0, this.grid.getSpace(position.getUp()));
                neighbours.add(1, this.grid.getSpace(position.getRight()));
                neighbours.add(2, this.grid.getSpace(position.getDown()));
                neighbours.add(3, this.grid.getSpace(position.getLeft()));

                int modifier = 0;
                while (modifier <= 3) {
                    if (modifier == 3) {
                        if (neighbours.get(modifier) != null && neighbours.get(0) != null) {
                            if (neighbours.get(modifier).type == Space.WALL && neighbours.get(0).type == Space.WALL) {
                                space.isDead = true;
                                this.grid.replaceSpace(position, space);
                            }
                        }
                    } else {
                        if (neighbours.get(modifier) != null && neighbours.get(modifier + 1) != null) {
                            if (neighbours.get(modifier).type == Space.WALL && neighbours.get(modifier + 1).type == Space.WALL) {
                                space.isDead = true;
                                this.grid.replaceSpace(position, space);
                            }
                        }
                    }

                    modifier++;
                }
            }
        }
    }
    
    private void checkWallLineAt(Position position) {
        Space space = grid.getSpace(position);
        
        if (space != null && !space.isDead && space.type == Space.FREE) {
            // check up
            Space neighbour = grid.getSpace(position.getUp());
            if (neighbour.type == Space.WALL) {
                if (checkWallLineRight(position.getRight()) && checkWallLineLeft(position.getLeft())) {
                    space.isDead = true;
                }
            } else {
                // check down
                neighbour = grid.getSpace(position.getDown());
                if (neighbour.type == Space.WALL) {
                    if (checkWallLineRight(position.getRight()) && checkWallLineLeft(position.getLeft())) {
                        space.isDead = true;
                    }
                }
            }
            
            // check right
            neighbour = grid.getSpace(position.getRight());
            if (neighbour.type == Space.WALL) {
                if (checkWallLineUp(position.getUp()) && checkWallLineDown(position.getDown())) {
                    space.isDead = true;
                }
            } else {
                // check left
                neighbour = grid.getSpace(position.getLeft());
                if (neighbour.type == Space.WALL) {
                    if (checkWallLineUp(position.getUp()) && checkWallLineDown(position.getDown())) {
                        space.isDead = true;
                    }
                }
            }
        }
    }
    
    private boolean checkWallLineUp(Position position) {
        Space space = grid.getSpace(position);
        
        if (space.isDead || space.type == Space.WALL) {
            return true;
        } else {
            // check if right neighbour is wall
            Space neighbour = grid.getSpace(position.getRight());
            if (neighbour.type == Space.WALL) {
                return checkWallLineUp(position.getUp());
            } else {
                // check if left neighbour is wall
                neighbour = grid.getSpace(position.getLeft());
                if (neighbour.type == Space.WALL) {
                    return checkWallLineUp(position.getUp());
                } else {
                    // if neither neighbours are walls return false
                    return false;
                }
            }
        }
    }
    
    private boolean checkWallLineDown(Position position) {
        Space space = grid.getSpace(position);
        
        if (space.isDead || space.type == Space.WALL) {
            return true;
        } else {
            // check if right neighbour is wall
            Space neighbour = grid.getSpace(position.getRight());
            if (neighbour.type == Space.WALL) {
                return checkWallLineDown(position.getDown());
            } else {
                // check if left neighbour is wall
                neighbour = grid.getSpace(position.getLeft());
                if (neighbour.type == Space.WALL) {
                    return checkWallLineDown(position.getDown());
                } else {
                    // if neither neighbours are walls return false
                    return false;
                }
            }
        }
    }
    
    private boolean checkWallLineRight(Position position) {
        Space space = grid.getSpace(position);
        
        if (space.isDead || space.type == Space.WALL) {
            return true;
        } else {
            // check if up neighbour is wall
            Space neighbour = grid.getSpace(position.getUp());
            if (neighbour.type == Space.WALL) {
                return checkWallLineRight(position.getRight());
            } else {
                // check if down neighbour is wall
                neighbour = grid.getSpace(position.getDown());
                if (neighbour.type == Space.WALL) {
                    return checkWallLineRight(position.getRight());
                } else {
                    // if neither neighbours are walls return false
                    return false;
                }
            }
        }
    }
    
    private boolean checkWallLineLeft(Position position) {
        Space space = grid.getSpace(position);
        
        if (space.isDead || space.type == Space.WALL) {
            return true;
        } else {
            // check if up neighbour is wall
            Space neighbour = grid.getSpace(position.getUp());
            if (neighbour.type == Space.WALL) {
                return checkWallLineLeft(position.getLeft());
            } else {
                // check if down neighbour is wall
                neighbour = grid.getSpace(position.getDown());
                if (neighbour.type == Space.WALL) {
                    return checkWallLineLeft(position.getLeft());
                } else {
                    // if neither neighbours are walls return false
                    return false;
                }
            }
        }
    }
    
    public List<Node> getPossibleActions(Node node) {
        LinkedList<Node> states = null;
        
        if (node.state.getWorker() != null) {
            states = new LinkedList<>();
            
            State newState;
            
            Space space;
            Position position;
            Position tempPosition;
            
            
            // check up
            position = node.state.getWorker().getUp();
            if (!node.state.hasCrate(position)) {
                space = this.grid.getSpace(position);
                if (space != null && space.type == Space.FREE) {
                    // is FREE space -> move worker to it
                    newState = node.state.copy();
                    newState.setWorker(position);
                    states.add(new Node(node, newState, "u", node.cost + 1));
                }
            } else {
                // has crate -> check if it can be pushed
                tempPosition = position.getUp();
                if (!node.state.hasCrate(tempPosition)) {
                    space = this.grid.getSpace(tempPosition);
                    if (space != null && space.type == Space.FREE) {
                        if (space.isDead) {
                            if (this.grid.isGoal(tempPosition)) {
                                // it can be pushed -> push it
                                newState = node.state.copy();
                                newState.setWorker(position);
                                newState.removeCrate(position);
                                newState.addCrate(tempPosition);
                                states.add(new Node(node, newState, "u", node.cost + 1));
                            }
                        } else {
                            // it can be pushed -> push it
                            newState = node.state.copy();
                            newState.setWorker(position);
                            newState.removeCrate(position);
                            newState.addCrate(tempPosition);
                            states.add(new Node(node, newState, "u", node.cost + 1));
                        }
                    }
                }
            }
            
            
            // check right
            position = node.state.getWorker().getRight();
            if (!node.state.hasCrate(position)) {
                space = this.grid.getSpace(position);
                if (space != null && space.type == Space.FREE) {
                    // is FREE space -> move worker to it
                    newState = node.state.copy();
                    newState.setWorker(position);
                    states.add(new Node(node, newState, "r", node.cost + 1));
                }
            } else {
                // has crate -> check if it can be pushed
                tempPosition = position.getRight();
                if (!node.state.hasCrate(tempPosition)) {
                    space = this.grid.getSpace(tempPosition);
                    if (space != null && space.type == Space.FREE) {
                        if (space.isDead) {
                            if (this.grid.isGoal(tempPosition)) {
                                // it can be pushed -> push it
                                newState = node.state.copy();
                                newState.setWorker(position);
                                newState.removeCrate(position);
                                newState.addCrate(tempPosition);
                                states.add(new Node(node, newState, "r", node.cost + 1));
                            }
                        } else {
                            // it can be pushed -> push it
                            newState = node.state.copy();
                            newState.setWorker(position);
                            newState.removeCrate(position);
                            newState.addCrate(tempPosition);
                            states.add(new Node(node, newState, "r", node.cost + 1));
                        }
                    }
                }
            }
            
            
            // check down
            position = node.state.getWorker().getDown();
            if (!node.state.hasCrate(position)) {
                space = this.grid.getSpace(position);
                if (space != null && space.type == Space.FREE) {
                    // is FREE space -> move worker to it
                    newState = node.state.copy();
                    newState.setWorker(position);
                    states.add(new Node(node, newState, "d", node.cost + 1));
                }
            } else {
                // has crate -> check if it can be pushed
                tempPosition = position.getDown();
                if (!node.state.hasCrate(tempPosition)) {
                    space = this.grid.getSpace(tempPosition);
                    if (space != null && space.type == Space.FREE) {
                        if (space.isDead) {
                            if (this.grid.isGoal(tempPosition)) {
                                // it can be pushed -> push it
                                newState = node.state.copy();
                                newState.setWorker(position);
                                newState.removeCrate(position);
                                newState.addCrate(tempPosition);
                                states.add(new Node(node, newState, "d", node.cost + 1));
                            }
                        } else {
                            // it can be pushed -> push it
                            newState = node.state.copy();
                            newState.setWorker(position);
                            newState.removeCrate(position);
                            newState.addCrate(tempPosition);
                            states.add(new Node(node, newState, "d", node.cost + 1));
                        }
                    }
                }
            }
            
            
            // check left
            position = node.state.getWorker().getLeft();
            if (!node.state.hasCrate(position)) {
                space = this.grid.getSpace(position);
                if (space != null && space.type == Space.FREE) {
                    // is FREE space -> move worker to it
                    newState = node.state.copy();
                    newState.setWorker(position);
                    states.add(new Node(node, newState, "l", node.cost + 1));
                }
            } else {
                // has crate -> check if it can be pushed
                tempPosition = position.getLeft();
                if (!node.state.hasCrate(tempPosition)) {
                    space = this.grid.getSpace(tempPosition);
                    if (space != null && space.type == Space.FREE) {
                        if (space.isDead) {
                            if (this.grid.isGoal(tempPosition)) {
                                // it can be pushed -> push it
                                newState = node.state.copy();
                                newState.setWorker(position);
                                newState.removeCrate(position);
                                newState.addCrate(tempPosition);
                                states.add(new Node(node, newState, "l", node.cost + 1));
                            }
                        } else {
                            // it can be pushed -> push it
                            newState = node.state.copy();
                            newState.setWorker(position);
                            newState.removeCrate(position);
                            newState.addCrate(tempPosition);
                            states.add(new Node(node, newState, "l", node.cost + 1));
                        }
                    }
                }
            }
        }
        
        /* TUNNEL CHECKING - WORK IN PROGRESS
        if ((this.grid.get(position.getUp()).type == Space.WALL && this.grid.get(position.getDown()).type == Space.WALL)
                                 &&
                                (this.grid.get(tempPosition.getUp()).type == Space.WALL || this.grid.get(tempPosition.getDown()).type == Space.WALL)) {
                                
                                // is tunnel
                                newState = node.state.copy();
                                newState.setWorker(position);
                                newState.removeCrate(position);
                                newState.addCrate(tempPosition);
                                Node newNode = new Node(node, newState, "l", node.cost + 1, false);
                                
                                if (this.grid.get(tempPosition.getLeft()).type == Space.WALL || this.grid.get(tempPosition.getLeft()).isDead || newState.hasCrate(tempPosition.getLeft())) {
                                    states.add(newNode);
                                } else {
                                    //System.out.println("TUNNEL from:");
                                    //printState(newState);
                                    
                                    newState = newState.copy();
                                    newState.setWorker(tempPosition);
                                    newState.removeCrate(tempPosition);
                                    newState.addCrate(tempPosition.getLeft());
                                    
                                    //System.out.println("to:");
                                    //printState(newState);
                                    
                                    states.add(new Node(newNode, newState, "l", newNode.cost + 1, true));
                                    //return states;
                                }
                            } else {
                                newState = node.state.copy();
                                newState.setWorker(position);
                                newState.removeCrate(position);
                                newState.addCrate(tempPosition);
                                states.add(new Node(node, newState, "l", node.cost + 1, false));
                            }
        */
        
        return states;
    }
    
    public boolean isEnd(State state) {
        return state.getCrates().stream().noneMatch((pos) -> (!this.grid.isGoal(pos)));
    }
    
    public boolean isDeadlock(State state) {
        // PREVIOUS DEADLOCK DETECTION IMPLEMENTATION
        /*
        for (Position pos : state.getCrates()) {
            // crate next to wall between two dead spaces
              //#####
              //#X$X#
            
            if (this.grid.get(pos.getUp()).type == Space.WALL || this.grid.get(pos.getDown()).type == Space.WALL) {
                if (this.grid.get(pos.getRight()).isDead && this.grid.get(pos.getLeft()).isDead) {
                    if (!this.goals.contains(pos.getRight()) && !this.goals.contains(pos.getLeft())) {
                        return true;
                    }
                }
            } else if (this.grid.get(pos.getRight()).type == Space.WALL || this.grid.get(pos.getLeft()).type == Space.WALL) {
                if (this.grid.get(pos.getUp()).isDead && this.grid.get(pos.getDown()).isDead) {
                    if (!this.goals.contains(pos.getUp()) && !this.goals.contains(pos.getDown())) {
                        return true;
                    }
                }
            }
            
            
            // two crates next to wall
              //##
              //$$
            
            if (state.hasCrate(pos.getUp())) {
                if (this.grid.get(pos.getRight()).type == Space.WALL && this.grid.get(pos.getUp().getRight()).type == Space.WALL) {
                    return true;
                } else if (this.grid.get(pos.getLeft()).type == Space.WALL && this.grid.get(pos.getUp().getLeft()).type == Space.WALL) {
                    return true;
                }
            } else if (state.hasCrate(pos.getRight())) {
                if (this.grid.get(pos.getUp()).type == Space.WALL && this.grid.get(pos.getUp().getRight()).type == Space.WALL) {
                    return true;
                } else if (this.grid.get(pos.getDown()).type == Space.WALL && this.grid.get(pos.getDown().getRight()).type == Space.WALL) {
                    return true;
                }
            } else if (state.hasCrate(pos.getDown())) {
                if (this.grid.get(pos.getRight()).type == Space.WALL && this.grid.get(pos.getDown().getRight()).type == Space.WALL) {
                    return true;
                } else if (this.grid.get(pos.getLeft()).type == Space.WALL && this.grid.get(pos.getDown().getLeft()).type == Space.WALL) {
                    return true;
                }
            } else if (state.hasCrate(pos.getLeft())) {
                if (this.grid.get(pos.getUp()).type == Space.WALL && this.grid.get(pos.getUp().getLeft()).type == Space.WALL) {
                    return true;
                } else if (this.grid.get(pos.getDown()).type == Space.WALL && this.grid.get(pos.getDown().getLeft()).type == Space.WALL) {
                    return true;
                }
            }
        }
        
        return false;
        */
        
        // NEW DEADLOCK DETECTION IMPLEMENTATION
        
        // FROZEN CRATE DETECTION
        HashSet<Position> frozen = new HashSet<>();
        HashSet<Position> seenCrates = new HashSet<>();
        for (Position crate : state.getCrates()) {
            // check if crate can be pushed vertically or horizontally
            if (isFrozenV(crate, state.getCrates(), seenCrates)) {
                seenCrates.clear();
                
                if (isFrozenH(crate, state.getCrates(), seenCrates)) {
                    // crate is frozen
                    frozen.add(crate);
                }
            }
            seenCrates.clear();
        }
        
        // check if any of frozen crates is not on goal
        for (Position f : frozen) {
            if (!this.grid.isGoal(f)) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean isFrozenH(Position crate, HashSet<Position> crates, HashSet<Position> seenCrates) {
        if (seenCrates.contains(crate))
            return true;
        else
            seenCrates.add(crate);
        
        Space left = this.grid.getSpace(crate.getLeft());
        Space right = this.grid.getSpace(crate.getRight());
        
        // check if left or right is wall
        if (left.type == Space.WALL || right.type == Space.WALL) {
            return true;
        // check if left and right are dead
        } else if ((left.isDead && right.isDead) && (!this.grid.isGoal(crate.getLeft()) && !this.grid.isGoal(crate.getRight()))) {
            return true;
        // check if left is crate
        } else if (crates.contains(crate.getLeft())) {
            // check if left crate is frozen vertically
            return isFrozenV(crate.getLeft(), crates, seenCrates);
            // check if right is crate
        } else if (crates.contains(crate.getRight())) {
            // check if right crate is frozen vertically
            return isFrozenV(crate.getRight(), crates, seenCrates);
        } else {
            return false;
        }
    }
    
    private boolean isFrozenV(Position crate, HashSet<Position> crates, HashSet<Position> seenCrates) {
        if (seenCrates.contains(crate))
            return true;
        else
            seenCrates.add(crate);
        
        Space up = this.grid.getSpace(crate.getUp());
        Space down = this.grid.getSpace(crate.getDown());
        
        // check if up or down is wall
        if (up.type == Space.WALL || down.type == Space.WALL) {
            return true;
        // check if up and down are dead
        } else if ((up.isDead && down.isDead) && (!this.grid.isGoal(crate.getUp()) && !this.grid.isGoal(crate.getDown()))) {
            return true;
        // check if up is crate
        } else if (crates.contains(crate.getUp())) {
            // check if upper crate is frozen horizontally
            return isFrozenH(crate.getUp(), crates, seenCrates);
            // check if down is crate
        } else if (crates.contains(crate.getDown())) {
            // check if down crate is frozen horizontally
            return isFrozenH(crate.getDown(), crates, seenCrates);
        } else {
            return false;
        }
    }
    
    public void printState(State state, boolean debug) {
        Set<Position> pos = this.grid.getPositions();
        
        // find width and height of the grid
        int x = 0, y = 0;
        for (Position p : pos) {
            if (p.getX() > x)
                x = p.getX();
            
            if (p.getY() > y)
                y = p.getY();
        }
        
        // print out the grid
        
        // the x axis
        System.out.print("  ");
        for (int i = 0; i <= x; i++) {
            System.out.print(String.format("%d", i));
        }
        System.out.println();
        
        // then the y axis along with the grid
        StringBuilder sb;
        for (int j = 0; j <= y; j++) {
            sb = new StringBuilder();
            sb.append(String.format("%d ", j));
            
            for (int k = 0; k <= x; k++) {
                Position key = new Position(k, j);
                Space space = this.grid.getSpace(key);
                
                if (space == null) {
                    sb.append(" ");
                } else {
                    switch (space.type) {
                        case Space.WALL:
                            sb.append("#");
                            break;
                        case Space.FREE:
                            if (state.getWorker().equals(key)) {
                                if (this.grid.isGoal(key))
                                    sb.append("+");
                                else
                                    sb.append("@");
                            } else if (state.hasCrate(key)) {
                                if (this.grid.isGoal(key))
                                    sb.append("*");
                                else
                                    sb.append("$");
                            } else if (this.grid.isGoal(key)) {
                                sb.append(".");
                            } else {
                                if (space.isDead)
                                    sb.append("X");
                                else
                                    sb.append(" ");
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            
            System.out.println(sb.toString());
        }
        
        if (debug) {
            // now print the player data
            System.out.println("Worker is on position: " + state.getWorker().toString());

            // then the crates
            System.out.print("Crates are on positions: ");
            state.getCrates().forEach((p) -> {
                System.out.print(p.toString() + " | ");
            });
            System.out.println();

            // and finaly the goals
            System.out.print("Goals are on positions: ");
            this.grid.getGoals().forEach((p) -> {
                System.out.print(p.toString() + " | ");
            });
            System.out.println();
        }
    }
    
    public int getPlayableAreaSize() {
        int size = 0;
        for (Position pos : this.grid.getPositions()) {
            if (this.grid.getSpace(pos).type == Space.FREE)
                size++;
        }
        
        return size;
    }
}
