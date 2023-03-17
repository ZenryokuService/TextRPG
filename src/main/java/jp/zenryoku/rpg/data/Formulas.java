package jp.zenryoku.rpg.data;

import lombok.Data;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * クラス Formulas の注釈をここに書きます.
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
@Data
@XmlRootElement( name="formulas")
@XmlAccessorType(XmlAccessType.FIELD)
public class Formulas
{
    @XmlElement(name="formula", type=Formula.class)
    private List<Formula> formulas;
}
