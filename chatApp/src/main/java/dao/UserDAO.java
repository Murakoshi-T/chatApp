package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

public class UserDAO {
    private final String JDBC_URL = "jdbc:h2:tcp://localhost/~/chatApp";
    private final String DB_USER = "sa";
    private final String DB_PASS = "";
    
    // ユーザー登録メソッド
    public boolean register(User user) {
        try {
            Class.forName("org.h2.Driver");  // JDBCドライバの読み込み
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("JDBCドライバを読み込めませんでした。");
        }
        String sql = "INSERT INTO USERS (NAME, PASS) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement pStmt = conn.prepareStatement(sql)) {
            pStmt.setString(1, user.getName());
            pStmt.setString(2, user.getPass());
            System.out.println("登録中のユーザー名: " + user.getName());
            System.out.println("登録中のパスワード: " + user.getPass());
            int result = pStmt.executeUpdate();
            System.out.println("結果: " + result);
            return result == 1;
        } catch (SQLException e) {
            System.err.println("SQLエラーコード: " + e.getErrorCode());
            System.err.println("SQL状態: " + e.getSQLState());
            e.printStackTrace();
            return false;
        }
    }
    
    // ユーザー認証メソッド
    public boolean isValidUser(User user) {
        try {
            Class.forName("org.h2.Driver");  // JDBCドライバの読み込み
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("JDBCドライバを読み込めませんでした。");
        }
        String sql = "SELECT COUNT(*) FROM USERS WHERE NAME = ? AND PASS = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement pStmt = conn.prepareStatement(sql)) {
            pStmt.setString(1, user.getName());
            pStmt.setString(2, user.getPass());
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;  // 該当ユーザーが存在すればtrue
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
