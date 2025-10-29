package com.yourdomain.todo.model;

import java.time.LocalDate;

public class Todo {
    private int id;
    private int userId;
    private String title;
    private String description;
    private LocalDate targetDate;
    private boolean isDone;

    // Default Constructor (REQUIRED)
    public Todo() {}

    // Constructor for creating new Todos
    public Todo(int userId, String title, String description, LocalDate targetDate, boolean isDone) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.targetDate = targetDate;
        this.isDone = isDone;
    }

    // Constructor for fetching existing Todos
    public Todo(int id, int userId, String title, String description, LocalDate targetDate, boolean isDone) {
        this(userId, title, description, targetDate, isDone);
        this.id = id;
    }

    // --- Getters and Setters (The methods TodoDAO is looking for) ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDate getTargetDate() { return targetDate; }
    public void setTargetDate(LocalDate targetDate) { this.targetDate = targetDate; }
    
    // Standard boolean getter is 'isDone()'
    public boolean isDone() { return isDone; } 
    public void setDone(boolean done) { isDone = done; }
}
