package jp.zenryoku.rpg.data.config;

import lombok.Data;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.util.List;
import jp.zenryoku.rpg.data.param.*;
/**
 * クラス Config の注釈をここに書きます.
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
@Data
@XmlRootElement( name="config")
@XmlType(propOrder={"views", "money", "languages", "element", "params", "statusList", "items"})
public class Config extends StoryConfig
{
    /** インスタンス */
    private static Config instance;
    /** プレーヤーの表示項目 */
    private List<String> views;
    /** 通貨の名称 */
    private List<Params> money;
    /** 使用する言語 */
    private List<Params> languages;
    /** エレメント要素 */
    private List<Element> element;
    /** プレーヤーステータス要素 */
    private List<Params> params;
    /** プレーヤー状態 */
    private List<State> statusList;
    /** アイテムの要素 */
    private List<Params> items;
 
    private Config() {
    }
    
    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }
    
}
