<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Register - ToDo App</title>
    <link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
    <div class="container">
        <h1>Register New User</h1>
        
        <c:if test="${not empty errorMessage}">
            <p class="error-message">${errorMessage}</p> 
        </c:if>

        <form action="register" method="post">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>

            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>

            <button type="submit">Register</button>
        </form>
        
        <p>Already have an account? <a href="login">Login here</a></p>
    </div>
</body>
</html>