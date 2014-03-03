package suncertify.ui.exceptions;

/**
 * @author eaarcot
 * 
 */
/**
 * @author Aaron
 *
 */
public class InvalidModeException extends Exception {
	
	private static final long serialVersionUID = 2829747352002749104L;
	
	final String mode;

	/**
	 * 
	 */
	public InvalidModeException() {
		this.mode = "NO_MODE_SPECIFIED";
	}
	
	/**
	 * @param mode
	 */
	public InvalidModeException(final String mode) {
		this.mode = mode;
	}
	
	/**
	 * @return
	 */
	public String getMode(){
		return mode;
	}

}
