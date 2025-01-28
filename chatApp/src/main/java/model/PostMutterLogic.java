package model;

import dao.MuttersDAO;

public class PostMutterLogic {
    public boolean execute(Mutter mutter) {
        MuttersDAO dao = new MuttersDAO();
        boolean result = dao.create(mutter);
        if (!result) {
            System.err.println("つぶやきの保存に失敗しました: " + mutter.getText());
        }
        return result;
    }
}
