<%@ page import="java.util.List" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="ru.javalab.myservletapp.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Таблица пользователей</title>
</head>
<body>
<form method="post" id="sort_by_value" action="table">
    <div>
        <input type="radio" name="sort" id="sort_email" value="sort_email_value">
        <label for="sort_email">
            Sort Email
        </label>
    </div>
    <div >
        <input type="radio" name="sort" id="sort_password" value="sort_password_value">
        <label for="sort_password">
            Sort Password
        </label>
    </div>
    <input type="submit">
    <table class="table">
        <thead>
        <tr>
            <th>Email</th>
            <th>Password</th>
            <th>Country</th>
            <th>About</th>
        </tr>
        </thead>
        <tbody>

        <c:forEach var="user" items="${list}">
        <tr>
            <td>${user.email}
            </td>
            <td>${user.password}
            </td>
            <td>${user.country}
            </td>
            <td>${user.about}
            </td>
        </tr>
        </c:forEach>

        </tbody>
    </table>
</form>
<div>
    <span>${warning}</span>
</div>
</body>
</html>