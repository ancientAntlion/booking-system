package suncertify.server;

import java.util.List;

import suncertify.server.exceptions.BookingServiceException;

public interface LocalBookingService extends BookingService {

	public void book(final int recNo, final String customerID) throws BookingServiceException;
	
	public void unbook(final int recNo) throws BookingServiceException;
	
	public List<String[]> findAll() throws BookingServiceException;
	
	public List<String[]> find(final String name, final String location) throws BookingServiceException;
	
}