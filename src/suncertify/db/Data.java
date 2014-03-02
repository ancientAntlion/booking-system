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
	
	private final int singleRecordByteSize = 160;

	private final int[] recordEntrySizes = { 64, 64, 4, 1, 8, 10, 8 };

	private final int[] recordEntryPositions = { 0, 64, 128, 132, 133, 141,
			151, 159 };

	private final int firstRecordIndex = 56;

	private final int firstRecordNameIndex = firstRecordIndex + 1;

	private final int numberOfFieldsInRecord = 7;
	
	private final Random random = new Random();
	
	private final LockingManager lockingManager;
	
	private final DBAccess dbAccess;
		
	public Data(final String dbLocation) throws DatabaseInitializationException{
		this.dbAccess = new DBAccess(dbLocation);
		this.lockingManager = LockingManager.getInstance();
	}

	public String[] read(final int recNo) throws RecordNotFoundException {

		try {
			final RandomAccessFile raFile = new RandomAccessFile(dbLocation,
					"rw");
			final int requestedRecordIndex = firstRecordIndex + recNo
					* singleRecordByteSize;
			final byte[] fullRecordByteArray = new byte[singleRecordByteSize];

			raFile.seek(requestedRecordIndex);

			final long isValidRecord = raFile.readByte();
			if (isValidRecord != 0) {
				raFile.close();
				throw new RecordNotFoundException(
						"Record does not exist, or has been deleted");
			}

			raFile.read(fullRecordByteArray);
			raFile.close();

			final String[] resultArray = constructReadableStringArray(fullRecordByteArray);
			
			return resultArray;

		} catch (final Exception e) {
			throw new RecordNotFoundException("Record " + recNo + " not found. " + e.getMessage());
		}

	}

	public void update(final int recNo, final String[] data,
			final long lockCookie) throws RecordNotFoundException,
			SecurityException {

		try {
			lockingManager.verifyCookie(recNo, lockCookie);
			final RandomAccessFile raFile = new RandomAccessFile(dbLocation,
					"rw");

			final int requestedRecordIndex = firstRecordNameIndex + recNo
					* singleRecordByteSize;

			final byte[] writableBytes = constructWritableByteArray(data);

			final long isValidRecord = raFile.readByte();
			if (isValidRecord != 0) {
				raFile.close();
				throw new RecordNotFoundException(
						"Record does not exist, or has been deleted");
			}

			raFile.seek(requestedRecordIndex);
			raFile.write(writableBytes);
			raFile.close();
			
			for (int i = 0; i < data.length; i++) {
				System.out.print(data[i].trim() + " - ");
			}

		} catch (final FileNotFoundException fnfe) {
			System.out.println(fnfe);
		} catch (final EOFException eofe) {
			throw new RecordNotFoundException("There is no record " + recNo);
		} catch (final IOException ioe) {
			System.out.println(ioe);
		}

	}

	public void delete(final int recNo, final long lockCookie)
			throws RecordNotFoundException, SecurityException {
		try {
			lockingManager.verifyCookie(recNo, lockCookie);
			final RandomAccessFile raFile = new RandomAccessFile(dbLocation,
					"rw");

			final int requestedRecordIndex = firstRecordIndex + recNo
					* singleRecordByteSize;

			raFile.seek(requestedRecordIndex);

			final long isValidRecord = raFile.readByte();
			if (isValidRecord != 0) {
				raFile.close();
				throw new RecordNotFoundException(
						"Record does not exist, or has been deleted");
			} else {
				raFile.seek(requestedRecordIndex);
				raFile.writeByte(1);
				raFile.close();
			}
		} catch (final FileNotFoundException fnfe) {
			System.out.println(fnfe);
		} catch (final EOFException eofe) {
			throw new RecordNotFoundException("There is no record " + recNo);
		} catch (final IOException ioe) {
			System.out.println(ioe);
		}
	}
	
	public int[] find(String[] criteria) {
		final List<Integer> resultList = new ArrayList<Integer>();
		try{
			for(int i = 0;;i++){
				boolean isMatch = true;
				
				final String[] entry = read(i);
				
				for (int j = 0; j<criteria.length; j++){
					
					if(criteria[j] != null){
						if(!entry[j].startsWith(criteria[j])){
							isMatch = false;
							continue;
						}
					}
				}
				
				if(isMatch){
					resultList.add(i);
				}
				
			}
		}catch(RecordNotFoundException rnfe){
			
		}
		
		final int[] results = new int[resultList.size()];
		for(int i = 0; i<resultList.size(); i++){
			results[i] = resultList.get(i);
		}

		return results;

	}

	public int create(final String[] data) throws DuplicateKeyException {
		try {
			final long writeIndex = getFirstWritableIndex();

			final RandomAccessFile raFile = new RandomAccessFile(dbLocation,
					"rw");
			final byte[] writeableBytes = constructWritableByteArray(data);

			raFile.seek(writeIndex);
			raFile.writeByte(0);
			raFile.write(writeableBytes);
			raFile.close();
			
			for (int i = 0; i < data.length; i++) {
				System.out.print(Thread.currentThread().getId() + " - We just created" + data[i].trim() + " - ");
			}
			System.out.println();
			return (int) ((writeIndex - firstRecordIndex)/160);
		} catch (final FileNotFoundException fnfe) {
			System.out.println(fnfe);
		} catch (final IOException ioe) {
			System.out.println(ioe);
		}
		
		return -1;
	}

	private byte[] constructWritableByteArray(final String[] data) {
		String finalString = "";

		for (int i = 0; i < data.length; i++) {
			String fieldString = data[i];
			System.out.println(fieldString + " and " + recordEntrySizes[i]);
			if(fieldString.length() > recordEntrySizes[i]){
				finalString = finalString + fieldString.substring(0, recordEntrySizes[i]);
			}else if(fieldString.length() < recordEntrySizes[i]){
				final int fieldSizeOffset = recordEntrySizes[i]
						- fieldString.length();
				final String paddedFieldString = fieldString
						+ whitespaceString.substring(0, fieldSizeOffset);
				finalString = finalString + paddedFieldString;
			}else{
				finalString = finalString + fieldString;
			}
		}
		
		System.out.println("FINAL STRING : " + finalString);

		return finalString.getBytes();
	}

	private String[] constructReadableStringArray(final byte[] data)
			throws UnsupportedEncodingException {
		final String[] resultsArray = new String[numberOfFieldsInRecord];
		final String entireRow = new String(data, "US-ASCII");

		for (int i = 0; i < numberOfFieldsInRecord; i++) {
			resultsArray[i] = entireRow.substring(recordEntryPositions[i],
					recordEntryPositions[i + 1]);
		}

		return resultsArray;
	}

	private long getFirstWritableIndex() throws IOException {
		final RandomAccessFile raFile = new RandomAccessFile(dbLocation, "rw");

		try {
			for (int i = firstRecordIndex; i < raFile.length(); i = i
					+ singleRecordByteSize) {
				raFile.seek(i);
				final long isValidRecord = raFile.readByte();
				if (isValidRecord != 0) {
					final long writeLocation = raFile.getFilePointer() - 1;
					raFile.close();
					return writeLocation;
				}
			}
		} catch (final EOFException eofe) {
			final long writeLocation = raFile.getFilePointer() - 1;
			raFile.close();
			return writeLocation;
		}

		final long writeLocation = raFile.getFilePointer() -1 + singleRecordByteSize;
		raFile.close();
		return writeLocation;
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
