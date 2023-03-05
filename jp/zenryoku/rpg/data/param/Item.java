package jp.zenryoku.rpg.data.param;

import lombok.Data;
import java.util.List;
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
    /** 名前 */
    private String name;
    /** 効果式 */
    private Formula formula;

    public Item() {
    }
    
    public Item(String name, Formula formula) {
        this.name = name;
        this.formula = formula;
    }
}
