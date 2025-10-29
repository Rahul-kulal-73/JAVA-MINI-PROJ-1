<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>
        <c:choose>
            <c:when test="${todo != null}">Edit Todo</c:when>
            <c:otherwise>Add New Todo</c:otherwise>
        </c:choose>
    </title>
    <link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
    <div class="container">
        <h1>
            <c:choose>
                <c:when test="${todo != null}">Edit Existing Todo</c:when>
                <c:otherwise>Create New Todo</c:otherwise>
            </c:choose>
        </h1>
        
        <p><a href="list">Back to List</a></p>
        
        <c:url var="formAction" value="${todo != null ? 'update' : 'insert'}" />
        
        <form action="${formAction}" method="post">
            
            <c:if test="${todo != null}">
                <input type="hidden" name="id" value="${todo.id}" />
            </c:if>
            
            <label for="title">Title:</label>
            <input type="text" id="title" name="title" value="${todo.title}" required>

            <label for="description">Description:</label>
            <textarea id="description" name="description">${todo.description}</textarea>
            
            <label for="targetDate">Target Date:</label>
            <input type="date" id="targetDate" name="targetDate" value="${todo.targetDate}" required>

            <c:if test="${todo != null}">
                <label for="isDone">Completed:</label>
                <input type="checkbox" id="isDone" name="isDone" ${todo.done ? 'checked' : ''}>
            </c:if>

            <button type="submit">
                <c:choose>
                    <c:when test="${todo != null}">Save Changes</c:when>
                    <c:otherwise>Add Todo</c:otherwise>
                </c:choose>
            </button>
        </form>
    </div>
</body>
</html>