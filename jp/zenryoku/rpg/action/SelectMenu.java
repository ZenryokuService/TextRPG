package jp.zenryoku.rpg.action;

import jp.zenryoku.rpg.*;
import jp.zenryoku.rpg.utils.XMLUtil;
import jp.zenryoku.rpg.exception.RpgException;
import jp.zenryoku.rpg.views.InputSelector;
import jp.zenryoku.rpg.data.*;
import jp.zenryoku.rpg.data.config.*;
import jp.zenryoku.rpg.character.*;
import java.beans.PropertyChangeListener;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JTextArea;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import java.awt.Point;

/**
 * クラス TitleMenu の注釈をここに書きます.
 * テキストRPGの選択をするときに使用するクラス。
 * Scene.id: 選択ダイアログのタイトルになる
 *  
 * @author (Takunoji)
 * @version (1.0)
 */
public class SelectMenu extends AbstractAction
{
   /** 表示したテキストエリア */
   private static RpgTextArea textarea;
   /** 選択オブジェクト */
   private Select select;
   /** メインクラス */
   private TextRPGMain main;
    
   public SelectMenu() {
        super("titleMenu");
   }
    
   public SelectMenu(String str) {  
        super(str);
   }

   public SelectMenu(String str, RpgTextArea text) {
        super(str);
        this.textarea = text;
   }

   public SelectMenu(Select select, RpgTextArea text, TextRPGMain main) {
        super(select.getMongon());
        this.select = select;
        this.textarea = text;
        this.main = main;
   }

   
   public void actionPerformed(ActionEvent event) {
       String command =  event.getActionCommand();

       try {
           int sceneNo = select.getNextScene();
           Scene story = ConfigGenerator.getInstance().getScenes().get(sceneNo);
           if (story == null) {
               throw new RpgException("対応するストーリ番号がありまっせん。: " + sceneNo);
           }
           // シーンタイプの取得
           exeSceneType(command, story);

           // storyが未定義はPATH指定
           String st = story.getStory();
           if (st == null || "".equals(st)) {
               st = XMLUtil.loadText(story.getPath(), story.isCenter());
           }
           
           // 次のシーンを表示する
           JMenuItem item = (JMenuItem) event.getSource();           
           Point pos = item.getComponent().getLocation();
           ((InputSelector) item.getParent()).setVisible(false);

           InputSelector popup = new InputSelector(story, this.textarea, main);
           popup.show(main, (int)(pos.getX() + 350.0), (int)(pos.getY() + 500.0));
       } catch (Exception e) {
           e.printStackTrace();
           System.exit(-1);
       }
   }
   
   private void exeSceneType(String command, Scene story) throws RpgException {
       SceneType type = story.getSceneType();
       // ストーリーの表示
       textarea.setText(story.getStory());
       // それぞれのアアクション
       switch(type) {
           // 戦闘シーン
           case BATTLE:
               // TODO-[未実装]
               break;
           // 戦闘シーン
           case SHOP:
               // TODO-[未実装]
               break;
           // 戦闘シーン
           case EFFECT:
               // TODO-[未実装]
               break;
           // プレーヤー選択シーン
           case PLAYER_SELECT:
               // プレーヤー取得
               Player player = ConfigGenerator.getInstance()
                                   .getPlayers().get(command);
               
               main.setPlayer(player);
               break;
       }
   }
}
