package suncertify.shared.model;

import java.io.Serializable;

public class Record implements Serializable {
	
	private int recordNumber;
	private String[] recordData;
	
	public Record(final String[] recordData,final int recordNumber){
		this.setRecordData(recordData);
		this.setRecordNumber(recordNumber);
	}

	public String[] getRecordData() {
		return recordData;
	}
	
	public void setRecordData(final String[] recordData) {
		this.recordData = recordData;
	}
	
	public int getRecordNumber() {
		return recordNumber;
	}

	public void setRecordNumber(int recordNumber) {
		this.recordNumber = recordNumber;
	}	
	
}

