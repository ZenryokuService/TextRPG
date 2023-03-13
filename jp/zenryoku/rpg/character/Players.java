package jp.zenryoku.rpg.character;


import lombok.Data;

import java.util.List;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * クラス Players の注釈をここに書きます.
 * 
 * @author (あなたの名前)
 * @version (バージョン番号もしくは日付)
 */
@Data
@XmlRootElement( name="players")
@XmlAccessorType(XmlAccessType.FIELD)
public class Players
{
    @XmlElement(name="player", type=Player.class)
    private List<Player> players = new ArrayList<>();

    public Players() {}
}
