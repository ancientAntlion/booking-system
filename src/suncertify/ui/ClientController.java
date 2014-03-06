package suncertify.ui;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

import suncertify.db.DatabaseInitializationException;
import suncertify.server.LocalBookingService;
import suncertify.server.LocalBookingServiceImpl;
import suncertify.server.RemoteBookingService;
import suncertify.server.exceptions.BookingServiceException;
import suncertify.shared.model.Record;
import suncertify.shared.utilities.DBFileChooser;
import suncertify.shared.utilities.PropertyException;
import suncertify.shared.utilities.PropertyManager;
import suncertify.ui.exceptions.InvalidCustomerIDException;
import suncertify.ui.exceptions.ServiceUnavailableException;

/**
 * This is the controller class. It is the centre and brains of the client side application.
 * A controller instance is owned by the view. It's methods are called by the view
 * And calls from the view in turn update the model.
 * 
 * The controller contains the reference to the serverside booking service. We have 2 BookingService
 * objects, 1 for remote and 1 for local. Depending on what mode is selected 1 will be ignored.
 * 
 * @author Aaron
 */
public class ClientController {
	
	final static String SERVICE_NAME = "BookingService";

	private final LocalBookingService localBookingService;

	private final RemoteBookingService remoteBookingService;

	private final Mode mode;
	
	private ClientTableModel tableModel;

	String[] columnNames = { "Name", "Location", "Size", "Smoking", "Rate",
			"Date", "Owner" };

	/**
	 * Construct in which we initialize our BookingService
	 * 
	 * @param mode
	 * @throws ServiceUnavailableException
	 */
	public ClientController(final Mode mode) throws ServiceUnavailableException {
		try {
			this.mode = mode;
			if (this.mode.equals(Mode.LOCAL)) {
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
				localBookingService = new LocalBookingServiceImpl(filePath);
				remoteBookingService = null;
			} else {
				localBookingService = null;
				String url = null;
				try{
					url = getRmiUrl();
				}catch(final PropertyException pe){
					JOptionPane.showMessageDialog(null, "Encountered a problem with loading/writing properties : " + pe);
					System.exit(0);
				}
				
				remoteBookingService = (RemoteBookingService) Naming.lookup(url);
			}
		} catch (final RemoteException re) {
			throw new ServiceUnavailableException(re.getMessage());
		} catch (final NotBoundException nbe) {
			throw new ServiceUnavailableException(nbe.getMessage());
		} catch (final MalformedURLException mue){
			throw new ServiceUnavailableException(mue.getMessage());
		} catch (final DatabaseInitializationException die){
			throw new ServiceUnavailableException(die.getMessage());
		}
	}

	/**
	 * Gets all records from the database and returns a TableModel filled with these records
	 * 
	 * @return tableModel
	 * @throws ServiceUnavailableException
	 * @throws BookingServiceException
	 */
	public TableModel getAllEntries() throws ServiceUnavailableException,
			BookingServiceException {

		final List<Record> allEntries;
		if (mode.equals(Mode.LOCAL)) {
			allEntries = localBookingService.find(null, null);
		} else {
			try {
				allEntries = remoteBookingService.find(null, null);
			} catch (final RemoteException re) {
				throw new ServiceUnavailableException(re.getMessage());
			}
		}

		tableModel = new ClientTableModel(allEntries);
		return tableModel;
	}

	/**
	 * Gets a filtered list of records from the database and returns a TableModel filled with
	 * these records
	 * 
	 * @param name
	 * @param location
	 * @return tableModel
	 * @throws ServiceUnavailableException
	 * @throws BookingServiceException
	 */
	public TableModel getSpecificEntries(final String name,
			final String location) throws ServiceUnavailableException,
			BookingServiceException {
		final List<Record> matchingEntries;
		if (mode.equals(Mode.LOCAL)) {
			matchingEntries = localBookingService.find(name, location);
		} else {
			try {
				matchingEntries = remoteBookingService.find(name, location);
			} catch (final RemoteException re) {
				throw new ServiceUnavailableException(re.getMessage());
			}
		}

		tableModel = new ClientTableModel(matchingEntries);
		return tableModel;
	}

	/**
	 * Reserves a single room with the specified customerID
	 * 
	 * @param recNo
	 * @param customerID
	 * @throws InvalidCustomerIDException
	 * @throws BookingServiceException
	 * @throws ServiceUnavailableException
	 */
	public void reserveRoom(final int recNo, final String customerID)
			throws InvalidCustomerIDException, BookingServiceException, ServiceUnavailableException {
		if (!isValidCustomerID(customerID)) {
			throw new InvalidCustomerIDException(customerID);
		}
		
		if(recNo == -1){
			return;
		}
		
		final Record record = tableModel.getRecord(recNo);

		if (mode.equals(Mode.LOCAL)) {
			localBookingService.book(record.getRecordNumber(), customerID);
		} else {
			try {
				remoteBookingService.book(record.getRecordNumber(), customerID);
			} catch (final RemoteException re) {
				throw new ServiceUnavailableException(re.getMessage());
			}
		}

	}

	/**
	 * Unreserves a single room
	 * 
	 * @param recNo
	 * @throws BookingServiceException
	 * @throws ServiceUnavailableException
	 */
	public void unreserveRoom(final int recNo) throws BookingServiceException, ServiceUnavailableException {
		
		final Record record = tableModel.getRecord(recNo);
		
		if (mode.equals(Mode.LOCAL)) {
			localBookingService.unbook(record.getRecordNumber());
		} else {
			try {
				remoteBookingService.unbook(record.getRecordNumber());
			} catch (final RemoteException re) {
				throw new ServiceUnavailableException(re.getMessage());
			}
		}

	}

	/**
	 * Determintes if a customerID is numeric and eight digits long
	 * 
	 * @param customerID
	 * @return
	 */
	private boolean isValidCustomerID(final String customerID) {
		if (customerID.length() == 8 && customerID.matches("-?\\d+(\\.\\d+)?")) {
			return true;
		}

		return false;
	}
	
	/**
	 * Contacts the PropertyManager, loads the initial rmi values into an input dialog and
	 * Saves the values that the user specifies into the PropertyManager afterward. Then
	 * Returns an rmiUrl that can be used to lookup the service
	 * 
	 * @return rmiUrl
	 * @throws PropertyException
	 */
	private String getRmiUrl() throws PropertyException{
			PropertyManager propertyManager = PropertyManager.getInstance();
			String rmiHost = propertyManager.getProperty(PropertyManager.RMI_HOST);
			String rmiPort = propertyManager.getProperty(PropertyManager.RMI_PORT);
			
			String serverAddress = JOptionPane.showInputDialog("Server Address", rmiHost + ":" + rmiPort);
			
			if(serverAddress == null){
				//Cancel was clicked, quitting the program
				System.exit(0);
			}
			
			try{
				rmiHost = serverAddress.substring(0, serverAddress.indexOf(":"));
				rmiPort = serverAddress.substring(serverAddress.indexOf(":")+1);
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, "Invalid RMI host supplied");
				System.exit(0);
			}
			
			propertyManager.setProperty(PropertyManager.RMI_HOST, rmiHost);
			propertyManager.setProperty(PropertyManager.RMI_PORT, rmiPort);
			
			return "rmi://" + serverAddress + "/" + SERVICE_NAME;
	}

}
