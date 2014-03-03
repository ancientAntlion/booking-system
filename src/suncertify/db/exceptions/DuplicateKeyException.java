package suncertify.db.exceptions;

/**
 * @author Aaron
 *
 */
public class DuplicateKeyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6689165809485807888L;

	/**
	 * 
	 */
	public DuplicateKeyException(){
		super();
	}

	/**
	 * @param description
	 */
	public DuplicateKeyException(String description){
		super(description);
	}
	
}
