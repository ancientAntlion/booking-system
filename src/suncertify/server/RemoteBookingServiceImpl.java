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

public class RemoteBookingServiceImpl implements RemoteBookingService {
	
	private DB database;
	
	public RemoteBookingServiceImpl(final String dbFileName)  throws DatabaseInitializationException {
		database = new Data(dbFileName);
	}
	

	public void book(final int recNo, final String customerID) throws BookingServiceException, RemoteException{
		Long cookie = 0L;
		boolean successfullyLocked = false;
		try {
			cookie = database.lock(recNo);
			successfullyLocked = true;
			final String[] record = database.read(recNo);
			
			if (record[6].trim().isEmpty()) {
				record[6] = customerID;
				database.update(recNo, record, 1L);
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
					database.unlock(recNo, cookie);
				} catch (RecordNotFoundException rnfe) {
					throw new BookingServiceException(rnfe.getMessage());
				} catch (SecurityException se) {
					throw new BookingServiceException(se.getMessage());
				}
			}
		}
	}
	
	public void unbook(final int recNo) throws BookingServiceException, RemoteException{
		Long cookie = 0L;
		boolean successfullyLocked = false;
		try {
			cookie = database.lock(recNo);
			successfullyLocked = true;
			final String[] record = database.read(recNo);
			if (record[6].trim().isEmpty()) {
				throw new BookingServiceException("Could not unbook record number " + recNo + ", it is not booked.");
			} else {
				record[6] = customerFieldWhiteSpace;
				database.update(recNo, record, 1L);
			}
		} catch (RecordNotFoundException rnfe) {
			throw new BookingServiceException(rnfe.getMessage());
		} catch (SecurityException se) {
			throw new BookingServiceException(se.getMessage());
		} finally {
			if(successfullyLocked){
				try {
					database.unlock(recNo, cookie);
				} catch (RecordNotFoundException rnfe) {
					throw new BookingServiceException(rnfe.getMessage());
				} catch (SecurityException se) {
					throw new BookingServiceException(se.getMessage());
				}
			}
		}
	}
	
	public List<String[]> findAll() throws BookingServiceException, RemoteException{

		final List<String[]> recordList = new ArrayList<String[]>();
		try {
			for (int i = 0;; i++) {
				final String[] singleEntry = database.read(i);
				recordList.add(singleEntry);
			}
		} catch (final RecordNotFoundException rnfe) {
			// End of file reached, all records read, carry on
		}

		return recordList;
	}
	
	public List<String[]> find(final String name, final String location) throws BookingServiceException, RemoteException{
		
		final String[] criteria = constructCriteria(name, location);
		final List<String[]> recordList = new ArrayList<String[]>();

		final int[] matchedEntries = database.find(criteria);

		try {
			for (final int i : matchedEntries) {
				final String[] singleEntry = database.read(i);
				recordList.add(singleEntry);
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
	
}
