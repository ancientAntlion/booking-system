package suncertify.db.locking;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LockingManager {
	
	private final Map<Integer, LockObject> recordLockMap;
	
	private static LockingManager lockingManager;
	
	public static LockingManager getInstance(){
		if(LockingManager.lockingManager == null){
			LockingManager.lockingManager = new LockingManager();
		}
		
		return lockingManager;
	}
	
	public LockingManager(){
		this.recordLockMap = new ConcurrentHashMap<Integer, LockObject>();
	}
	
	public long lock(final int recNo){
		LockObject lockObject = getLockObject(recNo);
		return lockObject.lock();
	}
	
	public void unlock(final int recNo, final long lockCookie){
		LockObject lockObject = getLockObject(recNo);
		lockObject.unlock(lockCookie);
	}
	
	public void verifyCookie(final int recNo, final long lockCookie) throws SecurityException {
		LockObject lockObject = this.recordLockMap.get(recNo);
		if(lockObject == null){
			this.recordLockMap.put(recNo, new LockObject());
		}else{
			if(lockObject.getCookie() != lockCookie){
				throw new SecurityException("Supplied cookie does not match record cookie");
			}
		}
	}
	
	private LockObject getLockObject(final int recNo){
		if(this.recordLockMap.get(recNo) == null){
			LockObject lockObject = new LockObject();
			this.recordLockMap.put(recNo, lockObject);
		}
		
		return this.recordLockMap.get(recNo);
	}
	
}
