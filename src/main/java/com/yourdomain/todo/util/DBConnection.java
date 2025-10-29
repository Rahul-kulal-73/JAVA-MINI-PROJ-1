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
            // Ensure the correct driver name for your DB (e.g., "org.postgresql.Driver" for Postgres)
            Class.forName("com.mysql.cj.jdbc.Driver"); 
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading JDBC driver: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        // Will now fail if DB_URL is not set in Render environment
        if (JDBC_URL == null || JDBC_URL.isEmpty()) {
            throw new SQLException("Database environment variables are not set.");
        }
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }
    
    // ... (rest of the close method)
}