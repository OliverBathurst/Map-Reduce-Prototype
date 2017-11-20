import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Oliver on 18/11/2017.
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

class Main {
    static ArrayList tempArrayList = new ArrayList<>();

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
                context.write(str[2],str[1]); //key is airport ffa code, value is flight ID
            }
        }
    }
    @SuppressWarnings("WeakerAccess")
    public static class reduce {
        @SuppressWarnings("unused")
        public reduce(Object key, Iterable<Object> values, Context context) {

            if (key.toString().matches("[A-Z]{3}[0-9]{4}[A-Z]{1}")) {
                String valuesToAdd = "";
                int passengers = 0;

                for (Object val : values) {
                    if (val instanceof ArrayList) {
                        for (Object flight : (ArrayList) val) {
                            if (flight instanceof Flight) {
                                Flight flightData = (Flight) flight;
                                valuesToAdd += "\n" + "FLIGHT ID: " + flightData.getFlightID() + "\n" + "FROM: " + flightData.getFrom() + "\n" +
                                        "TO: " + flightData.getTo() + "\n" + "DEPARTURE TIME: " + flightData.getDepartTime()
                                        + "\n" + "FLIGHT TIME (M): " + flightData.getFlightTime() + "\n" + "ARRIVAL TIME: " + flightData.getArrivalTime() + "\n"
                                        + "PASSENGER ID: " + flightData.getPassengerID() + "\n";
                                passengers++;
                            }
                        }
                    }
                }
                context.write(key, valuesToAdd);
                context.write(key, "Passengers: " + passengers);
            }

            if (key.toString().matches("[A-Z]{3}")) {
                tempArrayList.clear();
                String airport = "";
                for (Object val : values) {
                    if (val instanceof ArrayList) {
                        for (Object air : (ArrayList) val) {
                            if (air instanceof AirportData) {
                                AirportData airportInfo = (AirportData) air;
                                airport = "AIRPORT NAME: " + airportInfo.getName() + "\n"
                                        + "AIRPORT LATITUDE: " + airportInfo.getLatitude() + "\n"
                                        + "AIRPORT LONGITUDE: " + airportInfo.getLongitude() + "\n";
                            }else{
                                tempArrayList.add(air);//it's a flight ID
                            }
                        }
                    }
                }
                context.write(key, airport + "NO. OF FLIGHTS: " + new HashSet(tempArrayList).size() + "\n");
            }
        }
    }
}