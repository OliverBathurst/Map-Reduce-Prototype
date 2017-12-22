import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Oliver on 18/11/2017.
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

class Main {
    private static final ArrayList tempArrayList = new ArrayList<>();
    private static final Set tempHashSet = new HashSet();
    private static final StringBuilder flightConcat = new StringBuilder();

    public static void main(String[] args) {
        Config newConfig = new Config();
        newConfig.setMapper(map.class);
        newConfig.setReducer(reduce.class);
        newConfig.setTitle("Testing");
        newConfig.addInputPath(args[0]);
        newConfig.addInputPath(args[1]); //add as many input paths as you want
        newConfig.addOutputPath(args[2]);
        //newConfig.setChunkSize(256);//not really needed, alter based on file size
        newConfig.setShuffle(false);
        newConfig.setMultiThreaded(false); //multithreading is slower in this instance
        new Job(newConfig).runJob();
    }
    @SuppressWarnings({"WeakerAccess, Annotator"})
    public static class map {
        public map(String values, Context context) {
            String[] str = values.split(","); //split with comma (csv)
            if (str[0].matches("[A-Z]{3,20}") && str[1].matches("[A-Z]{3}") && str[2].matches("[0-9]{1,3}\\.[0-9]{3,13}") && str[3].matches("[0-9]{1,3}\\.[0-9]{3,13}")) {
                context.write(str[1], new AirportData(str[0], str[2], str[3]));
            }
            if (str[0].matches("[A-Z]{3}[0-9]{4}[A-Z]{2}[0-9]{1}") && str[1].matches("[A-Z]{3}[0-9]{4}[A-Z]{1}") && str[2].matches("[A-Z]{3}") && str[3].matches("[A-Z]{3}") && str[4].matches("[0-9]{10}") && str[5].matches("[0-9]{1,4}")) {
                context.write(str[1], new Flight(str[1], str[0], str[2], str[3], str[4], str[5]));
                context.write(str[2], str[1]); //key is airport ffa code, value is flight ID
            }
        }
    }
    @SuppressWarnings({"WeakerAccess", "Annotator", "unused", "unchecked"})
    public static class reduce {
        public reduce(Object key, Iterable<Object> values, Context context) {
            
            if (key.toString().matches("[A-Z]{3}[0-9]{4}[A-Z]{1}")) {
                int passengers = 0;
                flightConcat.setLength(0);

                for (Object val : values) {
                    if (val instanceof Flight) {
                        Flight flightData = (Flight) val;
                        flightConcat.append("\n" + "FLIGHT ID: ").append(flightData.getFlightID()).append("\n")
                                    .append("FROM: ").append(flightData.getFrom()).append("\n").append("TO: ")
                                    .append(flightData.getTo()).append("\n").append("DEPARTURE TIME: ")
                                    .append(flightData.getDepartTime()).append("\n").append("FLIGHT TIME (M): ")
                                    .append(flightData.getFlightTime()).append("\n").append("ARRIVAL TIME: ")
                                    .append(flightData.getArrivalTime()).append("\n").append("PASSENGER ID: ")
                                    .append(flightData.getPassengerID()).append("\n");
                        passengers++;
                    }
                }
                context.write(key, flightConcat.toString());
                context.write(key, "Passengers: " + passengers);
            }

            if (key.toString().matches("[A-Z]{3}")) {
                tempArrayList.clear();
                tempHashSet.clear();
                String airport = "";
                for (Object val : values) {
                    if (val instanceof AirportData) {
                        AirportData airportInfo = (AirportData) val;
                        airport = "AIRPORT NAME: " + airportInfo.getName() + "\n"
                                + "AIRPORT LATITUDE: " + airportInfo.getLatitude() + "\n"
                                + "AIRPORT LONGITUDE: " + airportInfo.getLongitude() + "\n";
                    }else{
                        tempArrayList.add(val);//it's a flight ID
                    }
                }
                tempHashSet.addAll(tempArrayList);
                context.write(key, airport + "NO. OF FLIGHTS FROM AIRPORT: " + tempHashSet.size() + "\n");
            }
        }
    }
}