package suncertify.db.exceptions;

/**
 * @author Aaron
 *
 */
public class SecurityException extends Exception {

	private static final long serialVersionUID = 8491334144885559308L;

	/**
	 * 
	 */
	public SecurityException(){
		super();
	}

	/**
	 * @param description
	 */
	public SecurityException(String description){
		super(description);
	}
	
}
