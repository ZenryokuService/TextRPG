package jp.zenryoku.rpg.views;

import jp.zenryoku.rpg.action.TitleMenu;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
/**
 * クラス InputSelector の注釈をここに書きます.
 * テキストRPGの選択肢を表示するためのポップアップを作成する。
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
public class InputSelector extends JPopupMenu
{
    /**
     * 選択肢のポップアップを作成する。
     * @param selects 配列の要素一つが選択肢一つに当たる
     */
    public InputSelector(String[] selects) {

        for(String sel : selects) {
            TitleMenu act = new TitleMenu(sel);
            JMenuItem menu = new JMenuItem(act);
            add(menu);
        }
    }
    
    public void show(JFrame frame, int xPos, int yPos) {
        show(frame, xPos, yPos);
    }
}
