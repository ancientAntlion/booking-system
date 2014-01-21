package suncertify.ui.exceptions;

/**
 * @author eaarcot
 * 
 */
public class ServiceUnavailableException extends Exception {

	private static final long serialVersionUID = -6722031589181134646L;

	private final Exception exception;

	public ServiceUnavailableException(final Exception exception) {
		this.exception = exception;
	}

	/**
	 * @return the recNo
	 */
	public Exception getException() {
		return exception;
	}

}
