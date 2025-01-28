<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>ログインページ</title>
</head>
<body>
    <h1>ログイン</h1>
    <form action="Login" method="post">
        <label for="name">ユーザー名:</label>
        <input type="text" id="name" name="name" required />
        <br />
        <label for="pass">パスワード:</label>
        <input type="password" id="pass" name="pass" required />
        <br />
        <input type="submit" value="ログイン" />
    </form>

   <p><a href="Register">登録はこちら</a></p> <!-- サーブレット経由のリンク -->

    <c:if test="${not empty loginError}">
        <p style="color: red;">${loginError}</p>
    </c:if>
</body>
</html>
