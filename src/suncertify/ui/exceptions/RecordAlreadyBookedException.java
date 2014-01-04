package suncertify.ui.exceptions;

/**
 * @author eaarcot
 * 
 */
public class RecordAlreadyBookedException extends RecordBookingException {
	
	private static final long serialVersionUID = 145483798743149230L;
	private String customerId;

	public RecordAlreadyBookedException(final int recNo, final String customerId) {
		super(recNo);
		this.customerId = customerId;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

}
