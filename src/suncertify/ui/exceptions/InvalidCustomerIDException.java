package suncertify.ui.exceptions;

/**
 * @author eaarcot
 * 
 */
public class InvalidCustomerIDException extends Exception {

	private static final long serialVersionUID = -3257012412705615512L;

	private final String customerId;

	public InvalidCustomerIDException(final String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

}
