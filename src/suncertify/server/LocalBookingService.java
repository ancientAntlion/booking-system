package suncertify.server;

import java.util.List;

import suncertify.server.exceptions.BookingServiceException;
import suncertify.shared.model.Record;

/**
 * Interface used when local mode is selected. Identical to BookingService interface except
 * This interface does not throw RemoteExceptions
 * 
 * @author Aaron
 *
 */
public interface LocalBookingService extends BookingService {

	/**
	 * Books a record with the supplied ID
	 * 
	 * @param recNo
	 * @param customerID
	 * @throws BookingServiceException
	 */
	public void book(final int recNo, final String customerID) throws BookingServiceException;
	
	/**
	 * Unbooks a record
	 * 
	 * @param recNo
	 * @throws BookingServiceException
	 */
	public void unbook(final int recNo) throws BookingServiceException;
		
	/**
	 * Gets a list of records that start with or equal to the supplied name and location.
	 * If a null value for name or location will match any field value
	 * 
	 * @param name
	 * @param location
	 * @return recordList
	 * @throws BookingServiceException
	 */
	public List<Record> find(final String name, final String location) throws BookingServiceException;
	
}
