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

    SQLiteHandler db;
    MapOptions mapOptions;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapOptions = new MapOptions();
        db = new SQLiteHandler();
        googleMapView.addMapInializedListener(() -> configureMap());
    }

    protected void configureMap() {
//        DBConnector DB = new DBConnector();
//        Connection con = DB.connect("localhost", 3306, "test", "admin", "admin");
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Spots.clear();
//                ResultSet results = DB.executeQuery("select * from filari", con);
//                try {
//                    while (results.next()) {
//                        int filareId = results.getInt(1);
//                        int id = results.getInt(2);
//                        boolean occupied = results.getBoolean(1);
//                        //boolean resident = results.getBoolean(1);
//                        Date arrivalTime = results.getTime(1);
//                        String targa = results.getString(1);
//                        float lat = results.getFloat(1);
//                        float lon = results.getFloat(2);
//                        ParkingSpot parkingSpot = new ParkingSpot(filareId, id, arrivalTime, targa, lat, lon, occupied, false);
//                        Spots.add(parkingSpot);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 10 * 1000);

        mapOptions.center(new LatLong(46.071358, 11.124731))  //Trento centro
                .mapType(MapTypeIdEnum.ROADMAP)
                .zoom(14);
        map = googleMapView.createMap(mapOptions, false);

        try {
            ArrayList<ParkingSpotV2> spots = (ArrayList<ParkingSpotV2>) db.getAll(); //Poca eleganza...
            if (!spots.isEmpty()) {
                ParkingSpotV2 tmp = spots.get(0);
                while(tmp != null)
                {
                    addParking(tmp);
                    spots.remove(0);
                    if (!spots.isEmpty())
                    {
                        tmp = spots.get(0);
                    } else { tmp = null; }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        map.addMouseEventHandler(UIEventType.click, (GMapMouseEvent event) -> {
            LatLong latLong = event.getLatLong();
            double lat = latLong.getLatitude();
            double lon = latLong.getLongitude();

            latitudeLabel.setText(formatter.format(lat));
            longitudeLabel.setText(formatter.format(lon));
           // map.clearMarkers();
            map.addMarker(createMarker(latLong, 1));
        });
    }

    private void addParking(ParkingSpotV2 tmp)
    {
        int type = 1;
        if (tmp.used != false && tmp.resident == true)
        {
            type = 0;
        }
        if (tmp.used == false)
        {
            type = 2;
        }
        map.addMarker( createMarkerUpdate( new LatLong(tmp.lat, tmp.lon), type) );
    }

    private Marker createMarkerUpdate(LatLong pos, int type)
    {
        MarkerOptions options = new MarkerOptions();
        options.position(pos);

        switch (type)
        {
            case 0:
                options.icon((new MarkerImageFactory()).createMarkerImage("/blue.png", "png"));
                break;
            case 1:
                options.icon((new MarkerImageFactory()).createMarkerImage("/red.png", "png"));
                break;
            case 2:
                options.icon((new MarkerImageFactory()).createMarkerImage("/green.png", "png"));
                break;
            default:
                break;
        }

        Marker marker = new Marker(options);
        return marker;
    }

    private Marker createMarker(LatLong pos, int type)
    {
        MarkerOptions options = new MarkerOptions();
        options.position(pos);

        switch (type)
        {
            case 0:
                options.icon((new MarkerImageFactory()).createMarkerImage("/blue.png", "png"));
                break;
            case 1:
                options.icon((new MarkerImageFactory()).createMarkerImage("/red.png", "png"));
                break;
            case 2:
                options.icon((new MarkerImageFactory()).createMarkerImage("/green.png", "png"));
                break;
            default:
                break;
        }

        Marker marker = new Marker(options);

        try {
            db.addData(new ParkingSpotV2(-1, (new Random()).nextBoolean(), (new Random()).nextBoolean(), pos.getLatitude(), pos.getLongitude(), (new Random()).nextInt(3)-2, ""));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return marker;
    }
}









































