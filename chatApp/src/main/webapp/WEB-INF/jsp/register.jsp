<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="model.User" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>ユーザー登録</title>
</head>
<body>
    <h1>ユーザー登録</h1>
    <form action="Register" method="post">
        <label for="name">ユーザー名:</label>
        <input type="text" id="name" name="name" required />
        <br />
        <label for="pass">パスワード:</label>
        <input type="password" id="pass" name="pass" required />
        <br />
        <input type="submit" value="登録" />
    </form>

    <c:if test="${not empty errorMsg}">
        <p style="color: red;">${errorMsg}</p>
    </c:if>
    <c:if test="${not empty successMsg}">
        <p style="color: green;">${successMsg}</p>
    </c:if>
</body>
</html>
