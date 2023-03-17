package jp.zenryoku.rpg.character;

import lombok.Data;
import lombok.Builder;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import jp.zenryoku.rpg.character.*;
import jp.zenryoku.rpg.data.config.*;
import jp.zenryoku.rpg.data.param.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * クラス Monster の注釈をここに書きます.
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
@Data
@XmlRootElement( name="monster")
@XmlType(propOrder={"no", "talk", "message", "type", "money", "exp"})
public class Monster extends Player implements Cloneable
{
    /** 番号 */
    private int no;
    /** 話す */
    private boolean talk;
    /** 話すときのメッセージ */ 
    private String message;
    /** モンスタータイプ */
    private MonsterType type;
    /** お金 */
    private int money;
    /** 経験値 */
    private int exp;

    public int getNo() {
        return no;
    }
    
    public Monster() {
    }
    
    public Monster(String name) {
        super(name);
    }
    
    public Monster clone() {
        Monster mon = new Monster(name);
        mon.setLevel(level);
        mon.setSex(sex);
        Map<String, Params> stMap = new HashMap<>();
        stMap.putAll(status);
        mon.setStatus(stMap);
        List<Item> it = new ArrayList<>();
        it.addAll(items);
        mon.setItems(it);
        mon.setState(state);
        mon.setWepon(wepon);
        mon.setArmor(armor);
        mon.setNo(no);
        mon.setTalk(talk);
        mon.setMessage(message);
        mon.setType(type);
        mon.setMoney(money);
        mon.setExp(exp);
        return mon;
    }
    
    public void finalize() {
        //super.finalize();
        level = 0;
        sex = null;
        status.clear();
        status = null;
        items.clear();
        items = null;
        state = null;
        wepon = null;
        armor = null;
        no = 0;
        talk = false;
        message = null;
        type = null;
        money = 0;
        exp = 0;
    }
}
