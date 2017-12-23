import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Oliver on 18/11/2017.
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

/**
 * A Flight object stores information about a flight to be retrieved in the Reduce method
 */
@SuppressWarnings("unused")
class Flight {
    private final String passengerID, fromAirport, toAirport, flightTime, flightID;
    private final SimpleDateFormat form = new SimpleDateFormat("HH:MM:SS");
    private final Date depart;

    Flight(String flightID, String passID, String from, String to, String departureTime, String flightTime){
        this.flightID = flightID;
        this.passengerID = passID;
        this.fromAirport = from;
        this.toAirport = to;
        this.flightTime = flightTime;
        this.depart = new Date(Long.parseLong(departureTime) * 1000);
    }
    String getFlightID(){
        return flightID;
    }
    String getPassengerID(){
        return passengerID;
    }
    String getFrom(){
        return fromAirport;
    }
    String getTo(){
        return toAirport;
    }
    String getDepartTime(){
        return form.format(depart);
    }
    String getFlightTime(){
        return flightTime;
    }
    String getArrivalTime(){
        return form.format(new Date(depart.getTime() + (Integer.parseInt(flightTime) * 60000)));
    }
}
