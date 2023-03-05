package jp.zenryoku.rpg.character;

import lombok.Data;
import java.util.List;
import java.util.ArrayList;
import jp.zenryoku.rpg.data.param.Params;
import jp.zenryoku.rpg.data.param.Item;
import jp.zenryoku.rpg.data.param.Wepon;
import jp.zenryoku.rpg.data.param.Armor;
import jp.zenryoku.rpg.data.param.State;
import jp.zenryoku.rpg.data.param.SEX;
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
@XmlType(propOrder={"name", "sex", "status", "items", "job", "state", "wepon", "armor"})
@Data
public class Player
{
    /** 名前 */
    private String name;
    /** 性別 */
    private SEX sex;
    /** ステータス */
    private List<Params> status;
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
        status = new ArrayList<>();
        items = new ArrayList<>();
        state = new State();
    }
    
    public Player(String name) {
        this.name = name;
        status = new ArrayList<>();
        items = new ArrayList<>();
        state = new State();
    }
}
