package suncertify.server;

import java.util.List;

import suncertify.server.exceptions.BookingServiceException;
import suncertify.shared.model.Record;

public interface LocalBookingService extends BookingService {

	public void book(final int recNo, final String customerID) throws BookingServiceException;
	
	public void unbook(final int recNo) throws BookingServiceException;
		
	public List<Record> find(final String name, final String location) throws BookingServiceException;
	
}