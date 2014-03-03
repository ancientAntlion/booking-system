package suncertify.server.exceptions;

/**
 * @author Aaron
 *
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
