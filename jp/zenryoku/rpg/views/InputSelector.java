package jp.zenryoku.rpg.views;

import jp.zenryoku.rpg.action.TitleMenu;
import jp.zenryoku.rpg.RpgTextArea;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

/**
 * クラス InputSelector の注釈をここに書きます.
 * テキストRPGの選択肢を表示するためのポップアップを作成する。
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
public class InputSelector extends JPopupMenu
{
    private static JTextArea textArea;

    /**
     * Storyクラスから必要な情報を取得して
     * それぞれのシーンに対応した、文章とActionを生成する。
     */
    public InputSelector() {
        super();
        addSeparator();
        // Storyファイルのデータ
    }
    /**
     * 選択肢のポップアップを作成する。
     * @param selects 配列の要素一つが選択肢一つに当たる
     */
    public InputSelector(String[] selects) {
        super();
        addSeparator();

        for(String sel : selects) {
            TitleMenu act = new TitleMenu(sel);
            JMenuItem menu = new JMenuItem(act);
            add(menu);
            addSeparator();
        }
    }
    
    /**
     * 選択肢のポップアップを作成する。
     * @param selects 配列の要素一つが選択肢一つに当たる
     */
    public InputSelector(String title, String[] selects, RpgTextArea textarea) {
        super(title);
        addSeparator();
        for(String sel : selects) {
            TitleMenu act = new TitleMenu(sel, textarea);
            JMenuItem menu = new JMenuItem(act);
            add(menu);
            addSeparator();
         }
    }
    
    
}
