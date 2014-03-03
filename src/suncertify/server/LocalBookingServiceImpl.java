package suncertify.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import suncertify.db.DB;
import suncertify.db.Data;
import suncertify.db.exceptions.DatabaseInitializationException;
import suncertify.db.exceptions.RecordNotFoundException;
import suncertify.db.exceptions.SecurityException;
import suncertify.server.exceptions.BookingServiceException;
import suncertify.shared.model.Record;

public class LocalBookingServiceImpl implements LocalBookingService {
	
	private DB database;
	
	public LocalBookingServiceImpl(final String dbFileName) throws DatabaseInitializationException {
		database = new Data(dbFileName);
	}
	
	
	public void book(final int recNo, final String customerID) throws BookingServiceException{
		try {//TODO determine if there needs to be no locking during local mode....only 1 thread so i think no need for locking??
			final String[] record = database.read(recNo);
			
			if (record[6].trim().isEmpty()) {
				record[6] = customerID;
				database.update(recNo, record, 1L);
			} else {
				//TODO throw something
			}
		} catch (RecordNotFoundException e) {
			//TODO throw something
		} catch (SecurityException e) {
			//TODO throw something
		}
	}
	
	public void unbook(final int recNo) throws BookingServiceException{
		try {
			final String[] record = database.read(recNo);
			if (record[6].trim().isEmpty()) {
				//TODO throw not booked execption
			} else {
				record[6] = customerFieldWhiteSpace;
				database.update(recNo, record, 1L);
			}
		} catch (final RecordNotFoundException e) {
			// TODO HANDLE THIS
		} catch (final SecurityException se) {
			// TODO HANDLE THIS
		}
	}
	
	public List<Record> find(final String name, final String location) throws BookingServiceException{
		
		final String[] criteria = constructCriteria(name, location);
		final List<Record> recordList = new ArrayList<Record>();

		final int[] matchedEntries = database.find(criteria);

		try {
			for (final int i : matchedEntries) {
				final String[] singleEntry = database.read(i);
				recordList.add(constructRecordObject(singleEntry, i));
			}
		} catch (final RecordNotFoundException rnfe) {
			// End of file reached, all records read, carry on
		}

		return recordList;
		
	}
	
	private String[] constructCriteria(final String name, final String location) {
		final String[] criteria = new String[7];
		criteria[0] = name;
		criteria[1] = location;

		return criteria;
	}
	
	private Record constructRecordObject(final String[] dbRecord, final int recordNumber) {
		Record record = new Record(dbRecord[0], dbRecord[1], dbRecord[2], dbRecord[3], dbRecord[4], dbRecord[5], dbRecord[6], recordNumber);

		return record;
	}
	
}


