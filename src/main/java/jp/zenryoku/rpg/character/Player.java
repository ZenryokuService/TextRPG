package jp.zenryoku.rpg.character;

import lombok.Data;
import net.objecthunter.exp4j.ExpressionBuilder;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import jp.zenryoku.rpg.ConfigLoader;
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
@XmlType(propOrder={"level", "name", "sex", "status", "items", "job", "jobStr",  "state", "wepon", "armor", "money", "exp"})
@Data
public class Player implements Cloneable
{
    private static final boolean isDebug = true;
    /** 名前 */
    protected String name;
    /** レベル */
    protected int level;
    /** 性別 */
    protected SEX sex;
    /** ステータス */
    protected Map<String, Params> status;
    /** 所持アイテム */
    protected List<Item> items;
    /** 職業ID */
    protected String jobStr;
    /** 職業 */
    protected Job job;
    /** 状態 */
    protected State state;
    /** 武器 */
    protected Wepon wepon;
    /** 防具 */
    protected Armor armor;
    /** お金 */
    protected int money;
    /** 経験値(Monsterと同じものがある) */
    protected int exp;
    
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

    public void terminated() {
        level = 0;
        sex = null;
        status = null;
        items = null;
        state = null;
        job = null;
        wepon = null;
        armor = null;
    }
    public String getName() {
        return name;
    }
    public Params getParams(String key) {
        return status.get(key);
    }
    public void addExp(int exp) {
        this.exp += exp;
    }

    public void addMoney(int money) {
        this.money += money;
    }

    public void addItem(Item it) {
        items.add(it);
    }

    public boolean canHasMore() {
        int maxSize = status.get("BPK").getValue();
        int nowSize = items.size();
        return nowSize < maxSize;
    }
    public Player clone() {
        Player player = new Player(name);
        player.setLevel(level);
        player.setSex(sex);
        Map<String, Params> stMap = new HashMap<>();
        stMap.putAll(status);
        player.setStatus(stMap);
        List<Item> it = new ArrayList<>();
        it.addAll(items);
        player.setItems(it);
        player.setState(state);
        player.setJob(job);
        player.setWepon(wepon);
        player.setArmor(armor);
        return this;
    }
    public int attack(Formula formula, Player monster) throws RpgException {
        String conv = convertFormula(formula);
        if (isDebug)  print(monster.getName() + ": ", monster);
        int res = (int) new ExpressionBuilder(conv).build().evaluate();
        System.out.println("*** " + formula.getTarget() + "  ***");
        Params param = monster.getParams(formula.getTarget());
        int lastHp = param.getValue() - res;
        param.setValue(lastHp);
        if (isDebug) print(monster.getName() + ": ", monster);
        return res;
    }

    /** こうげきりょくを取得 */
    public int getAttackPower() {
        int result = 0;
        try {
            // TODO-[ATKを動的に指定したい]
            Formula atk = ConfigLoader.getInstance().getFormulas().get("ATK");
            String conv = convertFormula(atk);
            result = (int) new ExpressionBuilder(conv).build().evaluate();
            System.out.println("ATK=" + result);
        } catch (RpgException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return result;
    }

    /** ぼうぎょりょくを取得 */
    public int getDeffencePower() {
        int result = 0;
        try {
            // TODO-[DEFを動的に指定したい]
            Formula def = ConfigLoader.getInstance().getFormulas().get("DEF");
            String conv = convertFormula(def);
            result = (int) new ExpressionBuilder(conv).build().evaluate();
            System.out.println("DEF=" + result);
        } catch (RpgException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return result;
    }
    protected void print(String prefix, Player p) {
        System.out.println(prefix + "HP: " + p.getParams("HP").getValue());
    }
    private String convertFormula(Formula formula) throws RpgException {
        Map<String, Formula> formulas = ConfigLoader.getInstance().getFormulas();
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
        System.out.println("Lv1: " + resFormula);
        // ステータス文字列の変換
        Set<String> keys = status.keySet();
        String conv = null;
        for (String key : keys) {
            if (resFormula.contains(key)) {
                String value = String.valueOf(status.get(key).getValue());
                resFormula = resFormula.replace(key, value);
            }
        }
        System.out.println("Lv2: " + resFormula);
        return resFormula;
    }

    public void effect(Formula formula) throws RpgException {
        String f = formula.getFormulaStr();
        String target = formula.getTarget();
        System.out.println("Params: " + target);
        ConfigLoader loader = ConfigLoader.getInstance();

        String conv = null;
        if (loader.isCurrentMoney(target, true)) {
            conv = loader.convertMoneyStr(formula.getFormulaStr());
            int ef = (int) new ExpressionBuilder(conv).build().evaluate();
            System.out.println("Money: " + ef);
            setMoney(getMoney() + ef);
        } else {
            conv = convertFormula(formula);
            System.out.println(conv);
            int ef = (int) new ExpressionBuilder(conv).build().evaluate();
            Params p = this.status.get(target);
            System.out.println("Params: " + p.getName());
            p.setValue(p.getValue() + ef);
        }
    }

    /**
     * アイテムリストの番号からアイテムを取得する。
     * @param num アイテムリストの番号
     * @return 指定のアイテム
     */
    public Item selectItem(int num) {
        return items.get(num);
    }

    public void setWepon(Wepon wepon) {
        this.wepon = wepon;
        Params param = new Params("WEV", wepon.getName(), wepon.getValue());
        status.put("WEV", param);
    }

    public void setArmor(Armor arm) {
        this.armor = arm;
        Params param = new Params("ARV", arm.getName(), arm.getValue());
        status.put("ARV", param);
    }

}
