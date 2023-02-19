package jp.zenryoku.rpg.action;

import java.beans.PropertyChangeListener;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JTextArea;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

/**
 * クラス TitleMenu の注釈をここに書きます.
 * テキストRPGの選択をするときに使用するクラス。
 *  
 * @author (Takunoji)
 * @version (1.0)
 */
public class TitleMenu extends AbstractAction
{
    /** ニューゲーム */
    public static final String NEW_GAME = "new_game";
    /** コンテニュー */
    public static final String CONTINUE = "coninue";
    /** 表示したテキストエリア */
    private static JTextArea textarea;
    
    public TitleMenu() {
        super("titleMenu");
    }
    
    public TitleMenu(String str) {  
        super(str);
    }

    public TitleMenu(String str, JTextArea text) {
        super(str);
        this.textarea = text;
    }

   public void actionPerformed(ActionEvent event) {
       String selectedStr =  event.getActionCommand();
        
       textarea.setText(selectedStr);
       JMenuItem item = (JMenuItem) event.getSource();
       ((JPopupMenu) item.getParent()).setVisible(false);
   }
}
