import java.sql.*;

public class DBConfig {
    public static Connection getConnection() throws SQLException {
        try {
            // 1. Load the driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 2. Database URL
            String url = "jdbc:mysql://localhost:3306/quiz_system";
            
            // 3. Credentials
            String user = "root";
            String pass = "root"; // <--- Set to 'root' as you mentioned
            
            return DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found! Check your Build Path JAR.");
        } catch (SQLException e) {
            // This will catch if the password 'root' is still wrong
            throw new SQLException("Database connection failed: " + e.getMessage());
        }
    }
}
