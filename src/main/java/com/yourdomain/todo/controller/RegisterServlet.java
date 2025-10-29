package com.yourdomain.todo.controller;

import com.yourdomain.todo.dao.UserDAO;
import com.yourdomain.todo.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password); // Password will be hashed in DAO

        try {
            if (userDAO.registerUser(newUser)) {
                // Registration successful, redirect to login
                request.setAttribute("successMessage", "Registration successful! Please log in.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                // User already exists or other DB issue
                request.setAttribute("errorMessage", "Registration failed. Username may already exist.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Registration failed due to an internal error.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}