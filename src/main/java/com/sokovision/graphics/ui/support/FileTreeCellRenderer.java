/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sokovision.graphics.ui.support;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

/**
 *
 * @author anei
 */
public class FileTreeCellRenderer extends JLabel implements TreeCellRenderer {

    protected Color m_textSelectionColor;
    protected Color m_textNonSelectionColor;
    protected Color m_bkSelectionColor;
    protected Color m_bkNonSelectionColor;
    protected Color m_borderSelectionColor;

    protected boolean m_selected;
    
    
    public FileTreeCellRenderer() {
        super();
        m_textSelectionColor = UIManager.getColor(
          "Tree.selectionForeground");
        m_textNonSelectionColor = UIManager.getColor(
          "Tree.textForeground");
        m_bkSelectionColor = UIManager.getColor(
          "Tree.selectionBackground");
        m_bkNonSelectionColor = UIManager.getColor(
          "Tree.textBackground");
        m_borderSelectionColor = UIManager.getColor(
          "Tree.selectionBorderColor");
        setOpaque(false);
    }
    
    
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (value instanceof DefaultMutableTreeNode) {
            value = ((DefaultMutableTreeNode) value).getUserObject();
            
            if (value instanceof DataFile) {
                DataFile data = (DataFile) value;
                
                // set text to the name of file
                setText(data.toString());
                
                setIcon(data.getIcon());
            } else {
                setIcon(null);
            }
        }
        
        setFont(tree.getFont());
        setForeground(selected ? m_textSelectionColor : m_textNonSelectionColor);
        setBackground(selected ? m_bkSelectionColor : m_bkNonSelectionColor);
        m_selected = selected;
        return this;
    }
    
    
    @Override
    public void paintComponent(Graphics g) {
        Color bColor = getBackground();
        Icon icon = getIcon();

        g.setColor(bColor);
        int offset = 0;
        if(icon != null && getText() != null) 
            offset = (icon.getIconWidth() + getIconTextGap());

        g.fillRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1);

        if (m_selected) {
            g.setColor(m_borderSelectionColor);
            g.drawRect(offset, 0, getWidth()-1-offset, getHeight()-1);
        }  

        super.paintComponent(g);
    }
    
}
