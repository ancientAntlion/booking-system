package suncertify.db;

/** 
 * Exits just to fulfill the interface requirements. We never throw this.
 * I explain my reasoning in my choices.txt
 * 
 * @author Aaron
 */
public class DuplicateKeyException extends Exception {

	private static final long serialVersionUID = -6689165809485807888L;

	/**
	 * No parameter constructor
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
