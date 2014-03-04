package suncertify.server.exceptions;

/**
 * This is the generic booking exception that is thrown back to the client. 
 * All server side exceptions are converted into a BookingServiceException and
 * Thrown back to clients
 * 
 * BookingServiceException does not have a no argument constructor because it would
 * tell the client nothing about the problem encountered
 * 
 * @author Aaron
 */
public class BookingServiceException extends Exception {

	private static final long serialVersionUID = -938151842713187807L;
	
	/**
	 * @param msg
	 */
	public BookingServiceException(final String msg){
		super(msg);
	}

}
