package jp.zenryoku.rpg.data.config;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement( name="monsterTypes")
@XmlAccessorType(XmlAccessType.FIELD)
public class MonsterTypes {
    @XmlElement(name="monsterType", type=MonsterType.class)
    private List<MonsterType> monsterTypes;
}
