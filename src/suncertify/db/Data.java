package suncertify.db;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import suncertify.db.exceptions.DatabaseInitializationException;
import suncertify.db.exceptions.DuplicateKeyException;
import suncertify.db.exceptions.RecordNotFoundException;
import suncertify.db.exceptions.SecurityException;
import suncertify.db.locking.LockingManager;

/**
 * @author Aaron
 * 
 * Implementation of DB interface supplied by the instructions.
 * 
 * This class is used to interact with the Database. Any interactions with the 
 * Database should be performed through this class.
 */
public class Data implements DB {
	
	/**
	 * Singleton instance of locking manager 
	 */
	private final LockingManager lockingManager = LockingManager.getInstance();
	
	private final DBAccess dbAccess;
		
	/**
	 * Constructor where the DBAccess is initialized 
	 * 
	 * @param dbLocation which holds the full path to the DB file
	 * @throws DatabaseInitializationException
	 */
	public Data(final String dbLocation) throws DatabaseInitializationException{
		this.dbAccess = new DBAccess(dbLocation);
	}

	/* 
	 * Reads a single record from the database file
	 * 
	 * @param recNo
	 * @return record
	 * @throws RecordNotFoundException
	 */
	public String[] read(final int recNo) throws RecordNotFoundException {
		
		try{
			return dbAccess.getRecord(recNo);
		}catch(final EOFException eofe){
			throw new RecordNotFoundException("Record " + recNo + " not found. " + eofe.getMessage());
		}
		
	}

	/* 
	 * Updates a single record in the database file
	 * 
	 * @param recNo
	 * @param data - the String array which contains the record data
	 * @param lockCookie - the cookie required to update the supplied record
	 * @throws RecordNotFoundException
	 */
	public void update(final int recNo, final String[] data,
			final long lockCookie) throws RecordNotFoundException,
			SecurityException {

			lockingManager.verifyCookie(recNo, lockCookie);

			dbAccess.update(recNo, data);

	}

	/* 
	 * Deletes a single record in the database file
	 * 
	 * @param recNo
	 * @param lockCookie - the cookie required to delete the supplied record
	 * @throws RecordNotFoundException
	 * @throws SecurityException
	 */
	public void delete(final int recNo, final long lockCookie)
			throws RecordNotFoundException, SecurityException {
		
			lockingManager.verifyCookie(recNo, lockCookie);
			
			dbAccess.delete(recNo);
			
	}
	
	/* 
	 * Returns an array of ints containing the index of every record in the
	 * DB file that matches with the criteria supplied
	 * 
	 * @param criteria
	 * @return recordNumbers
	 */
	public int[] find(String[] criteria) {
		
		final int[] recordNumbers = dbAccess.find(criteria);
		
		return recordNumbers;
		
	}

	/* 
	 * Writes a single record to the database file
	 * 
	 * @param data
	 * @return recordNumber
	 */
	public int create(final String[] data) throws DuplicateKeyException {
		
		return dbAccess.create(data);
		
	}

	/* 
	 * Places a lock on a single record and returns a lockCookie
	 * 
	 * @param recNo
	 * @return lockCookie
	 */
	@Override
	public long lock(int recNo) throws RecordNotFoundException {
		return lockingManager.lock(recNo);
	}

	
	/* 
	 * Unlocks a single record if the supplied cookie is valid
	 * 
	 * @param recNo
	 * @param cookie
	 */
	@Override
	public void unlock(int recNo, long cookie) throws RecordNotFoundException,
			SecurityException {
		lockingManager.unlock(recNo, cookie);
	}


}
