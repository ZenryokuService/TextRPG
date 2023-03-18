package jp.zenryoku.rpg.data.param;

import jp.zenryoku.rpg.data.config.World;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement( name="itemList")
@XmlAccessorType(XmlAccessType.FIELD)
public class Items {
    @XmlElement(name="item", type= Item.class)
    private List<Item> items;
    @XmlElement(name="wepon", type= Wepon.class)
    private List<Wepon> wepons;
    @XmlElement(name="armor", type= Armor.class)
    private List<Armor> armors;

}
