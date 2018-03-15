package DB;

import java.sql.*;

public class DBConnector {

    public Connection connect (String host, int port, String dbName, String user, String psw){
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

    public void executeStatement(String command, Connection con){
        try{
            Statement stmt = con.createStatement();
            stmt.execute(command);
            stmt.close();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Query: "+command);
        }
    }

    public ResultSet executeQuery(String query, Connection con){
        try{
            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery(query);
            return results;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
