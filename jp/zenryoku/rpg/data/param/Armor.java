package jp.zenryoku.rpg.data.param;

import lombok.Data;
import jp.zenryoku.rpg.data.Formula;
/**
 * クラス Armor の注釈をここに書きます.
 * 
 * @author (あなたの名前)
 * @version (バージョン番号もしくは日付)
 */
@Data
public class Armor extends Item
{
    /** 防具の攻撃力 */
    private int value;
    /** 防具の特殊能力 */
    private Formula formula;
    
    public Armor() { 
    }
    
    public Armor(String name, int value, Formula f) {
        this.name = name;
        this.value = value;
        this.formula = f;
    }
}
