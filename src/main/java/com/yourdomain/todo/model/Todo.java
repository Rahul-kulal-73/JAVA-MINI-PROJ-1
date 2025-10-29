package com.yourdomain.todo.model;

import java.time.LocalDate;

public class Todo {
    private int id;
    private int userId;
    private String title;
    // ... all other fields (description, targetDate, isDone)
    
    // Constructors (omitted for brevity)
    
    // Getters and Setters (omitted for brevity)
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    // ... complete with all methods ...
}