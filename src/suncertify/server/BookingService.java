package suncertify.server;

import java.rmi.RemoteException;
import java.util.List;

import suncertify.server.exceptions.BookingServiceException;
import suncertify.shared.model.Record;

/**
 * @author Aaron
 *
 */
public interface BookingService {
	
	public static String customerFieldWhiteSpace = "        ";
	
	/**
	 * @param recNo
	 * @param customerID
	 * @throws BookingServiceException
	 * @throws RemoteException
	 */
	public void book(final int recNo, final String customerID) throws BookingServiceException, RemoteException;
	
	/**
	 * @param recNo
	 * @throws BookingServiceException
	 * @throws RemoteException
	 */
	public void unbook(final int recNo) throws BookingServiceException, RemoteException;
		
	/**
	 * @param name
	 * @param location
	 * @return
	 * @throws BookingServiceException
	 * @throws RemoteException
	 */
	public List<Record> find(final String name, final String location) throws BookingServiceException, RemoteException;

}