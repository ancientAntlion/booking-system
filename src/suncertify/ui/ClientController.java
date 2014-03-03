package suncertify.ui;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

import suncertify.db.exceptions.DatabaseInitializationException;
import suncertify.server.LocalBookingService;
import suncertify.server.LocalBookingServiceImpl;
import suncertify.server.RemoteBookingService;
import suncertify.server.exceptions.BookingServiceException;
import suncertify.ui.exceptions.InvalidCustomerIDException;
import suncertify.ui.exceptions.RecordNotBookedException;
import suncertify.ui.exceptions.ServiceUnavailableException;

public class ClientController {
	
	final static String SERVICE_NAME = "BookingService";

	private final LocalBookingService localBookingService;

	private final RemoteBookingService remoteBookingService;

	private final Mode mode;

	String[] columnNames = { "Name", "Location", "Size", "Smoking", "Rate",
			"Date", "Owner" };

	public static String customerFieldWhiteSpace = "        ";

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

	public TableModel getAllEntries() throws ServiceUnavailableException,
			BookingServiceException {

		final List<String[]> allEntries;
		if (mode.equals(Mode.LOCAL)) {
			allEntries = localBookingService.find(null, null);
			return new ClientTableModel(allEntries);
		} else {
			try {
				allEntries = remoteBookingService.find(null, null);
				return new ClientTableModel(allEntries);//TODO Need to save the state of the table. Columns are reseting every call
			} catch (final RemoteException re) {
				throw new ServiceUnavailableException(re);
			}
		}
	}

	public TableModel getSpecificEntries(final String name,
			final String location) throws ServiceUnavailableException,
			BookingServiceException {
		final List<String[]> matchingEntries;
		if (mode.equals(Mode.LOCAL)) {
			matchingEntries = localBookingService.find(name, location);
			return new ClientTableModel(matchingEntries);
		} else {
			try {
				matchingEntries = remoteBookingService.find(name, location);
				return new ClientTableModel(matchingEntries);
			} catch (final RemoteException re) {
				throw new ServiceUnavailableException(re);
			}
		}

	}

	public void reserveRoom(final int recNo, final String customerID)
			throws InvalidCustomerIDException, BookingServiceException {
		if (!isValidCustomerID(customerID)) {
			throw new InvalidCustomerIDException(customerID);
		}

		if (mode.equals(Mode.LOCAL)) {
			localBookingService.book(recNo, customerID);
		} else {
			try {
				remoteBookingService.book(recNo, customerID);
			} catch (final RemoteException e) {
				// TODO handle remote exception
			}
		}

	}

	public void unreserveRoom(final int recNo) throws RecordNotBookedException {
		try {
			if (mode.equals(Mode.LOCAL)) {
				localBookingService.unbook(recNo);
			} else {
				try {
					remoteBookingService.unbook(recNo);
				} catch (final RemoteException e) {
					// TODO handle remote exception
				}
			}
		} catch (final BookingServiceException e) {
			// TODO handle exception
		}

	}

	private boolean isValidCustomerID(final String customerID) {
		if (customerID.length() == 8 && customerID.matches("-?\\d+(\\.\\d+)?")) {
			return true;
		}

		return false;
	}

}
