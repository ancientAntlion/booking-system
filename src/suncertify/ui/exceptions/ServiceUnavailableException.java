package suncertify.ui.exceptions;

/**
 * @author eaarcot
 * 
 */
public class ServiceUnavailableException extends Exception {

	private static final long serialVersionUID = -6722031589181134646L;
	
	/**
	 * @param message
	 */
	public ServiceUnavailableException(final String message) {
		super(message);
	}

}
