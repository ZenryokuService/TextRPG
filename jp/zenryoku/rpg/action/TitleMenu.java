package jp.zenryoku.rpg.action;

import java.beans.PropertyChangeListener;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
/**
 * クラス TitleMenu の注釈をここに書きます.
 * テキストRPGの選択をするときに使用するクラス。
 *  
 * @author (Takunoji)
 * @version (1.0)
 */
public class TitleMenu extends AbstractAction
{
    public TitleMenu() {
        super("titleMenu");
    }
    
    public TitleMenu(String str) {
        super(str);
    }
 
   public void actionPerformed(ActionEvent event) {
        String selectedStr =  event.getActionCommand();
        
   }
}
