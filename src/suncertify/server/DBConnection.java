package suncertify.server;

import suncertify.db.DB;
import suncertify.db.Data;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.db.SecurityException;

public class DBConnection {
	
	final DB database = new Data("src\\suncertify\\db\\db-1x3.db");
		
	public String[] getDBEntry(final int recNo) throws RecordNotFoundException{
		return database.read(recNo);
	}

	public String[] read(int recNo) throws RecordNotFoundException {
		return database.read(recNo);
	  
	}

  public void update(int recNo, String[] data, long lockCookie)
    throws RecordNotFoundException, SecurityException{
	    database.update(recNo, data, lockCookie);
  }

  public void delete(int recNo, long lockCookie)
    throws RecordNotFoundException, SecurityException{
	  database.delete(recNo, lockCookie);
  }

  public int[] find(String[] criteria){
	return database.find(criteria);
	  
  }

  public int create(String[] data) throws DuplicateKeyException{
	return database.create(data);
	  
  }

  public long lock(int recNo) throws RecordNotFoundException{
	return recNo;
  
  }
	  

  public void unlock(int recNo, long cookie)
    throws RecordNotFoundException, SecurityException{
	  
  }
	
	
}
