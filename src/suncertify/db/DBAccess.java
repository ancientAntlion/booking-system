package suncertify.db;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to interact with the Database on the physical file system.
 * This class should exists to separate the physical disk operations from
 * The business logic elsewhere in the program.
 * 
 * The actual RAF is private static, and anytime it is used is synchronized
 * to ensure consistency with the file pointer during reads/writes
 * 
 * @author Aaron
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
	 * Class initializes and we verify the file exists
	 * 
	 * @param dbLocation
	 * @throws DatabaseInitializationException
	 */
	public DBAccess(final String dbLocation) throws DatabaseInitializationException {
		try {
			File dbFile = new File(dbLocation);
			if(dbFile.exists() && !dbFile.isDirectory()) {
				DBAccess.databaseFile = new RandomAccessFile(dbFile, "rw");
			}else{
				throw new DatabaseInitializationException("Selected file does not exist or is a directory");
			}
		} catch (DatabaseInitializationException die) {
			throw die;
		} catch (Exception e) {
			throw new DatabaseInitializationException("Exception encountered while initializing the database : " + e.getMessage());
		}
	}
	
	/**
	 * Returns a string array containing a single record. We find the record by first getting the index
	 * Of the requested record. We do this by multiplying the set record byte size and multiplying it 
	 * by the record number. This puts the RAF pointer at the start of the desired record. After verifying
	 * That the byte deletion flag is not set we read it from there. Otherwise we throw a RecordNotFoundException
	 * 
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
	 * Updates a single record. We find the record by first getting the index of the requested record.
	 * We do this by multiplying the set record byte size and multiplying it by the record number.
	 * This puts the RAF pointer at the start of the desired record. After verifying that the byte
	 * Deletion flag is not set we update it from there. Otherwise we throw a RecordNotFoundException
	 * 
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
	 * Deletes a single record. We find the record by first getting the index of the requested record.
	 * We do this by multiplying the set record byte size and multiplying it by the record number.
	 * This puts the RAF pointer at the start of the desired record. We change 1 byte which flags the
	 * record as null. This block in the file now is ready to be overwritten.
	 * 
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
	 * Returns an array of record numbers that match the supplied criteria. We use the getRecord()
	 * method in a loop to pull out every record in the DB. For each record retrieved we check if
	 * The criteria supplied matches the criteria in the DB. This is done as per the instructions.
	 * 
	 * "Returns an array of record numbers that match the specified
	 * criteria. Field n in the database file is described by
	 * criteria[n]. A null value in criteria[n] matches any field
	 * value. A non-null  value in criteria[n] matches any field
	 * value that begins with criteria[n]. (For example, "Fred"
	 * matches "Fred" or "Freddy".)
	 * 
	 * @param criteria
	 * @return recordNumbers
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
	 * Creates a single record in the DB and returns the index of that record. We use the
	 * constructWritableByteArray(String[]) method to construct the byte array, then we use 
	 * the getFirstWritableIndex() method to determine where to place our file pointer is.
	 * Then we write the data to the file.
	 * 
	 * I chose not to throw the DuplicateKeyException for 2 reasons. I explain my reasons
	 * In my choices.txt
	 * 
	 * @param data
	 * @return recordNumber
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
	 * Converts a String array into a byte array
	 * 
	 * @param data
	 * @return dataBytes
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
	 * Converts a byte array into a String array
	 * 
	 * @param dataBytes
	 * @return data
	 * @throws UnsupportedEncodingException
	 */
	private String[] constructReadableStringArray(final byte[] dataBytes)
			throws UnsupportedEncodingException {
		final String[] resultsArray = new String[numberOfFieldsInRecord];
		final String entireRow = new String(dataBytes, "US-ASCII");

		for (int i = 0; i < numberOfFieldsInRecord; i++) {
			resultsArray[i] = entireRow.substring(recordEntryPositions[i],
					recordEntryPositions[i + 1]);
		}

		return resultsArray;
	}

	/**
	 * Locates the first point in the DB that we can write a record. This involves
	 * Reading the first byte of each record to check if the delete flag is set.
	 * If none of the records are flagged as deleted then we return a write location
	 * At the very end of the file
	 * 
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
