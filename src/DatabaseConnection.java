import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String url = "jdbc:sqlserver://localhost:1433;databaseName=PT_XYZ_Otomotif;encrypt=false;trustServerCertificate=false;";
    private static final String username = "Fikhri";
    private static final String password = "Fikhri12";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
