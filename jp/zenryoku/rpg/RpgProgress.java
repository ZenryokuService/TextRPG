package jp.zenryoku.rpg;

import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import jp.zenryoku.rpg.utils.FileReaderUtil;
import jp.zenryoku.rpg.utils.StoryBuffer;
import jp.zenryoku.rpg.action.SelectMenu;
import jp.zenryoku.rpg.views.InputSelector;
import jp.zenryoku.rpg.data.config.Scene;
import jp.zenryoku.rpg.character.Player;
import jp.zenryoku.rpg.ConfigGenerator;
import jp.zenryoku.rpg.exception.RpgException;

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
@Deprecated
public class RpgProgress
{
    /** テキストエリア */
    private RpgTextArea fView;
    /** TextRPGMain */
    private TextRPGMain frame;
    /** ストーリーオブジェクトリスト */
    private Map<Integer, Scene> storyMap;
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
    public void run(int x, int y) throws RpgException {
         ConfigGenerator conf = ConfigGenerator.getInstance();
         Map<Integer, Scene> stories = conf.getScenes();
         int sceneNo = 0;
         Scene story = stories.get(sceneNo);
         // - 1. Storyの表示
         printStory(story);
         // - 2. 入力を受ける
         openPopup(x, y, story);
         // - 3. 結果の表委
         // - 4. 次のシーンへ移動
         sceneNo = story.getNextScene();
         //System.out.println("次は" + sceneNo);
    }
    
    /**
     * シーンオブジェクトからストーリーを取得して表示する。
     */
    private void printStory(Scene story) {
        this.fView.setText(story.getStory());
    }
    /**
     * ユーザーの入力を促すためのポップアップを表示する。
     * @param JTextArea　文字列を表示クラス
     * @param int ポップアップの横幅
     * @param int ポップアップの縦幅
     */
    private void openPopup(int x, int y, Scene story) throws RpgException {
        InputSelector pop = new InputSelector(story, this.fView, this.frame);
        pop.show(this.frame, x + 150, y + 280);
    }
}
