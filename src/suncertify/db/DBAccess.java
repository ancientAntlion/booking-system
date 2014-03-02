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

public class DBAccess {


	private final String whitespaceString = "                                                                ";
	
	private final int singleRecordByteSize = 160;

	private final int[] recordEntrySizes = { 64, 64, 4, 1, 8, 10, 8 };

	private final int[] recordEntryPositions = { 0, 64, 128, 132, 133, 141,
			151, 159 };

	private final int firstRecordIndex = 56;

	private final int firstRecordNameIndex = firstRecordIndex + 1;

	private final int numberOfFieldsInRecord = 7;
	
	private final RandomAccessFile databaseFile;
		
	public DBAccess(final String dbLocation) throws DatabaseInitializationException {
		try {
			databaseFile = new RandomAccessFile(dbLocation, "rw");
		} catch (Exception e) {
			throw new DatabaseInitializationException("Exception encountered while initializing the database : " + e.getMessage());
		}
	}
	
	public String[] getRecord(final int recNo) throws RecordNotFoundException {

		try {
			final int requestedRecordIndex = firstRecordIndex + recNo
					* singleRecordByteSize;
			final byte[] fullRecordByteArray = new byte[singleRecordByteSize];

			databaseFile.seek(requestedRecordIndex);

			final long isValidRecord = databaseFile.readByte();
			if (isValidRecord != 0) {
				throw new RecordNotFoundException("Record " + recNo + " not found");
			}

			databaseFile.read(fullRecordByteArray);

			final String[] resultArray = constructReadableStringArray(fullRecordByteArray);
			
			return resultArray;

		} catch (final Exception e) {
			throw new RecordNotFoundException("Record " + recNo + " not found");
		}

	}

	public void update(final int recNo, final String[] data,
			final long lockCookie) throws RecordNotFoundException,
			SecurityException {

		try {
			final int requestedRecordIndex = firstRecordNameIndex + recNo
					* singleRecordByteSize;

			final byte[] writableBytes = constructWritableByteArray(data);

			final long isValidRecord = databaseFile.readByte();
			if (isValidRecord != 0) {
				throw new RecordNotFoundException("Record " + recNo + " not found");
			}

			databaseFile.seek(requestedRecordIndex);
			databaseFile.write(writableBytes);
			
		} catch (final Exception e) {
			throw new RecordNotFoundException("Record " + recNo + " not found. " + e.getMessage());
		} 

	}

	public void delete(final int recNo, final long lockCookie)
			throws RecordNotFoundException, SecurityException {
		try {
			final int requestedRecordIndex = firstRecordIndex + recNo
					* singleRecordByteSize;

			databaseFile.seek(requestedRecordIndex);

			final long isValidRecord = databaseFile.readByte();
			if (isValidRecord != 0) {
				throw new RecordNotFoundException("Record " + recNo + " not found.");
			} else {
				databaseFile.seek(requestedRecordIndex);
				databaseFile.writeByte(1);
			}
		} catch (final Exception e) {
			throw new RecordNotFoundException("Record " + recNo + " not found. " + e.getMessage());
		}
	}
	
	public int[] find(String[] criteria) {
		final List<Integer> resultList = new ArrayList<Integer>();
		try{
			for(int i = 0;;i++){
				boolean isMatch = true;
				
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

			final byte[] writeableBytes = constructWritableByteArray(data);

			databaseFile.seek(writeIndex);
			databaseFile.writeByte(0);
			databaseFile.write(writeableBytes);
			
			return (int) ((writeIndex - firstRecordIndex)/160);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		} 
		
	}

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
		try {
			for (int i = firstRecordIndex; i < databaseFile.length(); i = i
					+ singleRecordByteSize) {
				databaseFile.seek(i);
				final long isValidRecord = databaseFile.readByte();
				if (isValidRecord != 0) {
					final long writeLocation = databaseFile.getFilePointer() - 1;
					return writeLocation;
				}
			}
		} catch (final EOFException eofe) {
			final long writeLocation = databaseFile.getFilePointer() - 1;
			return writeLocation;
		}

		final long writeLocation = databaseFile.getFilePointer() -1 + singleRecordByteSize;
		return writeLocation;
	}
	
}
