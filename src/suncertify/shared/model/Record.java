package suncertify.shared.model;

import java.io.Serializable;

/**
 * @author Aaron
 *
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

