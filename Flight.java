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
    private final Long depart;

    Flight(String flightID, String passengerID, String from, String to, String departureTime, String flightTime){
        this.flightID = flightID;
        this.passengerID = passengerID;
        this.fromAirport = from;
        this.toAirport = to;
        this.flightTime = flightTime;
        this.depart = Long.parseLong(departureTime) * 1000L;//convert into epoch time (ms)
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
        return new Date(depart).toString();
    }
    private String getFlightTime(){
        return flightTime;
    }
    private String getArrivalTime(){
        return new Date(depart + (Long.parseLong(flightTime) * 60000L)).toString();
    }
    String getDetails(){
        return "\nFLIGHT ID: " + getFlightID() + "\nFROM: " + getFrom() + "\nTO: " + getTo() + "\nDEPARTURE TIME: " + getDepartTime()
                + "\nFLIGHT TIME (M): " + getFlightTime() + "\nARRIVAL TIME: " + getArrivalTime() + "\nPASSENGER ID: " + getPassengerID() + "\n";
    }
}
