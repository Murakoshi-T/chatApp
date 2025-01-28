package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Mutter;

public class MuttersDAO {
    private final String JDBC_URL = "jdbc:h2:tcp://localhost/~/chatApp";
    private final String DB_USER = "sa";
    private final String DB_PASS = "";

    // 全てのつぶやきを取得
    public List<Mutter> findAll() {
        List<Mutter> mutterList = new ArrayList<>();
		//JDBCドライバを読み込む
		try {
			Class.forName("org.h2.Driver");
		}catch(ClassNotFoundException e) {
			throw new IllegalStateException(
					"JDBCドライバを読み込めませんでした");
			
		}
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT ID, NAME, TEXT, LIKE_COUNT, DISLIKE_COUNT FROM MUTTERS ORDER BY ID DESC";
            try (PreparedStatement pStmt = conn.prepareStatement(sql);
                 ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("ID");
                    String userName = rs.getString("NAME");
                    String text = rs.getString("TEXT");
                    int likeCount = rs.getInt("LIKE_COUNT");
                    int dislikeCount = rs.getInt("DISLIKE_COUNT");
                    Mutter mutter = new Mutter(id, userName, text, likeCount, dislikeCount);
                    mutterList.add(mutter);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mutterList;
    }

    // 新しいつぶやきを保存
    public boolean create(Mutter mutter) {
        boolean isSuccess = false;
        try {
            Class.forName("org.h2.Driver");  // JDBCドライバの読み込み
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("JDBCドライバを読み込めませんでした。");
        }
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
            String sql = "INSERT INTO MUTTERS(NAME, TEXT, LIKE_COUNT, DISLIKE_COUNT) VALUES(?, ?, 0, 0)";
            try (PreparedStatement pStmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pStmt.setString(1, mutter.getUserName());
                pStmt.setString(2, mutter.getText());
                int result = pStmt.executeUpdate();
                if (result == 1) {
                    try (ResultSet generatedKeys = pStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            mutter.setId(generatedKeys.getInt(1));  // 生成されたIDをセット
                            System.out.println("生成されたID: " + mutter.getId());
                        }
                    }
                    isSuccess = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("SQLエラーコード: " + e.getErrorCode());
            System.err.println("SQL状態: " + e.getSQLState());
            e.printStackTrace();
        }
        return isSuccess;
    }


    // 指定されたIDのつぶやきを取得
    public Mutter findById(int mutterId) {
        Mutter mutter = null;
		//JDBCドライバを読み込む
		try {
			Class.forName("org.h2.Driver");
		}catch(ClassNotFoundException e) {
			throw new IllegalStateException(
					"JDBCドライバを読み込めませんでした");
			
		}
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT ID, NAME, TEXT, LIKE_COUNT, DISLIKE_COUNT FROM MUTTERS WHERE ID = ?";
            try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
                pStmt.setInt(1, mutterId);
                try (ResultSet rs = pStmt.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt("ID");
                        String userName = rs.getString("NAME");
                        String text = rs.getString("TEXT");
                        int likeCount = rs.getInt("LIKE_COUNT");
                        int dislikeCount = rs.getInt("DISLIKE_COUNT");
                        mutter = new Mutter(id, userName, text, likeCount, dislikeCount);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mutter;
    }

    // つぶやきのいいねとわるいねのカウントを更新
    public boolean update(Mutter mutter) {
        boolean isSuccess = false;
		//JDBCドライバを読み込む
		try {
			Class.forName("org.h2.Driver");
		}catch(ClassNotFoundException e) {
			throw new IllegalStateException(
					"JDBCドライバを読み込めませんでした");
			
		}
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
            String sql = "UPDATE MUTTERS SET LIKE_COUNT = ?, DISLIKE_COUNT = ? WHERE ID = ?";
            try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
                pStmt.setInt(1, mutter.getLike());
                pStmt.setInt(2, mutter.getDislike());
                pStmt.setInt(3, mutter.getId());
                int result = pStmt.executeUpdate();
                isSuccess = result == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }
}
