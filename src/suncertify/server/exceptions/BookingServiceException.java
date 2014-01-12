package suncertify.server.exceptions;

public class BookingServiceException extends Exception {

	private static final long serialVersionUID = -938151842713187807L;
	
	public BookingServiceException(final String msg){
		super(msg);
	}

}
