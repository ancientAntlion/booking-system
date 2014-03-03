package suncertify.ui;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

import suncertify.db.exceptions.DatabaseInitializationException;
import suncertify.server.LocalBookingService;
import suncertify.server.LocalBookingServiceImpl;
import suncertify.server.RemoteBookingService;
import suncertify.server.exceptions.BookingServiceException;
import suncertify.shared.model.Record;
import suncertify.ui.exceptions.InvalidCustomerIDException;
import suncertify.ui.exceptions.ServiceUnavailableException;

/**
 * @author Aaron
 *
 */
public class ClientController {
	
	final static String SERVICE_NAME = "BookingService";

	private final LocalBookingService localBookingService;

	private final RemoteBookingService remoteBookingService;

	private final Mode mode;
	
	private ClientTableModel tableModel;

	String[] columnNames = { "Name", "Location", "Size", "Smoking", "Rate",
			"Date", "Owner" };

	public static String customerFieldWhiteSpace = "        ";

	/**
	 * @param mode
	 * @throws ServiceUnavailableException
	 */
	public ClientController(final Mode mode) throws ServiceUnavailableException {
		try {
			this.mode = mode;
			if (this.mode.equals(Mode.LOCAL)) {
				localBookingService = new LocalBookingServiceImpl(
						"src\\suncertify\\db\\db-1x3.db");
				remoteBookingService = null;
			} else {
				localBookingService = null;
				
				String serverAddress = JOptionPane.showInputDialog("Server Address", "localhost:" + Registry.REGISTRY_PORT);
				
				final String url = "rmi://" + serverAddress + "/" + SERVICE_NAME;
				remoteBookingService = (RemoteBookingService) Naming.lookup(url);
				System.out.println("Connected to BookingService");
			}
		} catch (final RemoteException re) {
			throw new ServiceUnavailableException(re);
		} catch (final NotBoundException nbe) {
			throw new ServiceUnavailableException(nbe);
		} catch (final MalformedURLException mue){
			throw new ServiceUnavailableException(mue);
		} catch (final DatabaseInitializationException die){
			throw new ServiceUnavailableException(die);
		}
	}

	/**
	 * @return
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
				 //TODO Need to save the state of the table. Columns are reseting every call
			} catch (final RemoteException re) {
				throw new ServiceUnavailableException(re);
			}
		}
		
		tableModel = new ClientTableModel(allEntries);
		return tableModel;
	}

	/**
	 * @param name
	 * @param location
	 * @return
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
				throw new ServiceUnavailableException(re);
			}
		}

		tableModel = new ClientTableModel(matchingEntries);
		return tableModel;
	}

	/**
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
		
		final Record record = tableModel.getRecord(recNo);

		if (mode.equals(Mode.LOCAL)) {
			localBookingService.book(record.getRecordNumber(), customerID);
		} else {
			try {
				remoteBookingService.book(record.getRecordNumber(), customerID);
			} catch (final RemoteException re) {
				throw new ServiceUnavailableException(re);
			}
		}

	}

	/**
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
				throw new ServiceUnavailableException(re);
			}
		}

	}

	/**
	 * @param customerID
	 * @return
	 */
	private boolean isValidCustomerID(final String customerID) {
		if (customerID.length() == 8 && customerID.matches("-?\\d+(\\.\\d+)?")) {
			return true;
		}

		return false;
	}

}
