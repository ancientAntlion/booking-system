package suncertify.db;

public class StaticMainVoid {
	
	//READ 1 ENTRY, CHANGE THE HOTEL NAME, WRITE ENTRY BACK TO SPREADSHEET, PRINT NEW ENTRY

//	public static void main(String[] args) {
//		DataAccessObject dao = new DataAccessObject();
//		try {
//			String[] lookup = dao.read(0);
//			for(String st : lookup){
//				System.out.print(st + " ");
//			}
//			lookup[0] = "Dew Drop Inn Changed";
//			
//			dao.update(0, lookup, 1L);
//			
//			String[] lookupAgain = dao.read(0);
//			for(String st : lookupAgain){
//				System.out.print(st + " ");
//			}
//			
//		} catch (RecordNotFoundException rnfe) {
//			rnfe.printStackTrace();
//		} catch(SecurityException se){
//			se.printStackTrace();
//		}
//		
//	}
	
	
	//PRINT OUT 30 ENTRIES FROM SPREADSHEET
	
//	public static void main(String[] args) {
//		DataAccessObject dao = new DataAccessObject();
//		try {
//			for(int i = 0; i<35; i++){
//				String[] lookup = dao.read(i);
//				System.out.println();
//				for(String st : lookup){
//					System.out.print(st.trim() + " - ");
//				}
//				System.out.println();
//			}
//		} catch (RecordNotFoundException rnfe) {
//			rnfe.printStackTrace();
//		} 
//		
//	}
	
	
	public static void main(String[] args) throws DuplicateKeyException, SecurityException {
		DataAccessObject dao = new DataAccessObject();
		try {
			for(int i = 0; i<35; i++){
				String[] lookup = dao.read(i);
				for(String st : lookup){
					System.out.print(st + " ");
				}
				
//				lookup[0] = "MOTHERFUCKING NEW";
//				
//				dao.create(lookup);
			}
//				System.out.println();
//				
//				lookup[0] = "Replacing first entry I hope";
//				lookup[1] = "New Location";
				
				
//				dao.create(lookup);
				
				
//				
//				lookup[0] = "Replacing first entry I hope";
//				lookup[1] = "New Location";
//				dao.create(lookup);
//				
//				dao.delete(0, 1L);
				
				
//				lookup[0] = "The Banana House";
				

//				
//				dao.create(lookup);
				
//				dao.delete(29, 1L);
//				dao.delete(30, 1L);

//				dao.delete(2, 1L);
//				dao.delete(2, 1L);
//				dao.delete(2, 1L);
//				dao.delete(2, 1L);
//				dao.delete(2, 1L);
//				dao.delete(2, 1L);
				
//				dao.create(lookup);
				
//			}
		} catch (RecordNotFoundException rnfe) {
			rnfe.printStackTrace();
		} 
		
	}

}
