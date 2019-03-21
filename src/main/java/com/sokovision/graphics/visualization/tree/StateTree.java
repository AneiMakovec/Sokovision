/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sokovision.graphics.visualization.tree;

import java.util.List;
import org.abego.treelayout.util.AbstractTreeForTreeLayout;

/**
 *
 * @author anei
 */
public class StateTree extends AbstractTreeForTreeLayout<DisplayTreeNode> {

    public StateTree(DisplayTreeNode root) {
        super(root);
    }
    
    @Override
    public boolean isLeaf(DisplayTreeNode tn) {
        return tn.childs.isEmpty();
    }

    @Override
    public DisplayTreeNode getParent(DisplayTreeNode tn) {
        return tn.parent;
    }

    @Override
    public List<DisplayTreeNode> getChildrenList(DisplayTreeNode tn) {
        return tn.childs;
    }
    
}
