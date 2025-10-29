package com.yourdomain.todo.controller;

import com.yourdomain.todo.dao.UserDAO;
import com.yourdomain.todo.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    // Declare the DAO field
    private UserDAO userDAO;

    // Initialize resources (DAO) when the servlet starts (Best Practice Fix)
    @Override
    public void init() throws ServletException {
        this.userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Just show the login form
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Input Validation Check
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("errorMessage", "Username and password are required.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        try {
            // Trim username before validation to match what was stored/processed during registration
            User user = userDAO.validateUser(username.trim(), password);
            
            if (user != null) {
                // SUCCESS: Create a session and set identifying attributes
                HttpSession session = request.getSession();
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                
                // Redirect to the To-Do list dashboard
                response.sendRedirect("list"); 
            } else {
                // FAILURE: Invalid credentials
                request.setAttribute("errorMessage", "Invalid username or password.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // Log the exception (e.g., database connection failure) and show a generic error
            e.printStackTrace();
            request.setAttribute("errorMessage", "Login failed due to an internal server error. Check your database connection.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
