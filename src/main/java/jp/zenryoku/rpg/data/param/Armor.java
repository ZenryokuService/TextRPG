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
    private Formula spcial;
    /** 防具の副作用 */
    private Formula demerit;

    public Armor() { 
    }
    
    public Armor(String id, String name, int value, Formula sp, Formula dem) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.spcial = sp;
        this.demerit = dem;
    }
}
