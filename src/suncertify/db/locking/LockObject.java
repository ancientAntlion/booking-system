package suncertify.db.locking;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import suncertify.db.exceptions.SecurityException;

/**
 * @author Aaron
 *
 */
public class LockObject {
	
	private long lockCookie;
		
	private final ReentrantLock lock = new ReentrantLock();
	
	private final Condition lockCondition = lock.newCondition();
	
	private boolean currentlyLocked;
		
	/**
	 * 
	 */
	public LockObject(){
		lockCookie = -1L;
		currentlyLocked = false;
	}
	
	/**
	 * @return
	 */
	public long lock(){
		lock.lock();
		try{
			while(currentlyLocked){
				lockCondition.awaitUninterruptibly();
			}
			currentlyLocked = true;
			lockCookie = LockCookieGenerator.getLockCookie();
			System.out.println(Thread.currentThread().getId() + " - new lock cookie - " + lockCookie);
			return lockCookie;
		}finally{
			lock.unlock();
		}
	}
	
	/**
	 * @param lockCookie
	 * @throws SecurityException
	 */
	public void unlock(final long lockCookie) throws SecurityException{
		lock.lock();
		try{
			if(this.lockCookie != lockCookie){
				throw new SecurityException("Supplied cookie (" + lockCookie + ") does not match record cookie (" + this.lockCookie + ")");
			}
			currentlyLocked = false;
			this.lockCookie = -1;
			lockCondition.signal();
		}finally{
			lock.unlock();
		}
	}
	
	/**
	 * @return
	 */
	public Long getCookie(){
		return lockCookie;
	}

}
