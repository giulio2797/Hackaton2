import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
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
        Marker marker = new Marker(options);


        return marker;
    }
}









































