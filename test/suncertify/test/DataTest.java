package suncertify.test;  
  
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.Data;  
import suncertify.db.DatabaseInitializationException;
import suncertify.db.RecordNotFoundException;
import suncertify.db.SecurityException;
  
/** 
 * The <code>DataClassTest</code> tests the main functionalities of the 
 * {@link Data} class. In order to simulate several clients trying to use it and 
 * exercise the locking mechanism, it also has several inner classes that extend 
 * the {@link Thread} class, where each class represents one client requesting 
 * one operation, and mainly requesting updating and deletion of records. The 
 * <code>FindingRecordsThread</code> exercises two functionalities: finding 
 * records and reading records. 
 *  
 * @author Roberto Perillo 
 * @version 1.0 05/11/2008 
 */  
public class DataTest {  
    
    private static final String DB_PATH = "C:\\Users\\Aaron\\booking-system\\src\\suncertify\\db\\db-1x3.db";
  
    private static Data data = null;  
  
    /* 
     * If any preparation has to be done before using the Data class, it can be 
     * done in a static block; in this case, before using the Data class, the 
     * loadDbRecords method has to be called prior to any other operation, so 
     * the records in the physical .db file can be placed in the Map that keeps 
     * them in memory; I also have a method called persistDbRecords, which 
     * writes each record back to the physical .db file, but this test aims only 
     * to test the functionalities without altering the database, so this method 
     * is never called anywhere 
     */  
  
    public static void main(String [] args) {
        try {
            data = new Data(DB_PATH);
        } catch (DatabaseInitializationException ex) {
            Logger.getLogger(DataTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("STARTING NOW");
        
        new DataTest().startTests();  
    }  
  
    public void startTests() {  
        try {  
              
            /* 
             * Practically, it is not necessary to execute this loop more than 1 
             * time, but if you want, you can increase the controller variable, 
             * so it is executed as many times as you want 
             */  
            for (int i = 0; i < 2000; i++) {  
                Thread updatingRandom = new UpdatingRandomRecordThread();  
                updatingRandom.start();  
                Thread updatingRecord1 = new UpdatingRecord1Thread();  
                updatingRecord1.start();  
                Thread creatingRecord = new CreatingRecordThread();  
                creatingRecord.start();  
                Thread deletingRecord = new DeletingRecord1Thread();  
                deletingRecord.start();  
                Thread findingRecords = new FindingRecordsThread();  
                findingRecords.start();  
            }  
        } catch (Exception e) {  
        	System.out.println("Exception during start tests operation");  
            System.out.println(e);  
        }  
  
    }  
  
    private class UpdatingRandomRecordThread extends Thread {  
  
        @SuppressWarnings("deprecation")  
        public void run() {  
            final String[] room = {"Palace", "Smallville", "2", "Y", "$150.00", "2005/05/06", "54120584"};  
  
            final int recNo = (int) (Math.random() * 50);  
            boolean lockedSuccessfully = false;
            Long cookie = 0L;
            try {  
                System.out.println(Thread.currentThread().getId()  
                        + " trying to lock record #" + recNo  
                        + " on UpdatingRandomRecordThread");  
  
                /* 
                 * The generated record number may not exist in the database, so 
                 * a RecordNotFoundException must be thrown by the lock method. 
                 * Since the database records are in a cache, it is not 
                 * necessary to put the unlock instruction in a finally block, 
                 * because an exception can only occur when calling the lock 
                 * method (not when calling the update/delete methods), 
                 * therefore it is not necessary to call the unlock method in a 
                 * finally block, but you can customize this code according to 
                 * your reality 
                 */  
                cookie = data.lock(recNo);
                lockedSuccessfully = true;
                System.out.println(Thread.currentThread().getId()  
                        + " trying to update record #" + recNo  
                        + " on UpdatingRandomRecordThread");  
  
                /* 
                 * An exception cannot occur here, otherwise, the unlock 
                 * instruction will not be reached, and the record will be 
                 * locked forever. In this case, I created a class called 
                 * RoomRetriever, which transforms from Room to String array, 
                 * and vice-versa, but it could also be done this way: 
                 */ 
                 data.update(recNo, room, cookie); 
                   
                //data.update(recNo, room.toStringArray());  
                System.out.println(Thread.currentThread().getId()  
                        + " trying to unlock record #" + recNo  
                        + " on UpdatingRandomRecordThread");  
					
				
            } catch (Exception e) {  
            	System.out.println("Exception during run operation");  
            	e.printStackTrace();
                System.out.println(e);  
            } finally{
            	if(lockedSuccessfully){
            		try {
						data.unlock(recNo, cookie);
					} catch (Exception e) {
						System.out.println("Exception during run operation");  
						e.printStackTrace();
		                System.out.println(e);  
					}
            	}
            }
        }  
    }  
  
    private class UpdatingRecord1Thread extends Thread {  
  
        @SuppressWarnings("deprecation")  
        public void run() {   
        	
        	boolean lockedSuccessfully = false;
        	Long cookie = 0L;
            try {  
                System.out.println(Thread.currentThread().getId()  
                        + " trying to lock record #1 on"  
                        + " UpdatingRecord1Thread");  
                cookie = data.lock(1);  
                lockedSuccessfully = true;
                System.out.println(Thread.currentThread().getId()  
                        + " trying to update record #1 on"  
                        + " UpdatingRecord1Thread"); 
                data.update(1, new String[] {"Castle", "Digitopolis", "2", 
                 "N", "$90.00", "2005/04/01", "88006644"}, cookie);
                //data.update(1, room.toStringArray());  
                System.out.println(Thread.currentThread().getId()  
                        + " trying to unlock record #1 on"  
                        + "UpdatingRecord1Thread");  
                  
                /* 
                 * In order to see the deadlock, this instruction can be 
                 * commented, and the other Threads, waiting to update/delete 
                 * record #1 will wait forever and the deadlock will occur 
                 */  
            } catch (Exception e) {  
            	System.out.println("Exception during update operation");  
            	e.printStackTrace();
                System.out.println(e);  
            } finally{
            	if(lockedSuccessfully){
            		try {
						data.unlock(1, cookie);
					} catch (Exception e) {
						System.out.println("Exception during update1 operation");  
						e.printStackTrace();
		                System.out.println(e);  
					}
            	}
            }
        }  
    }  
  
    private class CreatingRecordThread extends Thread {  
  
        @SuppressWarnings("deprecation")  
        public void run() {   
            try {  
                System.out.println(Thread.currentThread().getId()  
                        + " trying to create a record");
                data.create(new String[] {"Elephant Inn", "Emerald City", "6", 
                 "N", "$120.00", "2005/02/03",""});  
            } catch (Exception e) {  
            	System.out.println("Exception during create operation");  
                System.out.println(e);                  
            }  
        }  
    }  
  
    private class DeletingRecord1Thread extends Thread {  
  
        public void run() {  
        	boolean lockedSuccessfully = false;
        	Long cookie = 0L;
            try {  
                System.out.println(Thread.currentThread().getId()  
                        + " trying to lock record #1 on "  
                        + "DeletingRecord1Thread");  
                cookie = data.lock(1);  
                lockedSuccessfully = true;
                System.out.println(Thread.currentThread().getId()  
                        + " trying to delete record #1 on "  
                        + "DeletingRecord1Thread");  
                data.delete(1, cookie);  
                System.out.println(Thread.currentThread().getId()  
                        + " trying to unlock record #1 on "  
                        + "DeletingRecord1Thread");  
            } catch (Exception e) {  
            	System.out.println("Exception during delete1 operation");  
                System.out.println(e);  
            }  finally{
            	if(lockedSuccessfully){
            		try {
						data.unlock(1, cookie);
					} catch (Exception e) {
						System.out.println("Exception during delete1 operation");  
		                System.out.println(e);  
					}
            	}
            }
        }  
    }  
  
    private class FindingRecordsThread extends Thread {  
  
        public void run() {  
            try {  
                System.out.println(Thread.currentThread().getId()  
                        + " trying to find records");  
                final String [] criteria = {"Palace", "Smallville", null, null,  
                        null, null, null};  
                final int [] results = data.find(criteria);  
  
                for (int i = 0; i < results.length; i++) {  
                    System.out.println(results.length + " results found.");  
                    try {  
                        final String message = Thread.currentThread().getId()  
                                + " going to read record #" + results[i]  
                                + " in FindingRecordsThread - still "  
                                + ((results.length - 1) - i) + " to go.";  
                        System.out.println(message);  
                        final String [] room = data.read(results[i]);  
                        System.out.println("Hotel (FindingRecordsThread): "  
                                + room[0]);  
                        System.out.println("Has next? "  
                                + (i < (results.length - 1)));  
                    } catch (Exception e) {  
                        /* 
                         * In case a record was found during the execution of 
                         * the find method, but deleted before the execution of 
                         * the read instruction, a RecordNotFoundException will 
                         * occur, which would be normal then 
                         */  
                        System.out.println("Exception in "  
                                + "FindingRecordsThread - " + e);  
                    }  
                }  
                System.out.println("Exiting for loop");  
            } catch (Exception e) {  
            	System.out.println("Exception during find operation");  
                System.out.println(e);  
            }  
        }  
    }  
} 
