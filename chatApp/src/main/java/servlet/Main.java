package servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.MuttersDAO;
import model.GetMutterListLogic;
import model.Mutter;
import model.PostMutterLogic;
import model.User;

@WebServlet("/Main")
public class Main extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // アクションパラメータがある場合はdoActionを呼び出す
        String action = request.getParameter("action");
        if (action != null) {
            doAction(request, response);
            return;
        }

        // リストをセッションから取得
        HttpSession session = request.getSession();
        List<Mutter> mutterList = (List<Mutter>) session.getAttribute("mutterList");

        if (mutterList == null) {
            // セッションにリストがない場合、アプリケーションスコープから取得
            ServletContext application = this.getServletContext();
            mutterList = (List<Mutter>) application.getAttribute("mutterList");

            // アプリケーションスコープにもない場合はデータベースから取得
            if (mutterList == null) {
                GetMutterListLogic getMutterListLogic = new GetMutterListLogic();
                mutterList = getMutterListLogic.execute();
                // セッションとアプリケーション両方に保存
                session.setAttribute("mutterList", mutterList);
                application.setAttribute("mutterList", mutterList);
            }
        }

        // ログインユーザーがいるかチェック
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            response.sendRedirect("index.jsp");
        } else {
            request.setAttribute("mutterList", mutterList); // JSPにリストを渡す
            RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/main.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String text = request.getParameter("text");

        if (text != null && !text.isEmpty()) {
            HttpSession session = request.getSession();
            User loginUser = (User) session.getAttribute("loginUser");

            Mutter mutter = new Mutter(loginUser.getName(), text);
            PostMutterLogic postMutterLogic = new PostMutterLogic();
            boolean isPosted = postMutterLogic.execute(mutter);

            if (isPosted) {
                // 新しいつぶやきをセッションに格納
                List<Mutter> mutterList = (List<Mutter>) session.getAttribute("mutterList");
                mutterList.add(0, mutter); // 新しいつぶやきを先頭に追加
                session.setAttribute("mutterList", mutterList);
                response.sendRedirect("Main");
            } else {
                request.setAttribute("errorMsg", "つぶやきの保存に失敗しました。");
                RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/main.jsp");
                dispatcher.forward(request, response);
            }
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("errorMsg", "つぶやきが入力されていません");
            response.sendRedirect("Main");
        }
    }

    private void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        int mutterId = Integer.parseInt(request.getParameter("mutterId"));

        HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        HashMap<Integer, HashSet<String>> userActionHistory =
                (HashMap<Integer, HashSet<String>>) session.getAttribute("userActionHistory");
        if (userActionHistory == null) {
            userActionHistory = new HashMap<>();
            session.setAttribute("userActionHistory", userActionHistory);
        }

        // セッションからmutterListを取得
        List<Mutter> mutterList = (List<Mutter>) session.getAttribute("mutterList");
        if (mutterList == null) {
            response.sendRedirect("Main");
            return;
        }

        // 対象のMutterを見つける
        Mutter mutter = null;
        for (Mutter m : mutterList) {
            if (m.getId() == mutterId) {
                mutter = m;
                break;
            }
        }

        if (mutter != null) {
            HashSet<String> actions = userActionHistory.getOrDefault(mutterId, new HashSet<>());
            MuttersDAO dao = new MuttersDAO();

            // いいね・わるいねの処理
            try {
                if ("like".equals(action)) {
                    if (actions.contains("like")) {
                        mutter.addLike(-1); // いいね取り消し
                        actions.remove("like");
                    } else {
                        mutter.addLike(1); // いいね追加
                        actions.add("like");
                    }
                } else if ("dislike".equals(action)) {
                    if (actions.contains("dislike")) {
                        mutter.addDislike(-1); // わるいね取り消し
                        actions.remove("dislike");
                    } else {
                        mutter.addDislike(1); // わるいね追加
                        actions.add("dislike");
                    }
                }

                // データベースの更新
                if (dao.update(mutter)) {
                    // 更新後のアクション履歴をセッションに保存
                    userActionHistory.put(mutterId, actions);

                    // 更新後のmutterListをセッションに再設定
                    session.setAttribute("mutterList", mutterList);
                    System.out.println("Mutter ID " + mutterId + " がデータベースに更新されました。");
                } else {
                    System.err.println("データベースの更新に失敗しました: Mutter ID " + mutterId);
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMsg", "処理中にエラーが発生しました。");
                RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/main.jsp");
                dispatcher.forward(request, response);
                return;
            }
        }

        // 更新後、Mainページにリダイレクト
        response.sendRedirect("Main");
    }
}
