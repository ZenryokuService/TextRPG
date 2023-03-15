package jp.zenryoku.rpg.action;

import jp.zenryoku.rpg.TextRPGMain;
import jp.zenryoku.rpg.RpgTextArea;
import jp.zenryoku.rpg.character.Player;
import jp.zenryoku.rpg.character.Monster;
import jp.zenryoku.rpg.ConfigGenerator;
import jp.zenryoku.rpg.exception.RpgException;
import jp.zenryoku.rpg.data.config.Command;
import jp.zenryoku.rpg.data.config.Scene;
import jp.zenryoku.rpg.views.InputSelector;

import java.awt.Point;
import java.util.List;

/**
 * クラス BattleScene の注釈をここに書きます.
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
public class BattleScene extends Scene
{
    public static final String SEP = System.lineSeparator();
    /** モンスター */
    private Monster monster;
    /** 決着フラグ */
    private boolean battle;
    /** メインフレーム */
    private TextRPGMain main;

    public BattleScene(TextRPGMain main, Scene story) throws RpgException {
       int low = story.getMonsterNoLow();
       int high = story.getMonsterNoHigh();
       try {
            monster = ConfigGenerator.getInstance().callMonster(low, high);
            setNextScene(story.getNextScene());
       } catch (Exception e) {
            e.printStackTrace();
            throw new RpgException(e.getMessage());
       }
       
       battle = true;
       this.main = main;
       RpgTextArea area = main.getTextArea();
       area.setText(monster.getName() + "が現れた。");

       if (monster.isTalk()) {
           String mess = area.getText() + SEP + "「" + monster.getMessage() + "」";
           area.setText(mess);
       }
    }

    public boolean playScene(Player player, Point pos) throws RpgException {
       // TODO-[プレーヤーのコマンド一覧ポップアップ表示]
       List<Command> cmdList = player.getJob().getCommandList();
       InputSelector popup = new InputSelector(player, monster, main, this);
       popup.show(main, (int)(pos.getX() + 350.0), (int)(pos.getY() + 500.0));
       
       return false;
    }
    
    public boolean isBattle() {
        return battle;
    }
}
