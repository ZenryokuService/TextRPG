package jp.zenryoku.rpg;

import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import jp.zenryoku.rpg.utils.FileReaderUtil;
import jp.zenryoku.rpg.utils.StoryBuffer;
import jp.zenryoku.rpg.action.TitleMenu;
import jp.zenryoku.rpg.views.InputSelector;
import jp.zenryoku.rpg.data.config.Story;
import jp.zenryoku.rpg.character.Player;

/**
 * クラス RpgProgress の注釈をここに書きます.
 * テキストRPGのゲーム進行を行う。以下の処理を行う。
 * 
 * 1. ロード済みのストーリー(シーン)を呼び出す。
 * 2. シーンのストーリー表示。
 * 3. 入力受付。
 * 4. 結果の表示。
 * 5. 次のシーンへ移動。
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
public class RpgProgress
{
    /** テキストエリア */
    private RpgTextArea fView;
    /** JFrame */
    private JFrame frame;
    /** ストーリーオブジェクトリスト */
    private Map<Integer, Story> storyMap;
    /** プレーヤー */
    private Player player;
    
    /** コンストラクタ */
    public RpgProgress(JFrame frame, RpgTextArea view) {
        // ここに実行するための準備を実装する
        this.fView = view;
    }

    /**
     * テキストRPGのシーンを呼び出し実行する。
     * プログラムのメイン処理フローを実装。
     * @param comp JTextArea
     */
    public void run(int x, int y) {
         // - 1. Storyの表示
         StringBuilder build = FileReaderUtil.readFile("title.txt", true);
         fView.setText(build.toString());
         // - 2. 入力を受ける
         openPopup(x, y);
         // - 3. 結果の表委
         // - 4. 次のシーンへ移動
    }
    
    /**
     * ユーザーの入力を促すためのポップアップを表示する。
     * @param JTextArea　文字列を表示クラス
     * @param int ポップアップの横幅
     * @param int ポップアップの縦幅
     */
    private void openPopup(int x, int y) {
        String[] yesNo = new String[] {TitleMenu.NEW_GAME, TitleMenu.CONTINUE};
        InputSelector pop = new InputSelector("First", yesNo, this.fView);
        pop.show(this.frame, x + 150, y + 280);
    }
}
