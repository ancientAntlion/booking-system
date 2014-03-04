package suncertify.shared.utilities;

import java.io.File;

import javax.swing.JFileChooser;

public class DBFileChooser {
	
	/**
	 * @param args
	 */
	public static String selectFile() {
		
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("src\\suncertify\\db\\"));
        fileChooser.setSelectedFile(new File("db-1x3.db"));

        int result = fileChooser.showOpenDialog(null);
        String filename = null;

        if (result == JFileChooser.APPROVE_OPTION)
        {
            filename = fileChooser.getSelectedFile().getPath();            
        }
        else if (result == JFileChooser.CANCEL_OPTION)
        {
        	System.exit(0);
        }
        else if (result == JFileChooser.ERROR_OPTION)
        {
        	System.exit(0);
        }
        
        return filename;
    
    }

}
