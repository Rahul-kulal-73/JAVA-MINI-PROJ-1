package com.yourdomain.todo.dao;

import com.yourdomain.todo.model.User;
import com.yourdomain.todo.util.DBConnection;
import at.favre.lib.crypto.bcrypt.BCrypt; 
import java.sql.*;

public class UserDAO {
    private static final String INSERT_USER = "INSERT INTO users (username, password) VALUES (?, ?)";
    private static final String SELECT_USER_BY_USERNAME = "SELECT user_id, username, password FROM users WHERE username = ?";

    public boolean registerUser(User user) throws SQLException {
        // HASH the password before storing!
        String hashedPassword = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_USER)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, hashedPassword);
            return ps.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
             // Catch duplicate entry error (e.g., username already exists)
             return false;
        }
    }

    public User validateUser(String username, String password) throws SQLException {
        User user = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_USER_BY_USERNAME)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    
                    // Verify password against stored hash
                    BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), storedHash.toCharArray());
                    
                    if (result.verified) {
                        user = new User();
                        user.setId(rs.getInt("user_id"));
                        user.setUsername(rs.getString("username"));
                        // Do NOT copy the hash into the user object
                    }
                }
            }
        }
        return user;
    }
}
