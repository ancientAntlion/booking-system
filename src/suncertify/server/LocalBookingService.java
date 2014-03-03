package suncertify.server;

import java.util.List;

import suncertify.server.exceptions.BookingServiceException;
import suncertify.shared.model.Record;

public interface LocalBookingService extends BookingService {

	/* (non-Javadoc)
	 * @see suncertify.server.BookingService#book(int, java.lang.String)
	 */
	public void book(final int recNo, final String customerID) throws BookingServiceException;
	
	/* (non-Javadoc)
	 * @see suncertify.server.BookingService#unbook(int)
	 */
	public void unbook(final int recNo) throws BookingServiceException;
		
	/* (non-Javadoc)
	 * @see suncertify.server.BookingService#find(java.lang.String, java.lang.String)
	 */
	public List<Record> find(final String name, final String location) throws BookingServiceException;
	
}