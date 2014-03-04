package suncertify.db.exceptions;

/**
 * We throw this exception when the lock cookie supplied does not match the
 * Lock cookie that is currently holding the lock on a record
 * 
 * @author Aaron
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
