package model;

import dao.UserDAO;

public class LoginLogic {
    public boolean execute(User user) {
        if (user == null || user.getName() == null || user.getPass() == null) {
            return false;  // 無効なユーザー情報は失敗
        }
        UserDAO userDAO = new UserDAO();  // UserDAO を使ってデータベースを確認
        return userDAO.isValidUser(user);  // ユーザーの存在とパスワードの一致を確認
    }
}
