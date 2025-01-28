package model;
import java.io.Serializable;

public class Mutter implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;  // ID追加
    private String userName;
    private String text;
    private int like;
    private int dislike;

    public Mutter(int id, String userName, String text, int like, int dislike) {
        this.id = id;
        this.userName = userName;
        this.text = text;
        this.like = like;
        this.dislike = dislike;
    }

    public Mutter(String userName, String text) {
        this(0, userName, text, 0, 0);  // IDは初期値0
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }

    public int getLike() {
        return like;
    }

    public int getDislike() {
        return dislike;
    }

    public void addLike(int value) {
        this.like += value;
    }

    public void addDislike(int value) {
        this.dislike += value;
    }
}
