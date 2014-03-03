package suncertify.server;

import java.rmi.RemoteException;
import java.util.List;

import suncertify.server.exceptions.BookingServiceException;

public interface BookingService {
	
	public static String customerFieldWhiteSpace = "        ";
	
	public void book(final int recNo, final String customerID) throws BookingServiceException, RemoteException;
	
	public void unbook(final int recNo) throws BookingServiceException, RemoteException;
		
	public List<String[]> find(final String name, final String location) throws BookingServiceException, RemoteException;

}