package suncertify.ui.exceptions;

/**
 * @author eaarcot
 * 
 */
public class RecordBookingException extends Exception {

	private static final long serialVersionUID = -3257012412705615512L;

	private final int recNo;

	/**
	 * @param recNo
	 */
	public RecordBookingException(final int recNo) {
		this.recNo = recNo;
	}

	/**
	 * @return the recNo
	 */
	public int getRecNo() {
		return recNo;
	}

}
