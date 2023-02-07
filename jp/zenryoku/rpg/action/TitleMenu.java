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
    /*
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        
    }
    
    public boolean isEnabled() {
        return false;
    }
    
    public void putValue(String key, Object value) {
        
    }
    
    public void setEnabled(boolean b) {
        
    }
    
    public Object getValue(String key) {
        return null;
    }
   public void init() {
       
   }
    */
   
   public void actionPerformed(ActionEvent event) {
        System.out.println("Message: ");
   }
}
