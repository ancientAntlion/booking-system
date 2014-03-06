package suncertify.db;

/**
 * An exception we throw when there is a problem with initializing the DB. If there are any problems
 * Opening the file, or if the file doesn't exist, or anything like that we throw this exception
 * 
 * @author Aaron
 */
public class DatabaseInitializationException extends Exception {

	private static final long serialVersionUID = -797941450748637420L;

	/**
	 * No parameter constructor
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
