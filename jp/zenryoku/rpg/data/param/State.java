package jp.zenryoku.rpg.data.param;

import lombok.Data;
import java.util.List;
import jp.zenryoku.rpg.data.Formula;
/**
 * クラス State の注釈をここに書きます.
 * プレーヤーの状態ステータスを表す。
 * Config.xmlから状態部分を取得、生成する。
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
@Data
public class State
{
    /** 行動不能を示す固定の効果式 */
    public static final String NAC = "NAC";
    /** 状態のキー */
    private String key;
    /** 状態を示す　*/
    private Formula state;
    
    public State() {
        key = "Normal";
        state = null;
    }
}
