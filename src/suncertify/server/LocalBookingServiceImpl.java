package suncertify.server;

import java.util.ArrayList;
import java.util.List;

import suncertify.db.DB;
import suncertify.db.Data;
import suncertify.db.DatabaseInitializationException;
import suncertify.db.RecordNotFoundException;
import suncertify.db.SecurityException;
import suncertify.server.exceptions.BookingServiceException;
import suncertify.shared.model.Record;

/**
 * Implementation of LocalBookingService interface. When the a client starts it initializes
 * an instance of this class in it's own thread. This class has an instance DB which is our gateway
 * To the database
 * 
 * Because the DB interface takes lockCookies when updating and deleting records we have the lock
 * And unlock handling here rather than inside the DB implementation
 * 
 * @author Aaron
 */
public class LocalBookingServiceImpl implements LocalBookingService {
	
	private DB database;
	
	/**
	 * Constructor
	 * 
	 * @param dbFileName
	 * @throws DatabaseInitializationException
	 */
	public LocalBookingServiceImpl(final String dbFileName) throws DatabaseInitializationException {
		database = new Data(dbFileName);
	}
	
	
	/**
	 * Books a record with the supplied ID. We lock the record and when it is successfully locked we set
	 * The boolean successfullyLocked to true. This is used in the finally block so we know whether or not
	 * We need to unlock the record. Then we read the record and update the record with the supplied customerID
	 * We unlock the record in the finally block to ensure records are unlocked even in error scenarios
	 * 
	 * @param recNo
	 * @param customerID
	 * @throws BookingServiceException
	 */
	public void book(final int recNo, final String customerID) throws BookingServiceException{
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
	
	/**
	 * Unbooks a record. We lock the record and when it is successfully locked we set the boolean
	 * successfullyLocked to true. This is used in the finally block so we know whether or not we need
	 * To unlock the record. Then we read the record and update the record with a blank customer ID
	 * We unlock the record in the finally block to ensure records are unlocked even in error scenarios
	 * 
	 * @param recNo
	 * @throws BookingServiceException
	 */
	public void unbook(final int recNo) throws BookingServiceException{
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
	
	/**
	 * Gets a list of records that start with or equal to the supplied name and location.
	 * If a null value for name or location will match any field value
	 * 
	 * @param name
	 * @param location
	 * @return recordList
	 * @throws BookingServiceException
	 */
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
	
	/**
	 * Constructs a String array that can be sent to the DB.find(String[]) method.
	 * 
	 * @param name
	 * @param location
	 * @return criteria
	 */
	private String[] constructCriteria(final String name, final String location) {
		final String[] criteria = new String[7];
		criteria[0] = name;
		criteria[1] = location;

		return criteria;
	}
	
	/**
	 * Converts a String array and record number into a Record domain object
	 * 
	 * @param dbRecord
	 * @param recordNumber
	 * @return record
	 */
	private Record constructRecordObject(final String[] dbRecord, final int recordNumber) {
		Record record = new Record(dbRecord, recordNumber);

		return record;
	}
	
}
