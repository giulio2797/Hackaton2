import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import java.util.Date;

public class ParkingSpot {
    int filareId;
    int id;
    Date arrivalTime;
    String targa;
    boolean occupied = false;
    boolean residente = false;
    float lat;
    float lon;
    Marker marker;

    ParkingSpot(int filareId, int id, Date arrivalTime, String targa, float lat, float lon, boolean occupied, boolean residente){
        this.id = id;
        this.filareId = filareId;
        this.arrivalTime=arrivalTime;
        this.targa = targa;
        this.lat = lat;
        this.lon = lon;
        this.occupied = occupied;
        this.residente = residente;
        MarkerOptions markerOptions = new MarkerOptions();
        LatLong pos = new LatLong(lat, lon);
        markerOptions.position(pos);
        marker = new Marker(markerOptions);
    }
}
