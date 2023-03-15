package jp.zenryoku.rpg.views;

import jp.zenryoku.rpg.action.BattleScene;
import jp.zenryoku.rpg.action.SelectMenu;
import jp.zenryoku.rpg.action.CommandAction;
import jp.zenryoku.rpg.exception.RpgException;
import jp.zenryoku.rpg.RpgTextArea;
import jp.zenryoku.rpg.TextRPGMain;
import jp.zenryoku.rpg.data.config.*;
import jp.zenryoku.rpg.character.*;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import java.util.List;

/**
 * クラス InputSelector の注釈をここに書きます.
 * テキストRPGの選択肢を表示するためのポップアップを作成する。
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
public class InputSelector extends JPopupMenu
{
    private static RpgTextArea textArea;
    private static TextRPGMain main;

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
    public InputSelector(String[] selects)  throws RpgException {
        super();
        addSeparator();

        for(String sel : selects) {
            SelectMenu act = new SelectMenu(sel);
            JMenuItem menu = new JMenuItem(act);
            add(menu);
            addSeparator();
        }
    }
    
    /**
     * バトルシーン用の
     * 選択肢のポップアップを作成する。
     * @param commands 配列の要素一つが選択肢一つに当たる
     */
    public InputSelector(Player player, Monster monster
            , TextRPGMain main, BattleScene battle)  throws RpgException {
        super();
        addSeparator();
        this.main = main;

        List<Command> commands = player.getJob().getCommandList();
        for(Command com: commands) {
            //System.out.println("*** Testing [" + com.getName() + "]");
            CommandAction act = new CommandAction(com, player, monster, main, battle);
            JMenuItem menu = new JMenuItem(act);
            add(menu);
            addSeparator();
        }
    }
    
    /**
     * 選択肢のポップアップを作成する。
     * @param selects 配列の要素一つが選択肢一つに当たる
     */
    public InputSelector(String title, List<Select> selects
                , RpgTextArea textarea, TextRPGMain main) throws RpgException  {
        super(title);
        addSeparator();
        for(Select sel : selects) {
            SelectMenu act = new SelectMenu(sel, textarea, main);
            JMenuItem menu = new JMenuItem(act);
            add(menu);
            addSeparator();
         }
    }
    
    /**
     * 選択肢のポップアップを作成する。
     * @param selects 配列の要素一つが選択肢一つに当たる
     */
    public InputSelector(Scene story, RpgTextArea textarea, TextRPGMain main) throws RpgException {
        super(story.getId());
        addSeparator();
        List<Select> selects = story.getSelects();
        if (selects == null) {
            createSingleSelect(story, textarea, main);
            return;
        }
        for(Select sel : selects) {
            SelectMenu act = new SelectMenu(sel, textarea, main);
            JMenuItem menu = new JMenuItem(act);
            add(menu);
            addSeparator();
         }
    }
    
    private void createSingleSelect(Scene story, RpgTextArea textarea, TextRPGMain main) {
            Select sel = new Select(story.getNextScene(), "すすむ");
            SelectMenu act = new SelectMenu(sel, textarea, main);
            JMenuItem menu = new JMenuItem(act);
            add(menu);
    }
    
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        try {
            finalize();
        } catch(Throwable t) {
            System.out.println("ポップアップの削除に失敗しました");
            System.exit(-1);
        }
    }
}
