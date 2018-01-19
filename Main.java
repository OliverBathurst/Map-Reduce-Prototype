import java.util.*;

class Main {
    public static void main(String[] args) {
        Config newConfig = new Config();//create new config object
        newConfig.setMapperClass(map.class);//setup map and reduce classes
        newConfig.setReducerClass(reduce.class);
        newConfig.setTitle("AirportDataMapReduce");//set job name
        newConfig.setNumReduceTasks(5);
        newConfig.addInputPath(args[0]);
        newConfig.addInputPath(args[1]); //add as many input paths as you want
        newConfig.addOutputPath(args[2]);//set output path
        newConfig.setMultiThreaded(false); //multithreading is slower in this instance
        new Job(newConfig).runJob();//pass config to job and run job
    }

    /**
     * The user-customised map method
     */
    static class map {
        map(String values, Context context) {
            String[] str = values.split(","); //split with comma (csv file)
            if (str[0].matches("[A-Z]{3,20}") && str[1].matches("[A-Z]{3}") && str[2].matches("[0-9]{1,3}\\.[0-9]{3,13}") && str[3].matches("[0-9]{1,3}\\.[0-9]{3,13}")) {
                context.write(str[1], new AirportData(str[0], str[2], str[3]));//if it matches an airport entry, create new Airport obj using airport code as key
            }
            if (str[0].matches("[A-Z]{3}[0-9]{4}[A-Z]{2}[0-9]") && str[1].matches("[A-Z]{3}[0-9]{4}[A-Z]") && str[2].matches("[A-Z]{3}") && str[3].matches("[A-Z]{3}") && str[4].matches("[0-9]{10}") && str[5].matches("[0-9]{1,4}")) {
                context.write(str[1], new Flight(str[1], str[0], str[2], str[3], str[4], str[5]));//key is flight ID, value a flight object with the required info
                context.write(str[2], str[1]);//key is the "from" airport ffa code, value is flight ID, for calculating flights from each airport
            }
        }
    }

    /**
     * The user-customised reduce method
     */
    static class reduce {
        @SuppressWarnings("StringConcatenationInLoop")
        reduce(Object key, Iterable<Object> values, Context context) {
            if (key.toString().matches("[A-Z]{3}[0-9]{4}[A-Z]")) {//if the key matches a flight ID
                Set<Object> set = new HashSet<>();//storage for the unique passenger IDs on the flight to calculate no. passengers
                String flight = "";//store flight info for that flight ID

                for (Object val : values) {//iterate over values
                    if (val instanceof Flight) {//if the value is a flight object
                        Flight flightData = (Flight) val;//cast it to Flight
                        if(!set.contains(flightData.getPassengerID())){ //if passenger ID hasn't been added yet
                            set.add(flightData.getPassengerID());//add the passenger ID to the list
                            flight += flightData.getDetails();//print flight details containing new passenger ID
                        } //don't print flight details when there's a duplicate passenger ID
                    }
                }
                context.write(key, flight);//write the flight details with flight ID as key
                context.write(key, "PASSENGERS: " + set.size());//get number of unique passengers on flight with passenger ID set
            }

            if (key.toString().matches("[A-Z]{3}")) {// if the key matches an airport code
                Set<Object> flightIDs = new HashSet<>();//for storing flight IDs from airport
                String airport = "";//string to store airport data
                for (Object val : values) {
                    if (val instanceof AirportData) {//if the value is an airport data object
                        airport = ((AirportData) val).getDetails();//assign the details to string
                    }else{//otherwise the value must be a flight ID FROM that airport (departing)
                        flightIDs.add(val);//add the flight ID to hash set, hash set does not allow for duplicates so size() returns number of unique flight IDs leaving airport
                    }
                }//write airport details and number of flights from airport
                context.write(key, airport + "NO. OF FLIGHTS FROM AIRPORT: " + flightIDs.size() + "\n");
            }
        }
    }
}