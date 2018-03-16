import java.util.Date;

public class ParkingSpotV2 {
    int id;
    boolean resident, used;
    double lat, lon;
    long arrivalTime;
    String licencePlate;


    public ParkingSpotV2(int id, boolean resident, boolean used, double lat, double lon, long arrivalTime, String licencePlate) {
        this.id = id;
        this.resident = resident;
        this.used = used;
        this.lat = lat;
        this.lon = lon;
        if (used == false)
        {
            this.arrivalTime = -1;
            this.licencePlate = "";
        } else if (arrivalTime != -1)
            {
                this.arrivalTime = arrivalTime;
                this.licencePlate = licencePlate;
            } else
                {
                    this.arrivalTime = System.currentTimeMillis();
                    this.licencePlate = licencePlate;
                }
    }
}
