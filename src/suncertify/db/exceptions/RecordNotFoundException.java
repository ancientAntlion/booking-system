package suncertify.db.exceptions;

/**
 * @author Aaron
 *
 */
public class RecordNotFoundException extends Exception {

	private static final long serialVersionUID = 9172845648588845215L;

	/**
	 * 
	 */
	public RecordNotFoundException(){
		super();
	}

	/**
	 * @param description
	 */
	public RecordNotFoundException(String description){
		super(description);
	}
	
}
