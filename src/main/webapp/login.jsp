<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login - ToDo App</title>
    <link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
    <div class="container">
        <h1>To-Do App Login</h1>
        
        <c:if test="${not empty successMessage}">
            <p style="color: green;">${successMessage}</p> 
        </c:if>
        <c:if test="${not empty errorMessage}">
            <p class="error-message">${errorMessage}</p> 
        </c:if>

        <form action="login" method="post">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>

            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>

            <button type="submit">Login</button>
        </form>
        
        <p>Don't have an account? <a href="register">Register here</a></p>
    </div>
</body>
</html>