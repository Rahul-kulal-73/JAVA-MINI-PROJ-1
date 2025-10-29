package com.yourdomain.todo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    // --- READ FROM ENVIRONMENT VARIABLES ---
    private static final String JDBC_URL = System.getenv("DB_URL");
    private static final String JDBC_USER = System.getenv("DB_USER");
    private static final String JDBC_PASSWORD = System.getenv("DB_PASSWORD");
    
    // Static block to load the driver once
    static {
        try {
            // FIX: Use the standard PostgreSQL JDBC Driver name
            Class.forName("org.postgresql.Driver"); 
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading PostgreSQL JDBC driver: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        // Validation check for debugging environment variable issues
        if (JDBC_URL == null || JDBC_URL.isEmpty() || JDBC_USER == null || JDBC_PASSWORD == null) {
            throw new SQLException("Database environment variables are not correctly set in Render (DB_URL, DB_USER, DB_PASSWORD).");
        }
        
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (SQLException e) {
            // Re-throw the exception with a more descriptive message
            System.err.println("Failed to connect to the database. Check DB_URL/USER/PASSWORD.");
            throw e; 
        }
        return connection;
    }

    public static void close(Connection connection) {
        if (connection != null) {
            try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
