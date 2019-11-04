package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static Connection conn;

    //    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    //private static final String url = "jdbc:postgresql://ssuthlzhkrlhuw:5f142aec9070f918f5b364bc9f4d4e9efef4e96345c49c5f3e910fc075da9c9e@ec2-54-247-96-169.eu-west-1.compute.amazonaws.com:5432/d9hd1hrn63ek0o?ssl=true&sslmode=require&sslfactory=org.postgresql.ssl.NonValidatingFactory";
    public static Connection getConnection(){
        if (conn == null) {
            try {
                Properties props = new Properties();
                props.setProperty("user", "postgres");
                props.setProperty("password","postgres");
                conn = DriverManager.getConnection(url, props);
                System.out.println("Connection to DB has been established.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return conn;
    }

    public static void closeConnection(){
        try {
            if (conn != null) {
                conn.close();
                conn = null;
                System.out.println("Connection to DB has been closed.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
