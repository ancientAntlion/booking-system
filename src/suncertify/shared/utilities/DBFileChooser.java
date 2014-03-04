package suncertify.shared.utilities;

import java.io.File;

import javax.swing.JFileChooser;

/**
 * A utility class with a single static method. Used to open a JFileChooser which the user
 * uses to select the db file from their file system.
 * 
 * @author Aaron
 *
 */
public class DBFileChooser {
	
	/**
	 * Opens a JFileChooser and returns the String containing the full path to the selected file
	 * 
	 * @return filePath
	 */
	public static String selectFile() {
		
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("src\\suncertify\\db\\"));
        fileChooser.setSelectedFile(new File("db-1x3.db"));

        int result = fileChooser.showOpenDialog(null);
        String filePath = null;

        if (result == JFileChooser.APPROVE_OPTION)
        {
        	filePath = fileChooser.getSelectedFile().getPath();            
        }
        else if (result == JFileChooser.CANCEL_OPTION)
        {
        	System.exit(0);
        }
        else if (result == JFileChooser.ERROR_OPTION)
        {
        	System.exit(0);
        }
        
        return filePath;
    
    }

}
