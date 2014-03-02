package suncertify.db.locking;

import java.util.Random;

public class LockCookieGenerator {
	
	private final static Random random = new Random();
	
	public static long getLockCookie(){
		return random.nextLong();
	}

}
