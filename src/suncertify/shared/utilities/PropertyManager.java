package suncertify.shared.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Responsible for interacting with the suncertify.properties file
 * 
 * @author Aaron
 */
public class PropertyManager {
	
    private static final String PROPERTY_FILE_NAME = "suncertify.properties";

    private static final String PROPERTY_FILE_DIR = System.getProperty("user.dir");

    private static File propertiesFile =
            new File(PROPERTY_FILE_DIR, PROPERTY_FILE_NAME);
    
    public static final String DB_FILE_PATH = "databasePath";
    
    public static final String RMI_HOST = "rmiHost";

    public static final String RMI_PORT = "rmiPort";
    
    private static PropertyManager propertyManager;
    
    private Properties properties;
       
	/**
	 * Singleton pattern is used, this returns the instance of the class
	 * 
	 * @return propertyManager
	 * @throws PropertyException 
	 */
	public static PropertyManager getInstance() throws PropertyException{
		if(PropertyManager.propertyManager == null){
			PropertyManager.propertyManager = new PropertyManager();
		}
		
		return propertyManager;
	}
	
	/**
	 * Initialize the properties
	 * @throws PropertyException 
	 */
	public PropertyManager() throws PropertyException{
		this.initProperties();
	}
	
    /**
     * Set a property in memory and write it to disk
     * 
     * @param name
     * @param value
     * @throws PropertyException 
     */
    public void setProperty(String name, String value) throws PropertyException {
        properties.setProperty(name, value);
        saveProperties();
    }
    
    /**
     * Get a property
     * 
     * @param name
     * @return
     */
    public String getProperty(String name) {
        return properties.getProperty(name);
    }
    
    /**
     * Write the current properties object in memory to disk. If the file already exists overwrite it.
     * 
     * @throws PropertyException 
     */
    private void saveProperties() throws PropertyException {
        try {
            synchronized (propertiesFile) {
            	if (!propertiesFile.isDirectory() && propertiesFile.exists() && propertiesFile.canRead()) {
                    propertiesFile.delete();
                }
                propertiesFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(propertiesFile);
                properties.store(fos, "UrlyBird Configuration");
                fos.close();
            }
        } catch (IOException ioe) {
        	throw new PropertyException(ioe);
        }
    }
    
    /**
     * Initializes the properties. If the file does not exist a default is created, otherwise the
     * properties are read in from the file
     * 
     * @throws PropertyException 
     */
    private void initProperties() throws PropertyException {
        if (propertiesFile.exists()) {
            synchronized(propertiesFile) {
                try {
                	properties = new Properties();
                    FileInputStream fis = new FileInputStream(propertiesFile);
                    properties.load(fis);
                    fis.close();
                } catch (IOException ioe) {
                	throw new PropertyException(ioe);
                }
            }
        }else{
        	synchronized(propertiesFile) {
            	properties = new Properties();
            	properties.setProperty(DB_FILE_PATH, "");
            	properties.setProperty(RMI_HOST, "localhost");
            	properties.setProperty(RMI_PORT, "1099");
            	try {
            		FileOutputStream fis = new FileOutputStream(propertiesFile);
					properties.store(new FileOutputStream(propertiesFile), null);
					fis.close();
				} catch (IOException ioe) {
					throw new PropertyException(ioe);
				}
            }
        }
    }
    
    
}


