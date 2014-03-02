package suncertify.db.locking;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockObject {
	
	private long cookie;
		
	private final ReentrantLock lock;
	
	private final Condition lockCondition;
	
	private boolean currentlyLocked;
		
	public LockObject(){
		lock = new ReentrantLock();
		lockCondition = lock.newCondition();
		currentlyLocked = false;
	}
	
	public long lock(){
		lock.lock();
		try{
			if(currentlyLocked){
				lockCondition.awaitUninterruptibly();
			}
			currentlyLocked = true;
			cookie = LockCookieGenerator.getLockCookie();
			return cookie;
		}finally{
			lock.unlock();
		}
	}
	
	public void unlock(final long cookie){
		lock.lock();
		try{
			if(this.cookie != cookie){
				throw new SecurityException();
			}
			currentlyLocked = false;
			this.cookie = -1;
		}finally{
			lock.unlock();
		}
	}

}
