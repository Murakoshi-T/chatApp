package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.LoginLogic;
import model.User;

@WebServlet("/Login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name").trim();//入力間違いを防ぐ
        String pass = request.getParameter("pass").trim();//入力間違いを防ぐ

        User user = new User(name, pass);
        LoginLogic loginLogic = new LoginLogic();
        boolean isLogin = loginLogic.execute(user);

        if (isLogin) {
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", user);
            response.sendRedirect("Main");  // ログイン成功時はリダイレクトでメインへ
        } else {
            request.getSession().invalidate();				//ログインできなかった場合はフォワードでエラー文表示
            request.setAttribute("loginError", "ユーザー名またはパスワードが間違っています。");
            RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
            dispatcher.forward(request, response);
        }

    }
}