package jp.zenryoku.rpg.data.config;

import jp.zenryoku.rpg.data.SceneType;
import lombok.Data;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * クラス Scene の注釈をここに書きます.
 * 
 * 下にある例は、ストーリーを表示して固定のシーンへ移動するモノです。
 * 
 * 【最低限必要な情報】
 * 1. シーン番号
 * 2. シーンタイプ
 * 3. 表示する文章(ストーリー)
 * 4. 次のシーン番号
 * 5. 選択肢のリスト
 * @author (Takunoji)
 * @version (1.0)
 */
@Data
@XmlRootElement( name="scene")
@XmlType(propOrder={"sceneNo", "html", "sceneType", "story", "nextScene"
    , "canSelectNextScene", "center", "selects", "monsterNo"
    , "monsterNoLow", "monsterNoHigh", "items"})
public class Scene extends StoryConfig
{
    /** シーン番号 */
    private int sceneNo;
    /** HTMLフラグ */
    private boolean html;
    /** シーンタイプ */
    private SceneType sceneType;
    /** ストーリー文章 */
    private String story;
    /** 次のシーン番号(デフォルトは0) */
    private int nextScene;
    /** 次のシーンの選択可否 */
    private boolean canSelectNextScene;
    /** 表示する文字をセンタリング */
    private boolean center;
    /** 選択肢 */
    private List<Select> selects;
    /** モンスター番号 */
    private int monsterNo;
    /** モンスター番号最低値 */
    private int monsterNoLow;
    /** モンスター番号最高値 */
    private int monsterNoHigh;
    /** 取得できるアイテム(IDを保持) */
    private List<String> items;
    
    public Scene() {
    }

    public Scene(String path) {
        this.path = path;
        // TODO-[パスが設定されて�?るとき�?�、パスに�?定されて�?るファイルから生�?�]
    }
    
    public Scene(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Scene(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    
}
