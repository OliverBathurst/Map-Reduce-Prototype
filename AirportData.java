/**
 * Created by Oliver on 18/11/2017.
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

/**
 * An AirportData object stores information about a given airport, including name, latitude, and longitude
 */
class AirportData {
    private final String name, latitude, longitude;

    /**
     * An object must these three components
     */
    AirportData(String name, String lat, String lon){
        this.name = name;
        this.latitude = lat;
        this.longitude = lon;
    }
    String getName(){
        return name;
    }
    String getLatitude(){
        return latitude;
    }
    String getLongitude(){
        return longitude;
    }
}