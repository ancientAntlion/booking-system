package suncertify.tests;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JFrame;
import suncertify.rmi.RoomRMIManager;
import suncertify.ui.HotelFrameController;
import suncertify.ui.HotelServerFrame;
import suncertify.util.ApplicationMode;

public class TestBusinessService {  
  
    private static final String DB_PATH = "db-1x3_test.db";  
  
    public static void main(final String[] args) {  
        // start your RMI-server
        //RoomRMIManager.start(DB_PATH, "localhost", "1099");
        new TestBusinessService().startTests();  
    }  
  
    public void startTests() {  
        List<Thread> threads = new ArrayList<Thread>();  
        try {  
            // create book-threads  
            for (int i = 0; i < 100; i++) {  
                threads.add(new Thread(new BookThread(i * 100), String.valueOf(i * 100)));  
            }  
            // random order  
            Collections.shuffle(threads);  
            // start threads  
            for (Thread thread : threads) {  
                thread.start();  
            }  
            // sleep until all threads are finished  
            for (Thread thread : threads) {  
                thread.join();  
            }  
        } catch (final Exception e) {  
            System.out.println(e);  
        }  
        System.out.println("Done.");  
    }  
  
    private class BookThread implements Runnable {  
        private int id;  
        private String customer;  
        private boolean endRun;  
        private boolean noRoom;  
  
        public BookThread(final int id) {  
            this.id = id;  
            this.customer = String.format("%1$08d", id);  
            this.endRun = false;  
            this.noRoom = false;  
        }  
  
        @Override  
        public void run() {  
            int recNo = 0;  
            int number = 11111111;
            while (!endRun) {  
               // try {  
                    HotelFrameController service = new HotelFrameController(ApplicationMode.ALONE, DB_PATH, "1099");
                    service.reserveRoom(recNo, Integer.toString(number));  
                    endRun = true;  
                /*} catch (RemoteException e) {  
                    System.out.println(e);  
                } catch (NoRoomFoundException e) {  
                    endRun = true;  
                    noRoom = true;  
                } catch (RoomAlreadyBookedException e) {  
                    // expected to occur  
                }  */
                recNo++;  
            }  
            if (noRoom) {  
                System.out.println(id + " booked no room");  
            } else {  
                System.out.println(id + " booked room " + recNo);  
            }  
        }  
    }  
  
}  