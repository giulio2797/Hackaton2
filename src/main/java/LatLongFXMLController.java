import DB.DBConnector;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.*;

import com.lynden.gmapsfx.util.MarkerImageFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;

public class LatLongFXMLController implements Initializable {

    @FXML
    private Label latitudeLabel;

    @FXML
    private Label longitudeLabel;

    @FXML
    private GoogleMapView googleMapView;

    private GoogleMap map;

    private DecimalFormat formatter = new DecimalFormat("###.00000");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        googleMapView.addMapInializedListener(() -> configureMap());
    }

    protected void configureMap() {
        ArrayList<ParkingSpot> Spots = new ArrayList<ParkingSpot>();
        DBConnector DB = new DBConnector();
        Connection con = DB.connect("localhost", 3306, "test", "admin", "admin");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Spots.clear();
                ResultSet results = DB.executeQuery("select * from filari", con);
                try {
                    while (results.next()) {
                        int filareId = results.getInt(1);
                        int id = results.getInt(2);
                        boolean occupied = results.getBoolean(1);
                        Date arrivalTime = results.getTime(1);
                        String targa = results.getString(1);
                        float lat = results.getFloat(1);
                        float lon = results.getFloat(2);
                        ParkingSpot parkingSpot = new ParkingSpot(filareId, id, arrivalTime, targa, lat, lon, occupied);
                        Spots.add(parkingSpot);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 10 * 1000);


        MapOptions mapOptions = new MapOptions();

        mapOptions.center(new LatLong(46.071358, 11.124731))  //Trento centro
                .mapType(MapTypeIdEnum.ROADMAP)
                .zoom(14);
        map = googleMapView.createMap(mapOptions, false);

        map.addMouseEventHandler(UIEventType.click, (GMapMouseEvent event) -> {
            LatLong latLong = event.getLatLong();
            double lat = latLong.getLatitude();
            double lon = latLong.getLongitude();

            latitudeLabel.setText(formatter.format(lat));
            longitudeLabel.setText(formatter.format(lon));
            map.clearMarkers();
            map.addMarker(createMarker(latLong));
        });
    }

    private Marker createMarker(LatLong pos)
    {
        MarkerOptions options = new MarkerOptions();
        options.position(pos);
        options.icon((new MarkerImageFactory()).createMarkerImage("/blue.png", "png"));
        Marker marker = new Marker(options);


        return marker;
    }
}









































