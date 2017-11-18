/**
 * Created by Oliver on 18/11/2017.
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

@SuppressWarnings("unused")
class AirportData {
    private final String code, latitude, longitude;

    AirportData(String code, String lat, String lon){
        this.code = code;
        this.latitude = lat;
        this.longitude = lon;
    }
    String getCode(){
        return code;
    }
    String getLatitude(){
        return latitude;
    }
    String getLongitude(){
        return longitude;
    }
}
