package suncertify.shared.utilities;

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
	 * @throws PropertyException 
	 */
	public static String selectFile() throws PropertyException {
		
		PropertyManager propertyManager = PropertyManager.getInstance();
		
		String filePath = propertyManager.getProperty(PropertyManager.DB_FILE_PATH);
				
        JFileChooser fileChooser = new JFileChooser(filePath);

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION)
        {
        	filePath = fileChooser.getSelectedFile().getAbsolutePath();
        }
        else if (result == JFileChooser.CANCEL_OPTION)
        {
        	System.exit(0);
        }
        else if (result == JFileChooser.ERROR_OPTION)
        {
        	System.exit(0);
        }
        
        propertyManager.setProperty(PropertyManager.DB_FILE_PATH, filePath);
        
        return filePath;
    
    }

}
