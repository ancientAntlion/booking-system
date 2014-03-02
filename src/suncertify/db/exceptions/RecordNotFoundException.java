package suncertify.db.exceptions;

public class RecordNotFoundException extends Exception {

	private static final long serialVersionUID = 9172845648588845215L;

	public RecordNotFoundException(){
		super();
	}

	public RecordNotFoundException(String description){
		super(description);
	}
	
}
