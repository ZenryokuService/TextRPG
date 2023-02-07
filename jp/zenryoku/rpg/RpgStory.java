package jp.zenryoku.rpg;

import javax.swing.JComponent;
import jp.zenryoku.rpg.utils.FileReaderUtil;
import jp.zenryoku.rpg.views.InputSelector;
/**
 * クラス RpgStory の注釈をここに書きます.
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
public class RpgStory
{
    private RpgTextArea view;
    
    /** コンストラクタ */
    public RpgStory(RpgTextArea view) {
        // ここに実行するための準備を実装する
        this.view = view;
    }

    /**
     * テキストRPGのシーンを呼び出し実行する。
     * プログラムのメイン処理フローを実装。
     */
    public void run(JComponent comp, int x, int y) {
         // - 1. Storyの表示
         StringBuilder build = FileReaderUtil.readFile("title.txt", true);
         view.setText(build.toString());
         // - 2. 入力を受ける
         openSelects(comp, x, y);
         // - 3. 結果の表委
         // - 4. 次のシーンへ移動
    }
    
    private void openSelects(JComponent comp, int x, int y) {
        String[] yesNo = new String[] {"start", "continue"};
        InputSelector pop = new InputSelector(yesNo);
        pop.show(comp, x, y);
    }
}
