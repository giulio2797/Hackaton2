import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

public class ParkingSpot {
    int id;
    boolean occupied = false;
    float lat;
    float lon;
    Marker marker;

    ParkingSpot(float lat, float lon, boolean occupied){
        this.lat = lat;
        this.lon = lon;
        this.occupied = occupied;
        MarkerOptions markerOptions = new MarkerOptions();
        LatLong pos = new LatLong(lat, lon);
        markerOptions.position(pos);
        marker = new Marker(markerOptions);
    }
}
