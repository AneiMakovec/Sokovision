/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sokovision.graphics.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;

/**
 *
 * @author anei
 */
public class SolveSettingsPanel extends JPanel {
    
    
    public SolveSettingsPanel() {
        initComponents();
    }
    
    
    @SuppressWarnings("unchecked")
    private void initComponents() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 170));
    }
}
