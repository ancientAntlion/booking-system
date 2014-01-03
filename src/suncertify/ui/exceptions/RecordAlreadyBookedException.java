package suncertify.ui.exceptions;

/**
 * @author eaarcot
 * 
 */
public class RecordAlreadyBookedException extends Exception {

	private static final long serialVersionUID = -3257012412705615512L;

	private final String customerId;
	private final int recNo;

	public RecordAlreadyBookedException(final String customerId, final int recNo) {
		this.customerId = customerId;
		this.recNo = recNo;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @return the recNo
	 */
	public int getRecNo() {
		return recNo;
	}

}
