package suncertify.server;

import java.util.ArrayList;
import java.util.List;

import suncertify.db.DB;
import suncertify.db.Data;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.db.SecurityException;

public class DBConnection {
	
	final DB database = new Data();
	
	
	public String[][] getAllEntries(){
		final List<String[]> entryList = new ArrayList<String[]>();
		try{
			for(int i = 0;;i++){
				entryList.add(database.read(i));
			}
		}catch(RecordNotFoundException rnfe){
			//End of file reached, all records read, carry on
		}
		
		if(entryList.isEmpty()){
			return new String[0][0];
		}
		
		final String[][] allEntries = new String[entryList.size()][entryList.get(0).length];
		
		for(int i = 0; i<entryList.size(); i++){
			final String[] singleEntry = entryList.get(i);
			allEntries[i] = singleEntry;
		}
		
		return allEntries;
	}
	
	public String[] getDBEntry(final int recNo) throws RecordNotFoundException{
		return database.read(recNo);
	}

	public String[] read(int recNo) throws RecordNotFoundException {
		return null;
	  
	}

  public void update(int recNo, String[] data, long lockCookie)
    throws RecordNotFoundException, SecurityException{
	  
  }


  public void delete(int recNo, long lockCookie)
    throws RecordNotFoundException, SecurityException{
	  
  }

  public int[] find(String[] criteria){
	return null;
	  
  }

  public int create(String[] data) throws DuplicateKeyException{
	return 0;
	  
  }

  public long lock(int recNo) throws RecordNotFoundException{
	return recNo;
  
  }
	  

  public void unlock(int recNo, long cookie)
    throws RecordNotFoundException, SecurityException{
	  
  }
	
	
}
