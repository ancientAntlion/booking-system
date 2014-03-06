package suncertify.db.locking;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import suncertify.db.exceptions.SecurityException;

/**
 * The object that controls the actual lock on records. The class uses a ReentrantLock lock
 * to control access to records. We lock the ReentrantLock at the start of every method and
 * unlock it at the end of every method. Inside the methods we change the currentlyLocked boolean
 * to unlock and lock the record.
 * 
 * @author Aaron
 * 
 */
public class LockObject {
	
	private long lockCookie;
		
	private final ReentrantLock lock = new ReentrantLock();
	
	private final Condition lockCondition = lock.newCondition();
	
	private boolean currentlyLocked;
		
	/**
	 * Constructor
	 */
	public LockObject(){
		lockCookie = -1L;
		currentlyLocked = false;
	}
	
	/**
	 * Attempts to lock the object. If the object is already locked the thread waits for a signal
	 * to wake up and attempt to lock again.
	 * 
	 * @return lockCookie
	 */
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
	
	/**
	 * Attempts to unlock the object. If the supplied lockCookie does not match the actual lockCookie then
	 * A SecurityException is thrown. Otherwise we unlock the object and wake up any sleeping threads
	 * 
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
	 * @return lockCookie
	 */
	public Long getCookie(){
		return lockCookie;
	}

}
