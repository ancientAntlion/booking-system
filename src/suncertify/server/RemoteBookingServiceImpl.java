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

/**
 * @author Aaron
 *
 */
public class RemoteBookingServiceImpl implements RemoteBookingService {
	
	private DB database;
	
	/**
	 * @param dbFileName
	 * @throws DatabaseInitializationException
	 */
	public RemoteBookingServiceImpl(final String dbFileName)  throws DatabaseInitializationException {
		database = new Data(dbFileName);
	}
	

	/* (non-Javadoc)
	 * @see suncertify.server.BookingService#book(int, java.lang.String)
	 */
	public void book(final int recNo, final String customerID) throws BookingServiceException, RemoteException{
		Long lockCookie = 0L;
		boolean successfullyLocked = false;
		try {
			lockCookie = database.lock(recNo);
			successfullyLocked = true;
			final String[] record = database.read(recNo);
			
			if (record[6].trim().isEmpty()) {
				record[6] = customerID;
				database.update(recNo, record, lockCookie);
			} else {
				throw new BookingServiceException("Could not book record number " + recNo + ", it is already booked.");
			}
		} catch (RecordNotFoundException rnfe) {
			throw new BookingServiceException(rnfe.getMessage());
		} catch (SecurityException se) {
			throw new BookingServiceException(se.getMessage());
		} finally {
			if(successfullyLocked){
				try {
					database.unlock(recNo, lockCookie);
				} catch (RecordNotFoundException rnfe) {
					throw new BookingServiceException(rnfe.getMessage());
				} catch (SecurityException se) {
					throw new BookingServiceException(se.getMessage());
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see suncertify.server.BookingService#unbook(int)
	 */
	public void unbook(final int recNo) throws BookingServiceException, RemoteException{
		Long lockCookie = 0L;
		boolean successfullyLocked = false;
		try {
			lockCookie = database.lock(recNo);
			successfullyLocked = true;
			final String[] record = database.read(recNo);
			if (record[6].trim().isEmpty()) {
				throw new BookingServiceException("Could not unbook record number " + recNo + ", it is not booked.");
			} else {
				record[6] = customerFieldWhiteSpace;
				database.update(recNo, record, lockCookie);
			}
		} catch (RecordNotFoundException rnfe) {
			throw new BookingServiceException(rnfe.getMessage());
		} catch (SecurityException se) {
			throw new BookingServiceException(se.getMessage());
		} finally {
			if(successfullyLocked){
				try {
					database.unlock(recNo, lockCookie);
				} catch (RecordNotFoundException rnfe) {
					throw new BookingServiceException(rnfe.getMessage());
				} catch (SecurityException se) {
					throw new BookingServiceException(se.getMessage());
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see suncertify.server.BookingService#find(java.lang.String, java.lang.String)
	 */
	public List<Record> find(final String name, final String location) throws BookingServiceException, RemoteException{
		
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
	
	/**
	 * @param name
	 * @param location
	 * @return
	 */
	private String[] constructCriteria(final String name, final String location) {
		final String[] criteria = new String[7];
		criteria[0] = name;
		criteria[1] = location;

		return criteria;
	}
	
	/**
	 * @param dbRecord
	 * @param recordNumber
	 * @return
	 */
	private Record constructRecordObject(final String[] dbRecord, final int recordNumber) {
		Record record = new Record(dbRecord, recordNumber);
		
		return record;
	}
	
}
