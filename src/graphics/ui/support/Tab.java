/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.ui.support;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author anei
 */
public class Tab extends JPanel {
    
    private JLabel title;
    private JButton closeButton;
    
    public Tab(File file, int index, ActionListener listener) {
        super(new GridBagLayout());
        initComponents(file, index);
        closeButton.addActionListener(listener);
    }
    
    
    @SuppressWarnings("unchecked")
    private void initComponents(File file, int index) {
        setOpaque(false);
        setToolTipText(file.getAbsolutePath());
        title = new JLabel(file.getName());
        closeButton = new JButton("X");
        closeButton.setActionCommand("Tab" + file.getName());

        // adjust the position of tab components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        // add the tab title
        add(title, gbc);

        gbc.gridx++;
        gbc.weightx = 0;

        // add the close button to tab
        add(closeButton, gbc);
    }
    
    
    public JLabel getTitle() {
        return title;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.title);
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
        final Tab other = (Tab) obj;
        return Objects.equals(this.title, other.title);
    }
    
    
}
