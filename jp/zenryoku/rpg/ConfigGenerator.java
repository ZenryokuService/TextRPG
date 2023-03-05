package jp.zenryoku.rpg;

import jp.zenryoku.rpg.data.config.Worlds;
import jp.zenryoku.rpg.utils.XMLUtil;


/**
 * クラス ComfigGenerator の注釈をここに書きます.
 * 設定を読み込み保持する。シングルトン。
 * @author (Takunoji)
 * @version (1.0)
 */
public class ConfigGenerator
{
    /** このクラスのインスタンス、必ず一つ */
    private static ConfigGenerator instance;
    /** Worlds：世界観 */
    private Worlds worlds;
    
    /** コンストラクタ */
    private ConfigGenerator() {
        worlds = new Worlds();
        worlds.setWorlds(XMLUtil.loadWorldJaxb("config", "Worlds.xml"));
    }
    
    /**
     * インスタンスが生成済みならば、生成済みのものを返す。
     */
    public static ConfigGenerator getInstance() {
        if (instance == null) {
            instance = new ConfigGenerator();
        }
        return instance;
    }
}
