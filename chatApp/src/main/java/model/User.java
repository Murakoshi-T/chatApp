package model;
import java.io.Serializable;

public class User implements Serializable {
    private String name; // ユーザー名
    private String pass; // パスワード

    // 引数なしコンストラクタで初期化
    public User() {
        this.name = "";  // 空文字列で初期化
        this.pass = "";  // 空文字列で初期化
    }

    public User(String name, String pass) {
        this.name = name != null ? name : "";  // nullの場合は空文字列
        this.pass = pass != null ? pass : "";  // nullの場合は空文字列
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }
}
