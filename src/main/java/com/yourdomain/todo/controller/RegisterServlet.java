package com.yourdomain.todo.controller;

import com.yourdomain.todo.dao.UserDAO;
import com.yourdomain.todo.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    
    // Declare the DAO field
    private UserDAO userDAO;

    // Initialize resources (DAO) when the servlet starts
    @Override
    public void init() throws ServletException {
        // We initialize the DAO here, preventing potential issues with direct field initialization
        this.userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Simply forward to the registration form
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Basic input validation to prevent empty submissions
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("errorMessage", "Username and password cannot be empty.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        User newUser = new User();
        newUser.setUsername(username.trim()); // Trim whitespace
        newUser.setPassword(password); // Password will be hashed in DAO

        try {
            if (userDAO.registerUser(newUser)) {
                // Registration successful, send message and forward to login page
                request.setAttribute("successMessage", "Registration successful! You can now log in.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                // This typically catches SQLIntegrityConstraintViolation (duplicate username)
                request.setAttribute("errorMessage", "Registration failed. The username may already be taken.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // Log the stack trace for debugging on the server side
            e.printStackTrace();
            request.setAttribute("errorMessage", "An internal error occurred during registration. Please try again.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
