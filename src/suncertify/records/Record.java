package suncertify.records;

import java.util.Date;

public class Record {
	
	private boolean isValidRecord;
	private String hotelName;
	private String hotelLocation;
	private int roomSize;
	private boolean isSmokingRoom;
	private double roomRate;
	private Date arrivalDate;
	private String customerID;
	
	public Record(final boolean isValidRecord, final String hotelName, final String hotelLocation,
			final int roomSize, final boolean smokingRoom, final double roomRate, final Date arrivalDate,
			final String customerID){
		this.setValidRecord(isValidRecord);
		this.setHotelName(hotelName);
		this.setHotelLocation(hotelLocation);
		this.setRoomSize(roomSize);
		this.setSmokingRoom(smokingRoom);
		this.setRoomRate(roomRate);
		this.setArrivalDate(arrivalDate);
		this.setCustomerID(customerID);		
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getHotelLocation() {
		return hotelLocation;
	}

	public void setHotelLocation(String hotelLocation) {
		this.hotelLocation = hotelLocation;
	}

	public int getRoomSize() {
		return roomSize;
	}

	public void setRoomSize(int roomSize) {
		this.roomSize = roomSize;
	}

	public boolean isSmokingRoom() {
		return isSmokingRoom;
	}

	public void setSmokingRoom(boolean smokingRoom) {
		this.isSmokingRoom = smokingRoom;
	}

	public double getRoomRate() {
		return roomRate;
	}

	public void setRoomRate(double roomRate) {
		this.roomRate = roomRate;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public boolean isValidRecord() {
		return isValidRecord;
	}

	public void setValidRecord(boolean isValidRecord) {
		this.isValidRecord = isValidRecord;
	}
	
	
	
}

