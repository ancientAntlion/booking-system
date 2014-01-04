package suncertify.ui.exceptions;

/**
 * @author eaarcot
 * 
 */
public class RecordNotBookedException extends RecordBookingException {

	private static final long serialVersionUID = -3257012412705615512L;

	public RecordNotBookedException(final int recNo) {
		super(recNo);
	}

}
