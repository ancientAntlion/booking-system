package suncertify.db.exceptions;

public class SecurityException extends Exception {

	private static final long serialVersionUID = 8491334144885559308L;

	public SecurityException(){
		super();
	}

	public SecurityException(String description){
		super(description);
	}
	
}
