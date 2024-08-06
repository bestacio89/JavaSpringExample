package Utilities;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String DB_URL = System.getenv("SPRING_DATASOURCE_URL");
    private static final String DB_USER = System.getenv("SPRING_DATASOURCE_USER");
    private static final String DB_PASSWORD = System.getenv("SPRING_DATASOURCE_PASSWORD");
    private static final String DB_NAME = System.getenv("SPRING_DATASOURCE_NAME");

    public static void createDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Statement statement = connection.createStatement();
            String sql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            statement.executeUpdate(sql);
            System.out.println("Database created or already exists.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
