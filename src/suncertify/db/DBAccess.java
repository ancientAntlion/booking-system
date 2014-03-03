package suncertify.db;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import suncertify.db.exceptions.DatabaseInitializationException;
import suncertify.db.exceptions.DuplicateKeyException;
import suncertify.db.exceptions.RecordNotFoundException;
import suncertify.db.exceptions.SecurityException;

/**
 * @author Aaron
 * 
 * Used to interact with the Database on the physical file system.
 * This class should exists to separate the physical disk operations from
 * The business logic elsewhere in the program.
 */
public class DBAccess {
	
	/**
	 * White space string 160 characters long used to "pad" records to their appropriate sizes
	 */
	public static String whitespaceString = "                                                                ";
	
	public static int singleRecordByteSize = 160;

	public static int[] recordEntrySizes = { 64, 64, 4, 1, 8, 10, 8 };

	public static int[] recordEntryPositions = { 0, 64, 128, 132, 133, 141,
			151, 159 };

	public static int firstRecordIndex = 56;

	public static int firstRecordNameIndex = firstRecordIndex + 1;

	public static int numberOfFieldsInRecord = 7;
	
	private static RandomAccessFile databaseFile;
		
	/**
	 * @param dbLocation
	 * @throws DatabaseInitializationException
	 */
	public DBAccess(final String dbLocation) throws DatabaseInitializationException {
		try {
			DBAccess.databaseFile = new RandomAccessFile(dbLocation, "rw");
		} catch (Exception e) {
			throw new DatabaseInitializationException("Exception encountered while initializing the database : " + e.getMessage());
		}
	}
	
	/**
	 * @param recNo
	 * @return
	 * @throws RecordNotFoundException
	 * @throws EOFException
	 */
	public String[] getRecord(final int recNo) throws RecordNotFoundException, EOFException {

		try {
			final int requestedRecordIndex = firstRecordIndex + recNo
					* singleRecordByteSize;
			final byte[] fullRecordByteArray = new byte[singleRecordByteSize];
			
			synchronized(DBAccess.databaseFile){

				DBAccess.databaseFile.seek(requestedRecordIndex);
	
				final long isValidRecord = DBAccess.databaseFile.readByte();
				if (isValidRecord != 0) {
					throw new RecordNotFoundException("Record " + recNo + " not found");
				}
	
				DBAccess.databaseFile.read(fullRecordByteArray);
			
			}

			final String[] resultArray = constructReadableStringArray(fullRecordByteArray);
			
			return resultArray;

		} catch (final EOFException eofe) {
			throw eofe;
		} catch (final Exception e) {
			throw new RecordNotFoundException("Record " + recNo + " not found");
		}

	}

	/**
	 * @param recNo
	 * @param data
	 * @throws RecordNotFoundException
	 */
	public void update(final int recNo, final String[] data) throws RecordNotFoundException{

		try {
			final int requestedRecordIndex = firstRecordNameIndex + recNo
					* singleRecordByteSize;

			final byte[] writableBytes = constructWritableByteArray(data);
			
			synchronized(DBAccess.databaseFile){

				DBAccess.databaseFile.seek(requestedRecordIndex-1);
				
				final long isValidRecord = DBAccess.databaseFile.readByte();
				if (isValidRecord != 0) {
					throw new RecordNotFoundException("Record " + recNo + " not found");
				}
	
				DBAccess.databaseFile.seek(requestedRecordIndex);
				DBAccess.databaseFile.write(writableBytes);
				
			}
			
		} catch (final RecordNotFoundException rnfe) {
			throw rnfe;
		} catch (final Exception e) {
			throw new RecordNotFoundException("Record " + recNo + " not found. " + e.getMessage());
		} 

	}

	/**
	 * @param recNo
	 * @throws RecordNotFoundException
	 * @throws SecurityException
	 */
	public void delete(final int recNo)
			throws RecordNotFoundException, SecurityException {
		try {
			final int requestedRecordIndex = firstRecordIndex + recNo
					* singleRecordByteSize;
			
			synchronized(DBAccess.databaseFile){

				DBAccess.databaseFile.seek(requestedRecordIndex);
	
				final long isValidRecord = DBAccess.databaseFile.readByte();
				if (isValidRecord != 0) {
					throw new RecordNotFoundException("Record " + recNo + " not found.");
				} else {
					DBAccess.databaseFile.seek(requestedRecordIndex);
					DBAccess.databaseFile.writeByte(1);
				}
			
			}
		} catch (final Exception e) {
			throw new RecordNotFoundException("Record " + recNo + " not found. " + e.getMessage());
		}
	}
	
	/**
	 * @param criteria
	 * @return
	 */
	public int[] find(String[] criteria) {
		final List<Integer> resultList = new ArrayList<Integer>();
			for(int i = 0;;i++){
				boolean isMatch = true;
				
				try{
					final String[] entry = getRecord(i);
					
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
				}catch(RecordNotFoundException rnfe){
					//We can ignore this and continue.
					//We only want to break the loop when we reach the end of the DB file
				}catch(EOFException eofe){
					//Finished reading from file now, results are all stored in resultList
					break;
				}
				
			}
		
		
		final int[] results = new int[resultList.size()];
		for(int i = 0; i<resultList.size(); i++){
			results[i] = resultList.get(i);
		}

		return results;

	}

	/**
	 * @param data
	 * @return
	 * @throws DuplicateKeyException
	 */
	public int create(final String[] data) throws DuplicateKeyException {
		try {
			
			final byte[] writeableBytes = constructWritableByteArray(data);
						
			synchronized(DBAccess.databaseFile){
				final long writeIndex = getFirstWritableIndex();

				DBAccess.databaseFile.seek(writeIndex);
				DBAccess.databaseFile.writeByte(0);
				DBAccess.databaseFile.write(writeableBytes);
			
				return (int) ((writeIndex - firstRecordIndex)/160);
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		} 
		
	}

	/**
	 * @param data
	 * @return
	 */
	private byte[] constructWritableByteArray(final String[] data) {
		String finalString = "";

		for (int i = 0; i < data.length; i++) {
			String fieldString = data[i];
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

		return finalString.getBytes();
	}

	/**
	 * @param data
	 * @return
	 * @throws UnsupportedEncodingException
	 */
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

	/**
	 * @return
	 * @throws IOException
	 */
	private long getFirstWritableIndex() throws IOException {
		try {
			for (int i = firstRecordIndex; i < DBAccess.databaseFile.length(); i = i
					+ singleRecordByteSize) {
				DBAccess.databaseFile.seek(i);
				final long isValidRecord = DBAccess.databaseFile.readByte();
				if (isValidRecord != 0) {
					final long writeLocation = DBAccess.databaseFile.getFilePointer() - 1;
					return writeLocation;
				}
			}
		} catch (final EOFException eofe) {
			final long writeLocation = DBAccess.databaseFile.getFilePointer() - 1;
			return writeLocation;
		}

		final long writeLocation = DBAccess.databaseFile.getFilePointer() -1 + singleRecordByteSize;
		return writeLocation;
	}
	
}
