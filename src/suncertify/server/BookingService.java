package suncertify.server;

import java.rmi.RemoteException;
import java.util.List;

import suncertify.server.exceptions.BookingServiceException;
import suncertify.shared.model.Record;

/**
 * Interface to the BookingService. This is the highest level server interface. It defines methods with
 * Throws clauses for BookingServiceExceptions and RemoteExceptions to accommodate both local and remote
 * Booking service interfaces.
 * 
 * @author Aaron
 *
 */
public interface BookingService {
	
	/**
	 * White space string 8 characters long used for filling in the customer id field when unbooking
	 */
	public static String customerFieldWhiteSpace = "        ";
	
	/**
	 * Books a record with the supplied ID
	 * 
	 * @param recNo
	 * @param customerID
	 * @throws BookingServiceException
	 * @throws RemoteException
	 */
	public void book(final int recNo, final String customerID) throws BookingServiceException, RemoteException;
	
	/**
	 * Unbooks a record
	 * 
	 * @param recNo
	 * @throws BookingServiceException
	 * @throws RemoteException
	 */
	public void unbook(final int recNo) throws BookingServiceException, RemoteException;
		
	/**
	 * Gets a list of records that start with or equal to the supplied name and location.
	 * If a null value for name or location will match any field value
	 * 
	 * @param name
	 * @param location
	 * @return recordList
	 * @throws BookingServiceException
	 * @throws RemoteException
	 */
	public List<Record> find(final String name, final String location) throws BookingServiceException, RemoteException;

}