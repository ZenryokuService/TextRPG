package jp.zenryoku.rpg.data.config;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement( name="commands")
@XmlAccessorType(XmlAccessType.FIELD)
public class Commands {
    @XmlElement(name="command", type=Command.class)
    private List<Command> commandList;
}
