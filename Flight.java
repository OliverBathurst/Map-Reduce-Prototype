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
    private String getFlightID(){
        return flightID;
    }
    String getPassengerID(){
        return passengerID;
    }
    private String getFrom(){
        return fromAirport;
    }
    private String getTo(){
        return toAirport;
    }
    private String getDepartTime(){
        return form.format(depart);
    }
    private String getFlightTime(){
        return flightTime;
    }
    private String getArrivalTime(){
        return form.format(new Date(depart.getTime() + (Integer.parseInt(flightTime) * 60000)));
    }
    String getDetails(){
        return "\nFLIGHT ID: " + getFlightID() + "\nFROM: " + getFrom() + "\nTO: " + getTo() + "\nDEPARTURE TIME: " + getDepartTime()
                + "\nFLIGHT TIME (M): " + getFlightTime() + "\nARRIVAL TIME: " + getArrivalTime() + "\nPASSENGER ID: " + getPassengerID() + "\n";
    }
}
