package sample.database;
import java.sql.*;

public class DatabaseHandler {
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dbConnection = DriverManager.getConnection(
                    "jdbc:sqlserver://localhost:1433;databaseName=HousingSocietyManagement;selectMethod=cursor", "sa", "123456");
        return dbConnection;
    }
}
