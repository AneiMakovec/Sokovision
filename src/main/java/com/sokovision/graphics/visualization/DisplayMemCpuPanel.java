/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sokovision.graphics.visualization;

import java.awt.GridLayout;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author anei
 */
public class DisplayMemCpuPanel extends JPanel {
    
    MemoryUsage mem;
    
    public DisplayMemCpuPanel() {
        initComponents();
    }
    
    @SuppressWarnings("unchecked")
    private void initComponents() {
        JLabel memLabel = new JLabel("Memory used:");
        memText = new JLabel();
        
        setLayout(new GridLayout(1, 1));
        
        add(memLabel);
        add(memText);
        
        updateMemUsage();
    }
    
    
    public void updateMemUsage() {
        mem = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        memText.setText(Long.toString(mem.getUsed()) + "B");
    }
    
    // Variable declaration
    private JLabel memText;
    // End of variable declaration
}
