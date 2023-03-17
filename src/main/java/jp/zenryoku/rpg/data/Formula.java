package jp.zenryoku.rpg.data;

import lombok.Data;
/**
 * クラス Formula の注釈をここに書きます.
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
@Data
public class Formula
{
    /** 効果式のID */
    private String id;
    /** 効果式 */
    private String formulaStr;
    /** ターゲット */
    private String target;
    
    public Formula() {
    }
    
    public Formula(String formula) {
        this.formulaStr = formula;
    }
}
