/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.ui.support;

import java.io.File;
import javax.swing.Icon;

/**
 *
 * @author anei
 */
public class DataFile {
    
    public static final int PROJECT = 0;
    public static final int PROBLEM = 1;
    public static final int SOLVER = 2;
    public static final int STAT = 3;
    public static final int CSV = 4;
    public static final int DIRECTORY = 5;
    public static final int SOLUTION = 6;
    public static final int UNKNOWN = 7;
    
    
    protected int fileType;
    protected Icon   icon;
    protected File dataFile;
    
    
    public DataFile(File file, int fileType, Icon icon) {
        this.dataFile = file;
        this.fileType = fileType;
        this.icon = icon;
    }
    
    public Icon getIcon() { 
        return icon;
    }
    
    public int getFileType() {
        return fileType;
    }
    
    public File getDataFile() {
        return dataFile;
    }

    @Override
    public String toString() { 
        return dataFile.getName();
    }
}
