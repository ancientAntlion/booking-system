package suncertify.shared.model;

public class Record {
	
	private int recordNumber;
	private String hotelName;
	private String hotelLocation;
	private String roomSize;
	private String isSmokingRoom;
	private String roomRate;
	private String arrivalDate;
	private String customerID;
	
	public Record(final String hotelName, final String hotelLocation,
			final String roomSize, final String smokingRoom, final String roomRate, final String arrivalDate,
			final String customerID, final int recordNumber){
		this.setHotelName(hotelName);
		this.setHotelLocation(hotelLocation);
		this.setRoomSize(roomSize);
		this.setSmokingRoom(smokingRoom);
		this.setRoomRate(roomRate);
		this.setArrivalDate(arrivalDate);
		this.setCustomerID(customerID);
		this.setRecordNumber(recordNumber);
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

	public String getRoomSize() {
		return roomSize;
	}

	public void setRoomSize(String roomSize) {
		this.roomSize = roomSize;
	}

	public String isSmokingRoom() {
		return isSmokingRoom;
	}

	public void setSmokingRoom(String smokingRoom) {
		this.isSmokingRoom = smokingRoom;
	}

	public String getRoomRate() {
		return roomRate;
	}

	public void setRoomRate(String roomRate) {
		this.roomRate = roomRate;
	}

	public String getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}	
	
	public int getRecordNumber() {
		return recordNumber;
	}

	public void setRecordNumber(int recordNumber) {
		this.recordNumber = recordNumber;
	}	
	
}

