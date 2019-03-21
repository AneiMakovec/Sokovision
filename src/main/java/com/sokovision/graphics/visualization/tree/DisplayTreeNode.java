/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sokovision.graphics.visualization.tree;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author anei
 */
public class DisplayTreeNode {
    
    public static final int UNSEEN = 0;
    public static final int SEEN = 1;
    public static final int DEADLOCK = 2;
    public static final int START = 3;
    public static final int END = 4;
    public static final int CURRENT = 5;
    public static final int VISITED = 6;
    
    public DisplayTreeNode parent;
    public List<DisplayTreeNode> childs;
    public int type;
    private int id;
    
    public DisplayTreeNode(int id, DisplayTreeNode parent, int type) {
        this.id = id;
        this.parent = parent;
        this.childs = new LinkedList<>();
        this.type = type;
    }
    
    public void addChild(DisplayTreeNode node) {
        childs.add(node);
    }
    
    public void reset() {
        childs.clear();
        childs = new LinkedList<>();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.parent);
        hash = 37 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DisplayTreeNode other = (DisplayTreeNode) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.parent, other.parent)) {
            return false;
        }
        if (!Objects.equals(this.childs, other.childs)) {
            return false;
        }
        return true;
    }
    
    
    
}
