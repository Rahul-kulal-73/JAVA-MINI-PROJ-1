package com.yourdomain.todo.controller;

import com.yourdomain.dao.TodoDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import com.yourdomain.todo.model.Todo; 

@WebServlet({"/list", "/new", "/insert", "/delete", "/edit", "/update", "/toggle", "/logout"})
public class TodoServlet extends HttpServlet {
    private TodoDAO todoDAO = new TodoDAO();

    // A simple method to check if a user is logged in
    private boolean isAuthenticated(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login"); 
            return false;
        }
        return true;
    }

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
                // ... (cases for /new, /edit, /delete, /toggle)
                case "/list":
                default:
                    listTodo(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        if (!isAuthenticated(request, response)) return;
        
        // ... (handle POST actions like /insert and /update)
        
        response.sendRedirect("list"); // Default redirect after POST action
    }

    private void listTodo(HttpServletRequest request, HttpServletResponse response) 
            throws Exception, IOException, ServletException {
        int userId = (int) request.getSession().getAttribute("userId");
        // Fetch list of todos from the DAO
        request.setAttribute("listTodo", todoDAO.selectAllTodos(userId));
        request.getRequestDispatcher("todo-list.jsp").forward(request, response);
    }
}
