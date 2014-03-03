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

public class Data implements DB {
	
	private final LockingManager lockingManager;
	
	private final DBAccess dbAccess;
		
	public Data(final String dbLocation) throws DatabaseInitializationException{
		this.dbAccess = new DBAccess(dbLocation);
		this.lockingManager = LockingManager.getInstance();
	}

	public String[] read(final int recNo) throws RecordNotFoundException {
		
		try{
			return dbAccess.getRecord(recNo);
		}catch(final EOFException eofe){
			throw new RecordNotFoundException("Record " + recNo + " not found. " + eofe.getMessage());
		}
		
	}

	public void update(final int recNo, final String[] data,
			final long lockCookie) throws RecordNotFoundException,
			SecurityException {

			lockingManager.verifyCookie(recNo, lockCookie);

			dbAccess.update(recNo, data);

	}

	public void delete(final int recNo, final long lockCookie)
			throws RecordNotFoundException, SecurityException {
		
			lockingManager.verifyCookie(recNo, lockCookie);
			
			dbAccess.delete(recNo);
			
	}
	
	public int[] find(String[] criteria) {
		
		final int[] results = dbAccess.find(criteria);
		
		return results;
		
	}

	public int create(final String[] data) throws DuplicateKeyException {
		
		return dbAccess.create(data);
		
	}

	@Override
	public long lock(int recNo) throws RecordNotFoundException {
		return lockingManager.lock(recNo);
	}

	@Override
	public void unlock(int recNo, long cookie) throws RecordNotFoundException,
			SecurityException {
		lockingManager.unlock(recNo, cookie);
	}


}
