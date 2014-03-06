package suncertify.ui.exceptions;

/**
 * @author Aaron
 *
 */
public class InvalidCustomerIDException extends Exception {

	private static final long serialVersionUID = -3257012412705615512L;

	private final String customerId;

	/**
	 * @param customerId
	 */
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
