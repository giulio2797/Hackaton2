package DB;

import java.sql.*;

public class DBConnector {

    public static Connection connect (String host, int port, String dbName, String user, String psw){
        Connection DBConn = null;
        try {
            String dbString = null;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            dbString = "jdbc:mysql://" + host + "/" + dbName;
            DBConn = DriverManager.getConnection(dbString, user, psw);
        } catch (Exception e){
            e.printStackTrace();
        }
        return DBConn;
    }

    private static void executeStatement(String s, Connection con){
        try{
            Statement stmt = con.createStatement();
            stmt.execute(s);
            stmt.close();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Query: "+s);
        }
    }
}
