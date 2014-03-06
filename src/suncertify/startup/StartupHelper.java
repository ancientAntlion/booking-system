package suncertify.startup;

import java.awt.event.WindowAdapter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;

import suncertify.server.RemoteBookingService;
import suncertify.server.RemoteBookingServiceImpl;
import suncertify.server.exceptions.BookingServiceException;
import suncertify.shared.utilities.DBFileChooser;
import suncertify.shared.utilities.PropertyException;
import suncertify.ui.ClientView;
import suncertify.ui.Mode;
import suncertify.ui.exceptions.InvalidModeException;
import suncertify.ui.exceptions.ServiceUnavailableException;

/**
 * This class acts as the boot class for all modes. It can start a client in either local or remote mode,
 * or it can start a server depending on the command line parameter passed in.
 * 
 * Remote - starts a remote client
 * Local - starts a local client
 * Server - starts a server
 * 
 * @author Aaron
 *
 */
public class StartupHelper {
	
	/**
	 * Server start method. This method uses the DBFileChooser to locate a file path for
	 * The database. Then a RemoteBookingService is initialized and bound to the RMI Registry
	 * 
	 * @param args
	 */
	public static void maian(String[] args) {
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
	
	/**
	 * The Client launcher. This method takes a single command line arguement which must be either
	 * "Local" or "Remote".
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		try{
			final Mode mode = getMode(args);
			
			if(mode.equals(Mode.SERVER)){
				startServer();
			}else{
				startClient(mode);
			}
			
		}catch(InvalidModeException ime){
			JOptionPane.showMessageDialog(null, "Exception encountered with selected mode : " + ime.getMode());
			return;
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Exception encountered\n\n" + e.getMessage());
			return;
		}

	}
	
	/**
	 * Takes the command line arguments, determines the mode and returns it.
	 * 
	 * @param args
	 * @return
	 * @throws InvalidModeException
	 */
	private static Mode getMode(final String[] args) throws InvalidModeException {
		if(args.length == 0){
			throw new InvalidModeException();
		}else if(args[0].equalsIgnoreCase("LOCAL")){
			return Mode.LOCAL;
		}else if(args[0].equalsIgnoreCase("REMOTE")){
			return Mode.REMOTE;
		}else if(args[0].equalsIgnoreCase("SERVER")){
			return Mode.SERVER;
		}else{
			throw new InvalidModeException(args[0]);
		}
	}

	
	/**
	 * Starts a server
	 */
	private static void startServer(){
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
	        JOptionPane.showMessageDialog(null, "Exception encountered\n\n" + e.getMessage());
			return;
	    }
	}
	
	/**
	 * Starts a client in the specified mode
	 * 
	 * @param mode
	 * @throws BookingServiceException 
	 * @throws NotBoundException 
	 * @throws ServiceUnavailableException 
	 * @throws RemoteException 
	 */
	private static void startClient(final Mode mode) throws RemoteException, ServiceUnavailableException, NotBoundException, BookingServiceException{
		final ClientView clientGUI = new ClientView(mode);
		
		clientGUI.addWindowListener(getClosingWindowAdapater());
					
		clientGUI.setVisible(true);
	}
	
	/**
	 * Initializes a WindowAdapter that will kill our client process if client window is closed
	 * 
	 * @return windowAdapter
	 */
	private static WindowAdapter getClosingWindowAdapater(){
		WindowAdapter windowAdapter = new WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	System.exit(0);
		    }
		};
		
		return windowAdapter;
	}

}
