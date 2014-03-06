package suncertify.server;

import java.rmi.Remote;

/**
 * Interface used when remote mode is selected. Identical to BookingService interface except
 * This interface also extends Remote so that it can be used over RMI
 * 
 * @author Aaron
 */
public interface RemoteBookingService extends Remote, BookingService {

}