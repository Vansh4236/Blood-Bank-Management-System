package com.bloodbank.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.bloodbank.config.DatabaseConfig;

public class DBConnection {
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        try {
            if(connection == null || connection.isClosed()) {
                Class.forName(DatabaseConfig.DRIVER);
                connection = DriverManager.getConnection(
                    DatabaseConfig.URL, 
                    DatabaseConfig.USER, 
                    DatabaseConfig.PASSWORD
                );
                System.out.println("Database connected successfully!");
            }
            return connection;
        } catch(ClassNotFoundException e) {
            System.out.println("JDBC Driver not found!");
            throw new SQLException("JDBC Driver not found.", e);
        } catch(SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            throw e;
        }
    }

    public static void closeConnection() {
        if(connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed!");
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
