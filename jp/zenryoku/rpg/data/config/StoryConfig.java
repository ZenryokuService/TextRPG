package jp.zenryoku.rpg.data.config;

/**
 * 設定オクラスのルートクラス
 */
public class StoryConfig implements ConfigIF {
    /** HPなどの設定ファイル */
    public static final String CONFIG_XML = "Config.xml";
    /** 世界定義ファイルの名前 */
    public static final String WORLD_XML = "Worlds.xml";
    /** 気候定義ファイルの名称 */
    public static final String NATURE_XML = "Nature.xml";
    /** 職業定義ファイルの名前 */
    public static final String JOB_XML = "Job.xml";
    /** コマンド定義ファイルの名前 */
    public static final String CPMMADS_XML = "Commands.xml";
    /** 生物定義ファイルの名前 */
    public static final String CREATURE_XML = "Creatures.xml";
    /** モンスタータイプ定義ファイルの名前 */
    public static final String MONSTER_TYPE_XML = "MonsterType.xml";
    /** モンスター定義ファイルの名前 */
    public static final String MONSTERS_XML = "Monsters.xml";
    /** 地域定義ファイルの名前 */
    public static final String REGIONS_XML = "Regions.xml";
    /** STM定義ファイルの名前 */
    public static final String STM_XML = "STM.xml";
    /** Effect定義ファイルの名前 */
    public static final String EFFECTS_XML = "Effects.xml";


    /** ID */
    private String ID;
    /** 名前 */
    private String name;
    /** 説明 */
    private String discription;
}
