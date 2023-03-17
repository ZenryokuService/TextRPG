package jp.zenryoku.rpg.data.config;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement( name="worlds")
@XmlAccessorType(XmlAccessType.FIELD)
public class Worlds {
    @XmlElement(name="world", type=World.class)
    private List<World> worlds = new ArrayList<>();

    public Worlds() {}
}
