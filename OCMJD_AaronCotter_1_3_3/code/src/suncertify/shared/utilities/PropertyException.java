package suncertify.shared.utilities;


/**
 * This exception will be thrown if any problem is encountered during property file interactions.
 * 
 * @author Aaron
 *
 */
public class PropertyException extends Exception {
	
	private static final long serialVersionUID = -4666200752183675360L;

	public PropertyException(final Exception e){
		super(e);
	}

}
