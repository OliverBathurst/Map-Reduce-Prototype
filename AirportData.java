
/**
 * An AirportData object stores information about a given airport, including name, latitude, and longitude
 */
class AirportData {
    private final String name, latitude, longitude;

    /**
     * An object must these three components, name, latitude, and longitude
     */
    AirportData(String name, String lat, String lon){
        this.name = name;
        this.latitude = lat;
        this.longitude = lon;
    }
    private String getName(){
        return name;
    }
    private String getLatitude(){
        return latitude;
    }
    private String getLongitude(){
        return longitude;
    }

    /**
     * Return details as a string
     */
    String getDetails(){
        return "\nAIRPORT NAME: " + getName() + "\n" + "AIRPORT LATITUDE: "
                + getLatitude() + "\n" + "AIRPORT LONGITUDE: " + getLongitude() + "\n";
    }
}