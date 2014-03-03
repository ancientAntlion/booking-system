package suncertify.db.locking;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import suncertify.db.exceptions.SecurityException;

public class LockingManager {
	
	private static ConcurrentHashMap<Integer, LockObject> recordLockMap = new ConcurrentHashMap<Integer, LockObject>();
	
	private static LockingManager lockingManager;
	
	public static LockingManager getInstance(){
		if(LockingManager.lockingManager == null){
			LockingManager.lockingManager = new LockingManager();
		}
		
		return lockingManager;
	}
	
	public long lock(final int recNo){
		LockObject lockObject = getLockObject(recNo);
		return lockObject.lock();
	}
	
	public void unlock(final int recNo, final long lockCookie) throws SecurityException{
		LockObject lockObject = getLockObject(recNo);
		lockObject.unlock(lockCookie);
	}
	
	public void verifyCookie(final int recNo, final long lockCookie) throws SecurityException {
		LockObject lockObject = new LockObject();
		lockObject = LockingManager.recordLockMap.putIfAbsent(recNo, lockObject);
		if (lockObject.getCookie() != lockCookie) {
			throw new SecurityException("Supplied cookie (" + lockCookie + ") does not match record cookie (" + lockObject.getCookie() + ")");
		}
	}
	
	private LockObject getLockObject(final int recNo){
		final LockObject lockObject = new LockObject();
		LockingManager.recordLockMap.putIfAbsent(recNo, lockObject);
		return LockingManager.recordLockMap.get(recNo);
	}
	
}
