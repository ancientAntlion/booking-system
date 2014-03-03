package suncertify.db.exceptions;

/**
 * @author Aaron
 *
 */
public class DatabaseInitializationException extends Exception {

	private static final long serialVersionUID = -797941450748637420L;

	/**
	 * 
	 */
	public DatabaseInitializationException(){
		super();
	}
	
	/**
	 * @param msg
	 */
	public DatabaseInitializationException(final String msg){
		super(msg);
	}
	
}
