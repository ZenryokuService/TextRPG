package jp.zenryoku.rpg.action;

import jp.zenryoku.rpg.TextRPGMain;
import jp.zenryoku.rpg.RpgTextArea;
import jp.zenryoku.rpg.character.Player;
import jp.zenryoku.rpg.character.Monster;
import jp.zenryoku.rpg.ConfigGenerator;
import jp.zenryoku.rpg.exception.RpgException;


/**
 * クラス BattleSceme の注釈をここに書きます.
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
public class BattleScene
{
    /** モンスター */
    private Monster monster;

    public BattleScene(int low, int high) throws RpgException {
        try {
            monster = ConfigGenerator.getInstance().callMonster(low, high);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RpgException(e.getMessage());
        }
    }

    public void playScene(Player player, TextRPGMain main) {
        RpgTextArea area = main.getTextArea();
        area.setText("");
        if (monster.isTalk()) {
            area.setText(area.getText());
        }
        // TODO-[プレーヤーのコマンド一覧ポップアップ表示]
    }
}
