import java.util.*;

/**
 * Created by Oliver on 18/11/2017.
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

class Main {
    public static void main(String[] args) {
        Config newConfig = new Config();
        newConfig.setMapperClass(map.class);
        newConfig.setReducerClass(reduce.class);
        newConfig.setTitle("AirportDataMapReduce");
        newConfig.addInputPath(args[0]);
        newConfig.addInputPath(args[1]); //add as many input paths as you want
        newConfig.addOutputPath(args[2]);
        newConfig.setMultiThreaded(true); //multithreading is slower in this instance
        new Job(newConfig).runJob();
    }
    static class map {
        map(String values, Context context) {
            String[] str = values.split(","); //split with comma (csv)
            if (str[0].matches("[A-Z]{3,20}") && str[1].matches("[A-Z]{3}") && str[2].matches("[0-9]{1,3}\\.[0-9]{3,13}") && str[3].matches("[0-9]{1,3}\\.[0-9]{3,13}")) {
                context.write(str[1], new AirportData(str[0], str[2], str[3]));
            }
            if (str[0].matches("[A-Z]{3}[0-9]{4}[A-Z]{2}[0-9]") && str[1].matches("[A-Z]{3}[0-9]{4}[A-Z]") && str[2].matches("[A-Z]{3}") && str[3].matches("[A-Z]{3}") && str[4].matches("[0-9]{10}") && str[5].matches("[0-9]{1,4}")) {
                context.write(str[1], new Flight(str[1], str[0], str[2], str[3], str[4], str[5]));
                context.write(str[2], str[1]); //key is airport ffa code, value is flight ID
            }
        }
    }
    static class reduce {
        reduce(Object key, Iterable<Object> values, Context context) {

            if (key.toString().matches("[A-Z]{3}[0-9]{4}[A-Z]")) {
                Set<Object> set = new HashSet<>();
                String flight = "";

                for (Object val : values) {
                    if (val instanceof Flight) {
                        Flight flightData = (Flight) val;
                        if(!set.contains(flightData.getPassengerID())){ //if passenger ID hasn't been encountered yet
                            set.add(flightData.getPassengerID());//add the passenger ID to the list as new
                            flight += flightData.getDetails();//print flight details
                        } //don't print flight details when there's a duplicate passenger ID
                    }
                }
                context.write(key, flight);
                context.write(key, "Passengers: " + set.size());
            }

            if (key.toString().matches("[A-Z]{3}")) {
                Set<Object> flightIDs = new HashSet<>();
                String airport = "";
                for (Object val : values) {
                    if (val instanceof AirportData) {
                        airport = ((AirportData) val).getDetails();
                    }else{//it's a flight ID
                        flightIDs.add(val);
                    }
                }
                context.write(key, airport + "NO. OF FLIGHTS FROM AIRPORT: " + flightIDs.size() + "\n");
            }
        }
    }
}