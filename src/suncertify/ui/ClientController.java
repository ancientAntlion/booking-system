package suncertify.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.TableModel;

import suncertify.db.RecordNotFoundException;
import suncertify.db.SecurityException;
import suncertify.server.DBConnection;
import suncertify.ui.exceptions.InvalidCustomerIDException;
import suncertify.ui.exceptions.RecordAlreadyBookedException;
import suncertify.ui.exceptions.RecordNotBookedException;

public class ClientController {

	private final DBConnection dbConnection;

	String[] columnNames = { "Name", "Location", "Size", "Smoking", "Rate", "Date", "Owner" };
	
	public static String customerFieldWhiteSpace = "        ";

	public ClientController() {
		dbConnection = new DBConnection();
	}

	public TableModel getAllEntries() {
		final List<String[]> entryList = new ArrayList<String[]>();
		try {
			for (int i = 0;; i++) {
				entryList.add(dbConnection.read(i));
			}
		} catch (final RecordNotFoundException rnfe) {
			// End of file reached, all records read, carry on
		}

		final List<String[]> allEntries = new ArrayList<String[]>(); 

		for (int i = 0; i < entryList.size(); i++) {
			final String[] singleEntry = entryList.get(i);
			allEntries.add(singleEntry);
		}

		return new ClientTableModel(allEntries);
	}

	public TableModel getSpecificEntries(final String name,
			final String location) {
		final String[] criteria = constructCriteria(name, location);
		final List<String[]> entryList = new ArrayList<String[]>();

		final int[] matchedEntries = dbConnection.find(criteria);

		try {
			for (final int i : matchedEntries) {
				entryList.add(dbConnection.read(i));
			}
		} catch (final RecordNotFoundException rnfe) {
			// End of file reached, all records read, carry on
		}

		final List<String[]> allEntries = new ArrayList<String[]>(); 

		for (int i = 0; i < entryList.size(); i++) {
			final String[] singleEntry = entryList.get(i);
			allEntries.add(singleEntry);
		}

		return new ClientTableModel(allEntries);
	}

	public void reserveRoom(final int recNo, final String customerID)
			throws RecordAlreadyBookedException, InvalidCustomerIDException {
		try {
			if (!isValidCustomerID(customerID)) {
				throw new InvalidCustomerIDException(customerID);
			}

			final String[] record = dbConnection.read(recNo);
			if (record[6].trim().isEmpty()) {
				record[6] = customerID;
				dbConnection.update(recNo, record, 1L);
			} else {
				throw new RecordAlreadyBookedException(recNo, record[6]);
			}
		} catch (final RecordNotFoundException e) {
			// TODO HANDLE THIS
		} catch (final SecurityException se) {
			// TODO HANDLE THIS
		}

	}
	
	public void unreserveRoom(final int recNo)
			throws RecordNotBookedException {
		try {
			final String[] record = dbConnection.read(recNo);
			if (record[6].trim().isEmpty()) {
				throw new RecordNotBookedException(recNo);
			} else {
				record[6] = customerFieldWhiteSpace;
				dbConnection.update(recNo, record, 1L);
			}
		} catch (final RecordNotFoundException e) {
			// TODO HANDLE THIS
		} catch (final SecurityException se) {
			// TODO HANDLE THIS
		}

	}

	private String[] constructCriteria(final String name, final String location) {
		final String[] criteria = new String[7];
		criteria[0] = name;
		criteria[1] = location;

		return criteria;
	}
	
	private boolean isValidCustomerID(final String customerID){
		if(customerID.length() == 8 && customerID.matches("-?\\d+(\\.\\d+)?")){
			return true;
		}
		
		return false; 
	}

}
