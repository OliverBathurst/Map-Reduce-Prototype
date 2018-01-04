import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A Flight object stores information about a flight to be retrieved in the Reduce method
 */
class Flight {
    private final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); //convert date into a nice format
    private final String passengerID, fromAirport, toAirport, flightTime, flightID, departureTime;

    /**
     * Initialise flight object with flight ID, passenger ID, from airport, to airport, departure time and flight time
     */
    Flight(String flightID, String passengerID, String from, String to, String departureTime, String flightTime){
        this.flightID = flightID;
        this.passengerID = passengerID;
        this.fromAirport = from;
        this.toAirport = to;
        this.flightTime = flightTime;
        this.departureTime = departureTime;//convert into epoch time (ms)
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
    /**
     * Converts the epoch time from s to ms, creates new date and formats it
     */
    private String getDepartTime(){
        return format.format(new Date(Long.parseLong(departureTime) * 1000L));
    }
    private String getFlightTime(){
        return flightTime;
    }
    /**
     * Converts departure time from s to ms (epoch time) and adds the flight time in ms to get a new epoch time
     * Creates a new date object with the resulting epoch time to get the arrival time
     * The date is then formatted and returned
     */
    private String getArrivalTime(){
        return format.format(new Date((Long.parseLong(departureTime) * 1000L) + (Long.parseLong(flightTime) * 60000L)));
    }

    /**
     * Return all details as a string
     */
    String getDetails(){
        return "\nFLIGHT ID: " + getFlightID() + "\nFROM: " + getFrom() + "\nTO: " + getTo() + "\nDEPARTURE TIME: " + getDepartTime()
                + "\nFLIGHT TIME (M): " + getFlightTime() + "\nARRIVAL TIME: " + getArrivalTime() + "\nPASSENGER ID: " + getPassengerID() + "\n";
    }
}
