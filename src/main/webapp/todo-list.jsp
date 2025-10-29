<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>My To-Do List</title>
    <link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
    <div class="header">
        <h1>Welcome, ${sessionScope.username}!</h1>
        <p>
            <a href="new"><button style="background-color: #28a745;">Add New Todo</button></a> 
            <a href="logout"><button style="background-color: #dc3545;">Logout</button></a>
        </p>
    </div>
    
    <table>
        <thead>
            <tr>
                <th>Title</th>
                <th>Description</th>
                <th>Due Date</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="todo" items="${listTodo}">
                <tr class="${todo.done ? 'done' : ''}">
                    <td>${todo.title}</td>
                    <td>${todo.description}</td>
                    <td>${todo.targetDate}</td>
                    <td>
                        <c:choose>
                            <c:when test="${todo.done}">Completed</c:when>
                            <c:otherwise>Pending</c:otherwise>
                        </c:choose>
                    </td>
                    <td class="action-buttons">
                        <a href="edit?id=<c:out value='${todo.id}' />">Edit</a> 
                        &nbsp;&nbsp;&nbsp;
                        <a href="toggle?id=<c:out value='${todo.id}' />" style="color: <c:out value='${todo.done ? "#ffc107" : "#007bff"}' />;">
                            <c:out value='${todo.done ? "Mark Pending" : "Mark Done"}' />
                        </a>
                        &nbsp;&nbsp;&nbsp;
                        <a href="delete?id=<c:out value='${todo.id}' />" style="color: #dc3545;" onclick="return confirm('Are you sure you want to delete this task?');">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>