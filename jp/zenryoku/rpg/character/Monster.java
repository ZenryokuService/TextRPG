package jp.zenryoku.rpg.character;

import lombok.Data;
import jp.zenryoku.rpg.data.config.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
/**
 * クラス Monster の注釈をここに書きます.
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
@Data
@XmlRootElement( name="player")
@XmlType(propOrder={"no", "talk", "message", "type"})
public class Monster extends Player
{
    /** 番号 */
    private int no;
    /** 話す */
    private boolean talk;
    /** 話すときのメッセージ */ 
    private String message;
    /** モンスタータイプ */
    private MonsterType type;

    public Monster() {
    }
    
    public Monster(String name) {
        super(name);
    }
}
