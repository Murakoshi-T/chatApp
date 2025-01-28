<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>ログイン結果</title>
</head>
<body>
    <h1>ログイン結果</h1>
    <c:choose>
        <c:when test="${not empty loginUser}">
            <p>ようこそ、${loginUser.name} さん。</p>
            <p><a href="Main">メインページへ</a></p>
        </c:when>
        <c:otherwise>
            <p>ログインに失敗しました。ユーザー名またはパスワードが違います。</p>
            <p><a href="index.jsp">戻る</a></p>
        </c:otherwise>
    </c:choose>
</body>
</html>
