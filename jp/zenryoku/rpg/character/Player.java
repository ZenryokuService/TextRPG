package jp.zenryoku.rpg.character;

import lombok.Data;
import net.objecthunter.exp4j.ExpressionBuilder;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import jp.zenryoku.rpg.ConfigGenerator;
import jp.zenryoku.rpg.exception.*;
import jp.zenryoku.rpg.data.param.Params;
import jp.zenryoku.rpg.data.param.Item;
import jp.zenryoku.rpg.data.param.Wepon;
import jp.zenryoku.rpg.data.param.Armor;
import jp.zenryoku.rpg.data.param.State;
import jp.zenryoku.rpg.data.param.SEX;
import jp.zenryoku.rpg.data.Formula;
import jp.zenryoku.rpg.data.config.Job;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * クラス Player の注釈をここに書きます.
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
@XmlRootElement( name="player")
@XmlType(propOrder={"level", "name", "sex", "status", "items", "job", "state", "wepon", "armor"})
@Data
public class Player
{
    private static final boolean isDebug = true;
    /** 名前 */
    private String name;
    /** レベル */
    private int level;
    /** 性別 */
    private SEX sex;
    /** ステータス */
    private Map<String, Params> status;
    /** 所持アイテム */
    private List<Item> items;
    /** 職業 */
    private Job job;
    /** 状態 */
    private State state;
    /** 武器 */
    private Wepon wepon;
    /** 防具 */
    private Armor armor;
    
    public Player() {
        status = new HashMap<>();
        items = new ArrayList<>();
        state = new State();
    }
    
    public Player(String name) {
        this.name = name;
        status = new HashMap<>();
        items = new ArrayList<>();
        state = new State();
    }
    
    public Params getParams(String key) {
        return status.get(key);
    }
    
    public int attack(Formula formula, Player monster) throws RpgException {
        String conv = convertFormula(formula);
        if (isDebug)  print(monster.getName() + ": ", monster);
        int res = (int) new ExpressionBuilder(conv).build().evaluate();
        Params param = monster.getParams(formula.getTarget());
        int lastHp = param.getValue() - res;
        param.setValue(lastHp);
        if (isDebug) print(monster.getName() + ": ", monster);
        return res;
    }

    protected void print(String prefix, Player p) {
        System.out.println(prefix + "HP: " + p.getParams("HP").getValue());
    }
    private String convertFormula(Formula formula) throws RpgException {
        Map<String, Formula> formulas = ConfigGenerator.getInstance().getFormulas();
        String resFormula = formula.getFormulaStr();

        System.out.println("f: " + resFormula);
        // 基本計算記号の変換
        Set<String> fSet = formulas.keySet();
        for (String key : fSet) {

            if (resFormula.contains(key)) {
                Formula f = formulas.get(key);
                resFormula = resFormula.replaceAll(key, f.getFormulaStr());
            }
        }
        //System.out.println("Lv1: " + resFormula);
        // ステータス文字列の変換
        Set<String> keys = status.keySet();
        String conv = null;
        for (String key : keys) {
            if (resFormula.contains(key)) {
                String value = String.valueOf(status.get(key).getValue());
                resFormula = resFormula.replace(key, value);
            }
        }
        //System.out.println("Lv2: " + resFormula);
        return resFormula;
    }
}
