package suncertify.db;

/**
 * Any time the user requests record 'x' for updating, deleting or just reading and record 'x'
 * Either does not exist or has been flagged as deleted then we throw this exception
 *
 * @author Aaron
 */
public class RecordNotFoundException extends Exception {

	private static final long serialVersionUID = 9172845648588845215L;

	/**
	 * No parameter constructor
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
