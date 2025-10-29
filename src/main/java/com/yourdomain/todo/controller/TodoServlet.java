package com.yourdomain.todo.controller;

// --- CRITICAL FIXES ---
// 1. Corrected DAO import: must be com.yourdomain.todo.dao
import com.yourdomain.todo.dao.TodoDAO;
import com.yourdomain.todo.model.Todo;
// --- END FIXES ---

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.sql.SQLException;

@WebServlet({"/list", "/new", "/insert", "/delete", "/edit", "/update", "/toggle", "/logout"})
public class TodoServlet extends HttpServlet {
    
    private TodoDAO todoDAO; // Field declaration
    
    // Initialize the DAO instance when the Servlet is created
    public void init() {
        this.todoDAO = new TodoDAO();
    }
    
    // Helper to get the logged-in user ID safely
    private int getUserId(HttpServletRequest request) {
        // We assume 'userId' is safely in the session due to successful login
        return (int) request.getSession().getAttribute("userId");
    }

    // A simple method to check if a user is logged in
    private boolean isAuthenticated(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login"); 
            return false;
        }
        return true;
    }

    // --- doGet Implementation: Handles Read, Delete, Toggle Status, and Forms ---
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getServletPath();
        
        if ("/logout".equals(action)) {
            request.getSession().invalidate();
            response.sendRedirect("login");
            return;
        }
        
        if (!isAuthenticated(request, response)) return;

        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/delete":
                    deleteTodo(request, response);
                    break;
                case "/toggle":
                    toggleTodoStatus(request, response);
                    break;
                case "/list":
                default:
                    listTodo(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    // --- doPost Implementation: Handles Insert and Update ---
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        if (!isAuthenticated(request, response)) return;
            
        String action = request.getServletPath();
        try {
            switch (action) {
                case "/insert":
                    insertTodo(request, response);
                    break;
                case "/update":
                    updateTodo(request, response);
                    break;
                default:
                    listTodo(request, response); // Fallback to list
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    // --- CRUD GET Handlers ---

    private void listTodo(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException {
        int userId = getUserId(request);
        // Fetch list of todos from the DAO and set it as a request attribute
        request.setAttribute("listTodo", todoDAO.selectAllTodos(userId));
        request.getRequestDispatcher("todo-list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("todo-form.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        int userId = getUserId(request);
        
        Todo existingTodo = todoDAO.selectTodo(id, userId);
        
        if (existingTodo != null) {
            request.setAttribute("todo", existingTodo);
            request.getRequestDispatcher("todo-form.jsp").forward(request, response);
        } else {
            // Handle case where todo doesn't exist or doesn't belong to the user
            response.sendRedirect("list"); 
        }
    }

    private void deleteTodo(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        int userId = getUserId(request);
        todoDAO.deleteTodo(id, userId);
        response.sendRedirect("list");
    }
    
    private void toggleTodoStatus(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        int userId = getUserId(request);
        todoDAO.toggleTodoStatus(id, userId);
        response.sendRedirect("list");
    }

    // --- CRUD POST Handlers ---

    private void insertTodo(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        int userId = getUserId(request);
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        
        // Handle date parsing (assuming input format is correct)
        LocalDate targetDate = LocalDate.parse(request.getParameter("targetDate"));

        Todo newTodo = new Todo(userId, title, description, targetDate, false);
        todoDAO.insertTodo(newTodo);
        response.sendRedirect("list");
    }
    
    private void updateTodo(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        int userId = getUserId(request);
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        LocalDate targetDate = LocalDate.parse(request.getParameter("targetDate"));
        
        // Checkbox only sends a value if checked, so null check works
        boolean isDone = request.getParameter("isDone") != null;

        Todo updatedTodo = new Todo(id, userId, title, description, targetDate, isDone);
        todoDAO.updateTodo(updatedTodo);
        response.sendRedirect("list");
    }
}
