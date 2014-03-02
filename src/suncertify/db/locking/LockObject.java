package suncertify.db.locking;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockObject {
	
	private long lockCookie;
		
	private final ReentrantLock lock = new ReentrantLock();
	
	private final Condition lockCondition = lock.newCondition();
	
	private boolean currentlyLocked;
		
	public LockObject(){
		lockCookie = 0;
		currentlyLocked = false;
	}
	
	public long lock(){
		lock.lock();
		try{
			while(currentlyLocked){
				lockCondition.awaitUninterruptibly();
			}
			currentlyLocked = true;
			lockCookie = LockCookieGenerator.getLockCookie();
			return lockCookie;
		}finally{
			lock.unlock();
		}
	}
	
	public void unlock(final long lockCookie){
		lock.lock();
		try{
			if(this.lockCookie != lockCookie){
				throw new SecurityException("Supplied cookie does not match record cookie");
			}
			currentlyLocked = false;
			this.lockCookie = -1;
			lockCondition.signal();
		}finally{
			lock.unlock();
		}
	}
	
	public Long getCookie(){
		return lockCookie;
	}

}
