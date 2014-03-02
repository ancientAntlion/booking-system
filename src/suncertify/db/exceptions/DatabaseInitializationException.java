package suncertify.db.exceptions;

public class DatabaseInitializationException extends Exception {

	private static final long serialVersionUID = -797941450748637420L;

	public DatabaseInitializationException(){
		super();
	}
	
	public DatabaseInitializationException(final String msg){
		super(msg);
	}
	
}
