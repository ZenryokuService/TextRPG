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
public class Wepon
{
    /** 武器の名前 */
    private String name;
    /** 武器の攻撃力 */
    private int value;
    /** 武器の特殊能力 */
    private Formula formula;
    
    public Wepon() { 
    }
    
    public Wepon(String name, int value, Formula f) {
        this.name = name;
        this.value = value;
        this.formula = f;
    }

}
