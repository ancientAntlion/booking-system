package suncertify.db;

public class StaticMainVoid {
	
	public static void main(String[] args) throws DuplicateKeyException, RecordNotFoundException, SecurityException{
		final DB database = new Data("src\\suncertify\\db\\db-1x3.db");
		
		final String[] data = {"10th","9","3","4","5","6","7"};

//		final int index = database.create(data);
		
		database.delete(32, 1L);
		
//		System.out.println(index);
		
	}

}

//35 - 9th
//34 - 7th
//33 - 6th
//32 - 5th
//31 - third
//30 - 4th
//29 - hello
