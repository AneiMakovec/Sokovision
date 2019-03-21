/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sokovision.main;

import com.sokovision.graphics.editor.EditProblemPanel;
import com.sokovision.graphics.support.ImagePacker;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import com.sokovision.support.reader.SokobanReader;
import com.sokovision.support.writer.SokobanWriter;

/**
 *
 * @author anei
 */
public class EditFrame extends javax.swing.JFrame {
    private int width;
    private int height;
    private final ImagePacker packer;
    private File saveFile;
    
    /**
     * Creates new form EditFrame
     * @param width width of problem grid
     * @param height height of problem grid
     */
    public EditFrame(int width, int height) {
        this.width = width;
        this.height = height;
        this.packer = new ImagePacker();
        this.saveFile = null;
        
        //goFullscreen();
        initComponents();
    }
    
    
    @SuppressWarnings("unchecked")
    private void initComponents() {
        editPanel = new EditProblemPanel(width, height, packer);
        selectFileWindow = new javax.swing.JFileChooser();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        separator1 = new javax.swing.JPopupMenu.Separator();
        exportMenuItem = new javax.swing.JMenuItem();
        importMenuItem = new javax.swing.JMenuItem();
        separator2 = new javax.swing.JPopupMenu.Separator();
        quitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        
        // frame setup
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sokoban problem editor");
        setPreferredSize(new java.awt.Dimension(1000, 600));
        
        // add edit panel
        getContentPane().add(editPanel, java.awt.BorderLayout.CENTER);
        
        // menu bar setup
        fileMenu.setText("File");

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setText("Save");
        saveMenuItem.setToolTipText("Save grid to file.");
        saveMenuItem.addActionListener((java.awt.event.ActionEvent evt) -> {
            saveAction();
        });
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setText("Save as");
        saveAsMenuItem.setToolTipText("Save in a new file.");
        saveAsMenuItem.addActionListener((java.awt.event.ActionEvent evt) -> {
            saveAsAction();
        });
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(separator1);

        exportMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        exportMenuItem.setText("Export");
        exportMenuItem.setToolTipText("Export grid to file.");
        exportMenuItem.addActionListener((java.awt.event.ActionEvent evt) -> {
            exportAction();
        });
        fileMenu.add(exportMenuItem);

        importMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        importMenuItem.setText("Import");
        importMenuItem.setToolTipText("Import grid from file.");
        importMenuItem.addActionListener((java.awt.event.ActionEvent evt) -> {
            importAction();
        });
        fileMenu.add(importMenuItem);
        fileMenu.add(separator2);

        quitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        quitMenuItem.setText("Quit");
        quitMenuItem.setToolTipText("Quit the application.");
        quitMenuItem.addActionListener((java.awt.event.ActionEvent evt) -> {
            System.exit(0);
        });
        fileMenu.add(quitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText("Edit");
        menuBar.add(editMenu);

        setJMenuBar(menuBar);

        pack();
    }
    
    
    
    
    // Listener methods
    private void saveAction() {
        if (canSaveGrid()) {
            editPanel.optimizeGrid();
            
            if (saveFile == null) {
                saveFile = selectFile();
            }

            saveToFile(saveFile);
        }
    }
    
    private void saveAsAction() {
        if (canSaveGrid()) {
            editPanel.optimizeGrid();
            saveFile = selectFile();
            saveToFile(saveFile);
        }
    }
    
    private void exportAction() {
        if (canSaveGrid()) {
            editPanel.optimizeGrid();
            File file = selectFile();
            saveToFile(file);
        }
    }
    
    private void importAction() {
        int retVal = JOptionPane.showOptionDialog(this, "Do you want to import grid from file?", "Confirm import", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
        
        if (retVal == 0) {
            // read the grid
            saveFile = selectFile();
            importFromFile(saveFile);
        }
    }
    
    
    
    // Class methods
    
    // Public
    public void importFileToEdit(File file) {
        saveFile = file;
        importFromFile(file);
    }
    
    // Private
    private void goFullscreen() {
        setExtendedState(Frame.MAXIMIZED_BOTH);
    }
    
    private File selectFile() {
        int retVal = selectFileWindow.showOpenDialog(this);
        
        File sFile = null;
        
        if (retVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            sFile = selectFileWindow.getSelectedFile();

            if (!sFile.getName().endsWith(".skvi")) {
                sFile = null;
                JOptionPane.showMessageDialog(this, "Invalid file name extension. File name should end in '.txt'.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        }
        
        return sFile;
    }
    
    private void saveToFile(File sFile) {
        if (sFile != null) {
            try {
                sFile.createNewFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Cannot create new save file.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                System.out.println("ERROR: EditFrame@saveAsMenuActionPerformed - cannot create new save file.");
            }
            
            if (sFile.exists()) {
                SokobanWriter sw = new SokobanWriter(sFile);
                if (sw.isEnabled()) {
                    sw.write(editPanel.getGridWidth(), editPanel.getGridHeight(), editPanel.getGrid(), editPanel.getGameState());
                }
            }
        }
    }
    
    private boolean canSaveGrid() {
        if (!editPanel.isWallConnected()) {
            JOptionPane.showMessageDialog(this, "The outer walls must be connected!", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (!editPanel.isWorkerPresent()) {
            JOptionPane.showMessageDialog(this, "There is no worker!", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (!editPanel.isCrateGoalNumOk()) {
            JOptionPane.showMessageDialog(this, "The number of crates and goals must be the same and also higher than 1!", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void importFromFile(File file) {
        SokobanReader reader = new SokobanReader(file);
            
        if (reader.isEnabled()) {
            reader.read(editPanel.getGrid(), editPanel.getGameState());
            
            this.width = reader.getWidth();
            this.height = reader.getHeight();
                
            // resize the display grid
            editPanel.resizeGrid(this.width, this.height);
        }
    }
  
    
    
    
    // Variable declaration
    private EditProblemPanel editPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JPopupMenu.Separator separator1;
    private javax.swing.JMenuItem exportMenuItem;
    private javax.swing.JMenuItem importMenuItem;
    private javax.swing.JPopupMenu.Separator separator2;
    private javax.swing.JMenuItem quitMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JFileChooser selectFileWindow;
    // End of variable declaration
}
