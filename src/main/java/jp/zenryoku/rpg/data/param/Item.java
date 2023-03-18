package jp.zenryoku.rpg.data.param;

import lombok.Data;
import jp.zenryoku.rpg.data.Formula;
/**
 * クラス Item の注釈をここに書きます.
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
@Data
public class Item
{
    /** ID */
    protected String id;
    /** 名前 */
    protected String name;
    /** 効果式 */
    protected Formula formula;

    public Item() {
    }
    
    public Item(String id, String name, Formula formula) {
        this.id = id;
        this.name = name;
        this.formula = formula;
    }
}
