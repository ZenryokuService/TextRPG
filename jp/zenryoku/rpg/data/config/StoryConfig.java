package jp.zenryoku.rpg.data.config;

import lombok.Data;
/**
 * 設定オクラスのルートクラス
 */
@Data
public class StoryConfig implements ConfigIF {
    /** 世界定義ファイルの名前 */
    public static final String WORLD_XML = "Worlds.xml";
    /** 国の定義ファイルの名前 */
    public static final String COUNTRY_XML = "Countries.xml";
    /** 気候定義ファイルの名称 */
    public static final String NATURE_XML = "Nature.xml";
    /** 生物定義ファイルの名前 */
    public static final String CREATURE_XML = "Creatures.xml";
    /** モンスタータイプ定義ファイルの名前 */
    public static final String MONSTER_TYPE_XML = "MonsterType.xml";
    /** 地域定義ファイルの名前 */
    public static final String REGIONS_XML = "Regions.xml";
    /** 世界定義ファイルの名前 */
    public static final String WORLD_TAG = "world";
    /** IDタグ　*/
    public static final String ID_TAG = "id";
    /** NAMEタグ　*/
    public static final String NAME_TAG = "name";
    /** IMGタグ */
    public static final String IMG_TAG = "img";
    /** DISCRIPIONタグ　*/
    public static final String DESCRIPTION_TAG = "description";
    /** 国の定義リストの名前 */
    public static final String COUNTRY_LIST_TAG = "countries";
    /** 国の定義タグの名前 */
    public static final String COUNTRY_TAG = "country";
    /** 気候定義リストタグの名称 */
    public static final String NATURE_LIST_TAG = "natures";
    /** 気候定義タグの名称 */
    public static final String NATURE_TAG = "nature";
    /** 気候タグの名称 */
    public static final String CLIMATE_TAG = "climate";
    /** 生物定義リストの名前 */
    public static final String CREATURE_LIST_TAG = "creatures";
    /** 生物定義タグの名前 */
    public static final String CREATURE_TAG = "creature";
    /** 地域定義リストの名前 */
    public static final String REGION_LIST_TAG = "regions";
    /** 地域定義タグの名前 */
    public static final String REGION_TAG = "region";
    /** 文明リストタグの名前 */
    public static final String CIVILIZATION_LIST_TAG = "civilizations";
    /** 文明タグの名前 */
    public static final String CIVILIZATION_TAG = "civilization";
    /** 文字タグ */
    public static final String CHARACTER_TAG = "character";
    /** 社会構造 */
    public static final String STRUCTURE_TAG = "structures";
    /** 文化リストタグの名前 */
    public static final String CULTURE_LIST_TAG = "cultures";
    /** 技術タグ */
    public static final String TECHNOLOGY_TAG = "technology";
    /** 芸術タグ */
    public static final String ARTS_TAG = "arts";
    /** 文化タグの名前 */
    public static final String CULTURE_TAG = "culture";
    


    /** ID */
    protected String id;
    /** 名前 */
    protected String name;
    /** 説明 */
    protected String description;
    /** パス */
    protected String path;
    /** イメージ画像へのパス(URI) */
    protected String img;
    
    /** パスが指定されている場合の処理 */
    public ConfigIF loadConfig() {
        if (this.path == null || "".equals(this.path)) {
            System.out.println("パスが指定されていません。" + name);
            System.exit(-1);
        }
        ConfigIF instance = null;
        // TODO-[XMLを読み込みインスタンスを生成する]
        return instance;
    }
}
