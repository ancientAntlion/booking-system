package suncertify.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;

import suncertify.shared.utilities.DBFileChooser;
import suncertify.shared.utilities.PropertyException;

/**
 * @author Aaron
 * 
 * The server boot class. This class is executed to start the booking service
 *
 */
public class ServerStarter {

	/**
	 * Server start method. This method uses the DBFileChooser to locate a file path for
	 * The database. Then a RemoteBookingService is initialized and bound to the RMI Registry
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String filePath = null;
		try{
			 filePath = DBFileChooser.selectFile();
		}catch(final PropertyException pe){
			JOptionPane.showMessageDialog(null, "Encountered a problem with loading/writing properties : " + pe);
			System.exit(0);
		}
		
		if(filePath == null){
			JOptionPane.showMessageDialog(null, "An error occurred.");
			System.exit(0);
		}

        try {
	        String name = "BookingService";
	        RemoteBookingService bookingService = new RemoteBookingServiceImpl(filePath);
	        RemoteBookingService stub =
	            (RemoteBookingService) UnicastRemoteObject.exportObject(bookingService, 0);
	        Registry registry = LocateRegistry.createRegistry(1099); //your line 23  
	        registry.rebind(name, stub);
	        JOptionPane.showMessageDialog(null, "BookingService started");  
	    } catch (Exception e) {
	        System.err.println("BookingService exception:");
	        e.printStackTrace();
	    }
    
    
    }
	
}
