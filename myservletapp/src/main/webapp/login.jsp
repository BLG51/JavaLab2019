<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Login page</title>
</head>
<body>
<form method="post" id="sort_by_value" action="login">
    <input name="email" type="email"><br><p>email</p>
    <input name="password" type="password"><br><p>password</p>
    <input name="remember_me" type="checkbox"><p>remember me</p>
    <input type="submit">
</form>
<div>
    <span>${warning}</span>
</div>
</body>
</html>