import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteHandler
{
    Connection conn;
    Statement stat;

    public SQLiteHandler() {
        try {
            startDb();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() throws Exception
    {
        conn.close();
    }

    public void startDb() throws Exception
    {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        stat = conn.createStatement();
        //stat.executeUpdate("drop table if exists parkings;");
        stat.executeUpdate("create table if not EXISTS parkings (id integer PRIMARY KEY , resident INTEGER , used INTEGER , lat DOUBLE , lon DOUBLE , arr INTEGER , plate text);");
    }

    public void addData(ParkingSpot data) throws Exception
    {
        PreparedStatement prep = conn.prepareStatement("insert into parkings values (?, ?, ?, ?, ?, ?, ?);");

        prep.setBoolean(2, data.resident);
        prep.setBoolean(3, data.used);
        prep.setDouble(4, data.lat);
        prep.setDouble(5, data.lon);
        prep.setLong(6, data.arrivalTime);
        prep.setString(7, data.licencePlate);

        prep.addBatch();

        conn.setAutoCommit(false);
        prep.executeBatch();
        conn.setAutoCommit(true);

        System.out.println("ADD");
    }

    public ParkingSpot getSpot(int id) throws Exception
    {
        stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("select * from parkings where id = " + id + ";");
        ParkingSpot tmp = new ParkingSpot(
                rs.getInt(1),
                rs.getBoolean(2),
                rs.getBoolean(3),
                rs.getDouble(4),
                rs.getDouble(5),
                rs.getLong(6),
                rs.getString(7));
        rs.close();
        return tmp;
    }

    public List<ParkingSpot> getAll() throws Exception
    {
        stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("select * from parkings;");
        ParkingSpot tmp;
        List<ParkingSpot> list = new ArrayList<ParkingSpot>();
        while(rs.next())
        {
            tmp = new ParkingSpot(
                    rs.getInt(1),
                    rs.getBoolean(2),
                    rs.getBoolean(3),
                    rs.getDouble(4),
                    rs.getDouble(5),
                    rs.getLong(6),
                    rs.getString(7));
            list.add(tmp);
        }
        rs.close();
        return list;
    }





}
