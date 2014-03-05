package suncertify.shared.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyReader {

    private static final String PROPERTY_FILE_NAME = "suncertify.properties";

    private static final String PROPERTY_FILE_DIR = System.getProperty("src\\suncertify\\db\\");

    private static File propertiesFile =
            new File(PROPERTY_FILE_DIR, PROPERTY_FILE_NAME);
    
    private static final String DATABASE_PATH = "dbPath";

    private static final String RMI_HOST = "rmiHost";

    private static final String RMI_PORT = "rmiPort";

    private Logger logger = Logger.getLogger("suncertify.rmi");

    private Properties properties = null;

    private static final PropertyManager instance = new PropertyManager();

    private PropertyManager() {
        properties = loadProperties();
        
        if (properties == null || properties.isEmpty()) {
            properties = new Properties();
            
            properties.setProperty(RMI_HOST, "localhost");
            properties.setProperty(DATABASE_PATH, "");
            properties.setProperty(RMI_PORT,
                    "" + java.rmi.registry.Registry.REGISTRY_PORT);
        }
    }
    
    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
    
    public void setProperty(String propertyName, String propertyValue) {
        properties.setProperty(propertyName, propertyValue);
        saveProperties();
    }
    
    private void saveProperties() {
        try {
            synchronized (propertiesFile) {
                if (propertiesFile.exists()) {
                    propertiesFile.delete();
                }
                propertiesFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(propertiesFile);
                properties.store(fos, "UrlyBird Configuration");
                fos.close();
            }
        } catch (IOException iex) {
            logger.log(Level.SEVERE, "I/O problem when accessing file: "
                            + propertiesFile + "Exception is: " + iex.getMessage());
        }
    }
    
    private Properties loadProperties() {
        Properties loadedProperties = null;
        logger.log(Level.INFO, "File is: " + propertiesFile.toString());
        
        if (propertiesFile.exists() && propertiesFile.canRead()) {
            synchronized(propertiesFile) {
                try {
                    loadedProperties = new Properties();
                    FileInputStream fis = new FileInputStream(propertiesFile);
                    loadedProperties.load(fis);
                    fis.close();
                } catch (FileNotFoundException fex) {
                    logger.log(Level.SEVERE, "File not found at: "
                            + propertiesFile + "Exception is: " + fex.getMessage());
                } catch (IOException iex) {
                    logger.log(Level.SEVERE, "I/O problem when accessing file: "
                            + propertiesFile + "Exception is: " + iex.getMessage());
                }
            }
        }
        return loadedProperties;
    }
	
}





public final class PropertyManager {
    

}