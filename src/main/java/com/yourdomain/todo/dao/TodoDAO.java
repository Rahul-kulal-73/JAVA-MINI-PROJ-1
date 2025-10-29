package com.yourdomain.todo.dao;

import com.yourdomain.todo.model.Todo;
import com.yourdomain.todo.util.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TodoDAO {
    // JDBC SQL Constants
    private static final String INSERT_TODO_SQL = "INSERT INTO todos (user_id, title, description, target_date, is_done) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_TODOS_BY_USER = "SELECT * FROM todos WHERE user_id = ? ORDER BY target_date, id";
    private static final String SELECT_TODO_BY_ID = "SELECT * FROM todos WHERE id = ? AND user_id = ?";
    private static final String UPDATE_TODO_SQL = "UPDATE todos SET title = ?, description = ?, target_date = ?, is_done = ? WHERE id = ? AND user_id = ?";
    private static final String DELETE_TODO_SQL = "DELETE FROM todos WHERE id = ? AND user_id = ?";
    private static final String TOGGLE_STATUS_SQL = "UPDATE todos SET is_done = NOT is_done WHERE id = ? AND user_id = ?";

    private Todo mapResultSetToTodo(ResultSet rs) throws SQLException {
        return new Todo(
            rs.getInt("id"),
            rs.getInt("user_id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getDate("target_date").toLocalDate(),
            rs.getBoolean("is_done")
        );
    }
    
    // --- CREATE ---
    public void insertTodo(Todo todo) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_TODO_SQL)) {
            ps.setInt(1, todo.getUserId());
            ps.setString(2, todo.getTitle());
            ps.setString(3, todo.getDescription());
            ps.setDate(4, Date.valueOf(todo.getTargetDate()));
            ps.setBoolean(5, todo.isDone());
            ps.executeUpdate();
        }
    }

    // --- READ ALL ---
    public List<Todo> selectAllTodos(int userId) {
        List<Todo> todos = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ALL_TODOS_BY_USER)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    todos.add(mapResultSetToTodo(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todos;
    }

    // --- READ ONE (for editing) ---
    public Todo selectTodo(int todoId, int userId) {
        Todo todo = null;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_TODO_BY_ID)) {
            ps.setInt(1, todoId);
            ps.setInt(2, userId); // Ensures user can only fetch their own todos (Security)
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    todo = mapResultSetToTodo(rs);
                }
            }
        } catch (SQLException e) {
             e.printStackTrace();
        }
        return todo;
    }

    // --- UPDATE ---
    public boolean updateTodo(Todo todo) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_TODO_SQL)) {
            ps.setString(1, todo.getTitle());
            ps.setString(2, todo.getDescription());
            ps.setDate(3, Date.valueOf(todo.getTargetDate()));
            ps.setBoolean(4, todo.isDone());
            ps.setInt(5, todo.getId());
            ps.setInt(6, todo.getUserId()); 
            return ps.executeUpdate() > 0;
        }
    }

    // --- DELETE ---
    public boolean deleteTodo(int todoId, int userId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_TODO_SQL)) {
            ps.setInt(1, todoId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    // --- TOGGLE STATUS ---
    public boolean toggleTodoStatus(int todoId, int userId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(TOGGLE_STATUS_SQL)) {
            ps.setInt(1, todoId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }
}
