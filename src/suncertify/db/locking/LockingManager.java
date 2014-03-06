package suncertify.db.locking;

import java.util.concurrent.ConcurrentHashMap;

import suncertify.db.SecurityException;

/**
 * Manages all record locking. The class has a private static ConcurrentHashMap which
 * Stores record numbers paired with LockObjects. Each entry in the map represents a
 * Lock on a record. The class is a singleton.
 * 
 * @author Aaron
 */
public class LockingManager {
	
	private static ConcurrentHashMap<Integer, LockObject> recordLockMap = new ConcurrentHashMap<Integer, LockObject>();
	
	private static LockingManager lockingManager;
	
	
	/**
	 * If no instance exist we create one, otherwise we use return the existing instance.
	 * 
	 * @return lockingManager
	 */
	public static LockingManager getInstance(){
		if(LockingManager.lockingManager == null){
			LockingManager.lockingManager = new LockingManager();
		}
		
		return lockingManager;
	}
	
	/**
	 * Uses the getLockObject(int) method and locks the LockObject
	 * 
	 * @param recNo
	 * @return lockCookie
	 */
	public long lock(final int recNo){
		LockObject lockObject = getLockObject(recNo);
		return lockObject.lock();
	}
	
	/**
	 * Uses the getLockObject(int) method and unlocks the LockObject with the lockCookie provided
	 * 
	 * @param recNo
	 * @param lockCookie
	 * @throws SecurityException
	 */
	public void unlock(final int recNo, final long lockCookie) throws SecurityException{
		LockObject lockObject = getLockObject(recNo);
		lockObject.unlock(lockCookie);
	}
	
	/**
	 * Matches the supplied record number with the supplied lock. If they do not match then
	 * Then we throw a SecurityException. If the lock does not exist then the record has not
	 * Been locked so we throw a SecurityException. If nothing is thrown then we have verified
	 * The cookie matches the lock.
	 * 
	 * @param recNo
	 * @param lockCookie
	 * @throws SecurityException
	 */
	public void verifyCookie(final int recNo, final long lockCookie) throws SecurityException {
		LockObject lockObject = LockingManager.recordLockMap.get(recNo);
		if(lockObject == null){
			throw new SecurityException("There is no lock on record record " + recNo + " so access is denied");
		}
		if (lockObject.getCookie() != lockCookie) {
			throw new SecurityException("Supplied cookie (" + lockCookie + ") does not match record cookie (" + lockObject.getCookie() + ")");
		}
	}
	
	/**
	 * Returns a lock from the recordLockMap. If a lock does not exist in the recordLockMap then
	 * We create it and return it. If the lock already exists then we return that lock.
	 * 
	 * 
	 * @param recNo
	 * @return
	 */
	private LockObject getLockObject(final int recNo){
		final LockObject lockObject = new LockObject();
		LockingManager.recordLockMap.putIfAbsent(recNo, lockObject);
		return LockingManager.recordLockMap.get(recNo);
	}
	
}
