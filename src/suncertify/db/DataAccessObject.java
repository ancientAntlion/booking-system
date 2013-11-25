package suncertify.db;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class DataAccessObject {
	
	private final String fileLocation = "c:\\Users\\Aaron\\workspace\\URLybird\\src\\suncertify\\db\\db-1x3.db";
	
	private final String whitespaceString = "                                                                ";
	
	private final int singleRecordByteSize = 160;
	
	private final int[] recordEntrySizes = {64,64,4,1,8,10,8};
	
	private final int[] recordEntryPositions = {0,64,128,132,133,141,151,159};
	
	private final int uniqueKeySize = recordEntrySizes[0] + recordEntrySizes[1];

	private final int firstRecordIndex = 56;
	
	private final int firstRecordNameIndex = firstRecordIndex+1;
	
	private final int numberOfFieldsInRecord = 7;

	public String[] read(int recNo) throws RecordNotFoundException {
		
		try{
			final RandomAccessFile raFile = new RandomAccessFile(fileLocation, "rw");
			final int requestedRecordIndex = firstRecordIndex + (recNo*singleRecordByteSize);
			byte[] fullRecordByteArray = new byte[singleRecordByteSize];
			
			// TODO Place lock on records
			raFile.seek(requestedRecordIndex);
			
			long isValidRecord = (long)raFile.readByte();
			if(isValidRecord!=0){
				raFile.close();
				// TODO Unlock records
				throw new RecordNotFoundException("Record does not exist, or has been deleted");
			}
			
			raFile.read(fullRecordByteArray);
			raFile.close();
			
			// TODO Remove lock on records
			
			final String[] resultArray = constructReadableStringArray(fullRecordByteArray);
			
			System.out.println("LOGGER : Reading from index : " + requestedRecordIndex);
			System.out.println("LOGGER : Results : ");
			for(int i = 0; i< resultArray.length; i++){
				System.out.print(resultArray[i].trim() + " - ");
			}
					
			return resultArray;
			
		}catch(FileNotFoundException fnfe){
			System.out.println(fnfe);
		}catch(EOFException eofe){
			throw new RecordNotFoundException("There is no record " + recNo);
		}catch(UnsupportedEncodingException uee){
			System.out.println(uee);
		}catch(IOException io){
			System.out.println(io);
		}
		
		return null;
		
	}
	
	public void update(int recNo, String[] data, long lockCookie) throws RecordNotFoundException, SecurityException {
		
		try{
			final RandomAccessFile raFile = new RandomAccessFile(fileLocation, "rw");
			
			final int requestedRecordIndex = firstRecordNameIndex + (recNo*singleRecordByteSize);
			
			byte[] writableBytes = constructWritableByteArray(data);
			
			long isValidRecord = (long)raFile.readByte();
			if(isValidRecord!=0){
				raFile.close();
				// TODO Unlock records
				throw new RecordNotFoundException("Record does not exist, or has been deleted");
			}
			
			// TODO Place lock on records
			raFile.seek(requestedRecordIndex);
			raFile.write(writableBytes);
			raFile.close();
			
			// TODO Remove lock on records
			
			System.out.println("LOGGER : Writing to index : " + requestedRecordIndex);
			System.out.println("LOGGER : Results : ");
			for(int i = 0; i< data.length; i++){
				System.out.print(data[i].trim() + " - ");
			}
			
		}catch(FileNotFoundException fnfe){
			System.out.println(fnfe);
		}catch(EOFException eofe){
			throw new RecordNotFoundException("There is no record " + recNo);
		}catch(IOException ioe){
			System.out.println(ioe);
		}
		
	}
	
	public void delete(int recNo, long lockCookie)
		    throws RecordNotFoundException, SecurityException{
		try{
			final RandomAccessFile raFile = new RandomAccessFile(fileLocation, "rw");
			
			final int requestedRecordIndex = firstRecordIndex + (recNo*singleRecordByteSize);
						
			// TODO Place lock on records
			raFile.seek(requestedRecordIndex);

			long isValidRecord = (long)raFile.readByte();
			if(isValidRecord!=0){
				raFile.close();
				// TODO Unlock records
				throw new RecordNotFoundException("Record does not exist, or has been deleted");
			}else{
				raFile.seek(requestedRecordIndex);
				raFile.writeByte(1);
				raFile.close();
				// TODO Unlock records
			}
			
			System.out.println("LOGGER : Deleting at index : " + recNo);
			
		}catch(FileNotFoundException fnfe){
			System.out.println(fnfe);
		}catch(EOFException eofe){
			throw new RecordNotFoundException("There is no record " + recNo);
		}catch(IOException ioe){
			System.out.println(ioe);
		}
	}
	
	
	public void create(String[] data) throws DuplicateKeyException{
		try{
			if(isDuplicateKey(data[0].trim() + "-" + data[1].trim())){
				throw new DuplicateKeyException("Entry with name : " + data[0]
						+ "and location : "+ data[1] + " already exists");
			}
			final long writeIndex = getFirstWritableIndex();
			
			final RandomAccessFile raFile = new RandomAccessFile(fileLocation, "rw");
			final byte[] writeableBytes = constructWritableByteArray(data);
						
			raFile.seek(writeIndex);
			raFile.writeByte(0);
			raFile.write(writeableBytes);
			raFile.close();

			// TODO Remove lock on records
			
			System.out.println("LOGGER : Creating at index : " + writeIndex);
			System.out.println("LOGGER : Results : ");
			for(int i = 0; i< data.length; i++){
				System.out.print(data[i].trim() + " - ");
			}
		}catch(FileNotFoundException fnfe){
			System.out.println(fnfe);
		}catch(IOException ioe){
			System.out.println(ioe);
		}
	}
	
	private byte[] constructWritableByteArray(String[] data){
		String finalString = "";
		
		for(int i=0;i<data .length;i++){
			final String fieldString = data[i];
			int fieldSizeOffset = recordEntrySizes[i] - fieldString.length();
			final String paddedFieldString = fieldString + whitespaceString.substring(0, fieldSizeOffset);
			finalString = finalString + paddedFieldString;
		}
		
		return finalString.getBytes();
	}
	
	private String[] constructReadableStringArray(byte[] data) throws UnsupportedEncodingException{
		final String[] resultsArray = new String[numberOfFieldsInRecord];
		final String entireRow = new String(data, "US-ASCII");
		
		for(int i = 0; i < numberOfFieldsInRecord; i++){
			resultsArray[i] = entireRow.substring(recordEntryPositions[i], recordEntryPositions[i+1]);
		}
		
		return resultsArray;
	}
	
	private long getFirstWritableIndex() throws IOException{
		final RandomAccessFile raFile = new RandomAccessFile(fileLocation, "rw");
		
		try{
			for(int i = firstRecordIndex; i<raFile.length(); i=i+singleRecordByteSize){
				raFile.seek(i);
				long isValidRecord = (long)raFile.readByte();
				if(isValidRecord!=0){
					final long writeLocation = raFile.getFilePointer()-1;
					raFile.close();
					return writeLocation;
				}
			}
		}catch(EOFException eofe){
			final long writeLocation = raFile.getFilePointer()-1;
			raFile.close();
			return writeLocation;
		}
		
		final long writeLocation = raFile.getFilePointer()-1;
		raFile.close();
		return writeLocation;
	}
	
	private boolean isDuplicateKey(String nameAddress) throws IOException{
		final RandomAccessFile raFile = new RandomAccessFile(fileLocation, "rw");
		byte[] uniqueNameByteArray = new byte[uniqueKeySize/2];
		byte[] uniqueLocationByteArray = new byte[uniqueKeySize/2];
		
		for(int i = firstRecordIndex; i<raFile.length(); i=i+singleRecordByteSize){
			raFile.seek(i);
			long isValidRecord = (long)raFile.readByte();
			if(isValidRecord==0){
				raFile.read(uniqueNameByteArray);
				raFile.read(uniqueLocationByteArray);
				final String name = new String(uniqueNameByteArray, "US-ASCII").trim();
				final String location = new String(uniqueLocationByteArray, "US-ASCII").trim();
				final String uniqueKey = name + "-" + location;
				if(uniqueKey.equals(nameAddress)){
					return true;
				}
			}
		}
		
		return false;

	}
		
}
