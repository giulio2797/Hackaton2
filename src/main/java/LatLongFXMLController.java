import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.StateEventHandler;
import com.lynden.gmapsfx.javascript.event.UIEventHandler;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

import com.lynden.gmapsfx.util.MarkerImageFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Callback;
import netscape.javascript.JSObject;

public class LatLongFXMLController implements Initializable {

    @FXML
    private Label latitudeLabel;
    Random rn;

    @FXML
    private Label longitudeLabel;

    @FXML
    private GoogleMapView googleMapView;

    private GoogleMap map;
    private List<Marker> localMarkers;
    private DecimalFormat formatter = new DecimalFormat("###.00000");

    SQLiteHandler db;
    MapOptions mapOptions;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        localMarkers = new ArrayList<>();
        rn = new Random();
        mapOptions = new MapOptions();
        db = new SQLiteHandler();
        googleMapView.addMapInializedListener(() -> configureMap());
    }

    protected void configureMap() {

        mapOptions.center(new LatLong(46.071358, 11.124731))  //Trento centro
                .mapType(MapTypeIdEnum.ROADMAP)
                .zoom(14);
        map = googleMapView.createMap(mapOptions, false);

        try {
            ArrayList<ParkingSpot> spots = (ArrayList<ParkingSpot>) db.getAll(); //Poca eleganza...
            if (!spots.isEmpty()) {
                ParkingSpot tmp = spots.get(0);
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

        map.addMarkers(localMarkers, UIEventType.click, new Callback<Marker, UIEventHandler>() {
            @Override
            public UIEventHandler call(Marker param) {
                return new UIEventHandler() {
                    @Override
                    public void handle(JSObject jso) {
                        displayInfoWindow(new LatLong((JSObject) jso.getMember("latLng")), param);
                    }
                };
            }
        });
    }

    private char randomLetter()
    {
        return (char)(rn.nextInt('Z'-'A') + 'A');
    }
    InfoWindow window;
    private void displayInfoWindow(LatLong loc, Marker m) {
        if (window != null ){window.close();}
        InfoWindowOptions infoOptions = new InfoWindowOptions();
        infoOptions.content("<p>Licence plate: " + randomLetter() + randomLetter() + (rn.nextInt(900) + 100) + randomLetter() + randomLetter() + "</p><p>At parking number: " + rn.nextInt(2000) + "</p>" +
                "<p>Since: "+ (rn.nextInt(23) + 2) + ":" + rn.nextInt(60) +" </p>");
        window = new InfoWindow(infoOptions);
        window.open(map, m);
    }


    private void addParking(ParkingSpot tmp)
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

        if (type == 0) {
            options.icon((new MarkerImageFactory()).createMarkerImage("/blue.png", "png"));

        } else if (type == 1) {
            options.icon((new MarkerImageFactory()).createMarkerImage("/red.png", "png"));

        } else if (type == 2) {
            options.icon((new MarkerImageFactory()).createMarkerImage("/green.png", "png"));

        } else {
        }

        Marker marker = new Marker(options);
        localMarkers.add(marker);
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
            db.addData(new ParkingSpot(-1, (new Random()).nextBoolean(), (new Random()).nextBoolean(), pos.getLatitude(), pos.getLongitude(), (new Random()).nextInt(3)-2, ""));
        } catch (Exception e) {
            e.printStackTrace();
        }

        localMarkers.add(marker);
        return marker;
    }
}









































