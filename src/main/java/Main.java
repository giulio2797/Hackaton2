import DB.DBConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application
{
    @Override
    public void start(Stage stage) throws Exception {
        ArrayList<ParkingSpot> Spots = new ArrayList<ParkingSpot>();
        DBConnector DB = new DBConnector();
        Connection con = DB.connect("localhost", 3306, "test", "admin", "admin");
        Timer timer = new Timer();
        Parent root = FXMLLoader.load(getClass().getResource("/Scene.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("Smart Parking -- Admin view");
        stage.setScene(scene);
        stage.show();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Spots.clear();
                ResultSet results = DB.executeQuery("select * from filari", con);
                try{
                    while (results.next()){
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
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, 10*1000);
    }


    public static void main(String[] args) {
        launch(args);
    }
}