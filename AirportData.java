/**
 * Created by Oliver on 18/11/2017.
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

@SuppressWarnings("unused")
class AirportData {
    private final String name, latitude, longitude;

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
