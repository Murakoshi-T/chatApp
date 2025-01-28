<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="model.User, model.Mutter, java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>つぶやきアプリ</title>
</head>
<body>
    <h1>つぶやき一覧</h1>

    <!-- エラーメッセージ表示 -->
    <c:if test="${not empty sessionScope.errorMsg}">
        <p style="color: red;">${sessionScope.errorMsg}</p>
        <!-- エラーメッセージ表示後にセッションから削除 -->
        <c:remove var="errorMsg" scope="session" />
    </c:if>

    <!-- つぶやき投稿フォーム -->
    <form action="Main" method="post">
        <input type="text" name="text" placeholder="つぶやきを入力" required />
        <input type="submit" value="投稿" />
    </form>

    <!-- つぶやきリストの表示 -->
    <c:forEach var="mutter" items="${mutterList}">
        <p>
            <c:out value="${mutter.userName}" />: <c:out value="${mutter.text}" />
            <br />
            <!-- いいね・わるいねの表示 -->
            👍<c:out value="${mutter.like}" />
            <a href="<c:url value='Main?action=like&mutterId=${mutter.id}' />">[いいね]</a>
            👎<c:out value="${mutter.dislike}" />
            <a href="<c:url value='Main?action=dislike&mutterId=${mutter.id}' />">[わるいね]</a>
        </p>
        <hr />
    </c:forEach>

    <!-- ログアウトリンク -->
    <p><a href="Logout">ログアウト</a></p>
</body>
</html>
