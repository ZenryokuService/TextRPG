package jp.zenryoku.rpg.data.config;

import jp.zenryoku.rpg.data.SceneType;
import lombok.Data;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * クラス Story の注釈をここに書きます.
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
@XmlRootElement( name="story")
@XmlType(propOrder={"sceneNo", "sceneType", "story", "nextScene", "canSelectNextScene", "center", "selects"})
public class Story extends StoryConfig
{
    /** シーン番号 */
    private int sceneNo;
    /** シーンタイプ */
    private SceneType sceneType;
    /** ストーリー文章 */
    private String story;
    /** 次のシーン番号(デフォルト値は0) */
    private int nextScene;
    /** 次のシーンの選択可否 */
    private boolean canSelectNextScene;
    /** 表示する文字をセンタリング */
    private boolean center;
    /** 選択肢 */
    private List<Select> selects;
    
    public Story() {
    }

    public Story(String path) {
        this.path = path;
        // TODO-[パスが設定されているときは、パスに指定されているファイルから生成]
    }
    
    public Story(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Story(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    
}
