package suncertify.db.locking;

import java.util.Random;

/**
 * A simple random number generator used for cookie generation
 * 
 * @author Aaron
 */
public class LockCookieGenerator {
	
	private final static Random random = new Random();
	
	/**
	 * @return lockCookie
	 */
	public static long getLockCookie(){
		return random.nextLong();
	}

}
