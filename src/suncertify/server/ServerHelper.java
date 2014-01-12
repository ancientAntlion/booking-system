package suncertify.server;

import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ServerHelper {

	public static void main(String[] args) {
		
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("src\\suncertify\\db\\"));
        fileChooser.setSelectedFile(new File("db-1x3.db"));

        int result = fileChooser.showOpenDialog(null);
        String filename;

        if (result == JFileChooser.APPROVE_OPTION)
        {
            filename = fileChooser.getSelectedFile().getPath();
            
            try {
    	        String name = "BookingService";
    	        RemoteBookingService bookingService = new RemoteBookingServiceImpl(filename);
    	        RemoteBookingService stub =
    	            (RemoteBookingService) UnicastRemoteObject.exportObject(bookingService, 0);
    	        Registry registry = LocateRegistry.createRegistry(1099); //your line 23  
    	        registry.rebind(name, stub);
    	        System.out.println("BookingService bound");
    	        JOptionPane.showMessageDialog(null, "BookingService started");  
    	    } catch (Exception e) {
    	        System.err.println("BookingService exception:");
    	        e.printStackTrace();
    	    }
        }
        else if (result == JFileChooser.CANCEL_OPTION)
        {
        	System.exit(0);
        }
        else if (result == JFileChooser.ERROR_OPTION)
        {
        	JOptionPane.showMessageDialog(null, "An error occurred.");
        	System.exit(0);
        }
        
        
		
		
		
		//////////////
		
//		
//		JFileChooser fileChooser = new JFileChooser();
//		
//		
//		JOptionPane.
//		
//        try {
//        	
//        	
//        	
//            String name = "BookingService";
//            RemoteBookingService bookingService = new ComputeEngine();
//            Compute stub =
//                (Compute) UnicastRemoteObject.exportObject(engine, 0);
//            Registry registry = LocateRegistry.getRegistry();
//            registry.rebind(name, stub);
//            System.out.println("ComputeEngine bound");
//        } catch (Exception e) {
//            System.err.println("ComputeEngine exception:");
//            e.printStackTrace();
//        }
    }
	
}
