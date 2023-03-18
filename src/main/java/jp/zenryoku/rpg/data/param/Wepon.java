package jp.zenryoku.rpg.data.param;

import lombok.Data;
import jp.zenryoku.rpg.data.Formula;
/**
 * クラス Wepon の注釈をここに書きます.
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
@Data
public class Wepon extends Item
{
    /** 武器の攻撃力 */
    private int value;
    /** 武器の特殊能力 */
    private Formula special;
    /** 武器の副作用 */
    private Formula demerit;
    
    public Wepon() { 
    }
    
    public Wepon(String id, String name, int value, Formula f, Formula dem) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.special = f;
        this.demerit = dem;
    }

}
