package jp.zenryoku.rpg.character;


import lombok.Data;

import java.util.List;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * クラス Monsters の注釈をここに書きます.
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
@Data
@XmlRootElement( name="monsters")
@XmlAccessorType(XmlAccessType.FIELD)
public class Monsters
{
    private List<Monster> monsters = new ArrayList<>();

    public Monsters() {}
}
