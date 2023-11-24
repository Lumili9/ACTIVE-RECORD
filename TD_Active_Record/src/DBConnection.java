import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBConnection {
    private static Connection connection;

    static String userName = "root";
    static String password = "";
    static String serverName = "localhost";
    static String portNumber = "3306";
    static String dbName = "testpersonne";

    public static void setDbName(String dbName) {
        DBConnection.dbName = dbName;
    }

    public DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        if(connection == null){

            // creation de la connection
            Properties connectionProps = new Properties();
            connectionProps.put("user", DBConnection.userName);
            connectionProps.put("password", DBConnection.password);
            String urlDB = "jdbc:mysql://" + DBConnection.serverName + ":";
            urlDB += DBConnection.portNumber + "/" + DBConnection.dbName;
            DBConnection.connection = DriverManager.getConnection(urlDB, connectionProps);
        }
        return DBConnection.connection;
    }
}
