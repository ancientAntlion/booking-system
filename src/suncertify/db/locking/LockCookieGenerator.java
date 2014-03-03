package suncertify.db.locking;

import java.util.Random;

/**
 * @author Aaron
 *
 */
public class LockCookieGenerator {
	
	private final static Random random = new Random();
	
	/**
	 * @return
	 */
	public static long getLockCookie(){
		return random.nextLong();
	}

}
