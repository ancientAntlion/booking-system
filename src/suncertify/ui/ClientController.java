package suncertify.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.TableModel;

import suncertify.db.RecordNotFoundException;
import suncertify.db.SecurityException;
import suncertify.server.DBConnection;
import suncertify.ui.exceptions.InvalidCustomerIDException;
import suncertify.ui.exceptions.RecordAlreadyBookedException;

public class ClientController {

	private final DBConnection dbConnection;

	String[] columnNames = { "First Name", "Last Name", "Sport", "# of Years",
			"Vegetarian", "aaaa", "bbb" };

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

		final String[][] allEntries;

		if (entryList.isEmpty()) {
			allEntries = new String[0][0];
		} else {

			allEntries = new String[entryList.size()][entryList.get(0).length];

			for (int i = 0; i < entryList.size(); i++) {
				final String[] singleEntry = entryList.get(i);
				allEntries[i] = singleEntry;
			}
		}

		return new ClientTableModel(allEntries, columnNames);
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

		final String[][] allEntries;

		if (entryList.isEmpty()) {
			allEntries = null;
		} else {

			allEntries = new String[entryList.size()][entryList.get(0).length];

			for (int i = 0; i < entryList.size(); i++) {
				final String[] singleEntry = entryList.get(i);
				allEntries[i] = singleEntry;
			}
		}

		return new ClientTableModel(allEntries, columnNames);
	}

	public void reserveRoom(final int recNo, final String customerID)
			throws RecordAlreadyBookedException, InvalidCustomerIDException {
		try {
			if (customerID.length() != 8) {
				throw new InvalidCustomerIDException(customerID);
			}

			final String[] record = dbConnection.read(recNo);
			if (record[6].isEmpty()) {
				record[6] = customerID;
				dbConnection.update(recNo, record, 1L);
			} else {
				throw new RecordAlreadyBookedException(record[6], recNo);
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

}
