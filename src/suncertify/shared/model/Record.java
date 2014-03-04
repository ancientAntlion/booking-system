package suncertify.shared.model;

import java.io.Serializable;

/**
 * A domain object which holds a String array containing a single records data and a recordNumber
 * 
 * This exists in order to inform the client of where each record sits in the database
 * 
 * @author Aaron
 */
public class Record implements Serializable {

	private static final long serialVersionUID = 8032934961324824045L;
	
	private int recordNumber;
	
	private String[] recordData;
	
	/**
	 * @param recordData
	 * @param recordNumber
	 */
	public Record(final String[] recordData,final int recordNumber){
		this.setRecordData(recordData);
		this.setRecordNumber(recordNumber);
	}

	/**
	 * @return
	 */
	public String[] getRecordData() {
		return recordData;
	}
	
	/**
	 * @param recordData
	 */
	public void setRecordData(final String[] recordData) {
		this.recordData = recordData;
	}
	
	/**
	 * @return
	 */
	public int getRecordNumber() {
		return recordNumber;
	}

	/**
	 * @param recordNumber
	 */
	public void setRecordNumber(int recordNumber) {
		this.recordNumber = recordNumber;
	}	
	
}

