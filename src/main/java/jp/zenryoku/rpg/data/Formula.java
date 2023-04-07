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
    /** 方向性 */
    private String orient;
    
    public Formula() {
    }
    
    public Formula(String formula) {
        this.formulaStr = formula;
    }

    public Formula(String id, String formula, String target) {
        this.id = id;
        this.formulaStr = formula;
        this.target = target;
        this.orient = "-";
    }

    public Formula(String id, String formula, String target, String orient) {
        this.id = id;
        this.formulaStr = formula;
        this.target = target;
        this.orient = orient;
    }

}
