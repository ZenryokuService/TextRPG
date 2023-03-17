package jp.zenryoku.rpg;

import lombok.Data;
import lombok.Data;
import jp.zenryoku.rpg.data.config.Worlds;
import jp.zenryoku.rpg.character.*;
import jp.zenryoku.rpg.utils.XMLUtil;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Path;


import jp.zenryoku.rpg.data.config.*;
import jp.zenryoku.rpg.data.*;
import jp.zenryoku.rpg.exception.RpgException;

/**
 * XMLファイルに定義した設定、プレーヤーなど全てを読み込む。
 */
@Data
public class ConfigLoader {

    private static final boolean isDebug = false;
    /** このクラスのインスタンス、必ず一つ */
    private static ConfigLoader instance;
    /** 設定(Config.xml) */
    private Config conf;
    /** Worlds：世界観 */
    private Worlds worlds;
    /** ストーリーマップ */
    private Map<Integer, Scene> scenes;
    /** プレーヤーマップ */
    private Map<String, Player> players;
    /** モンスターマップ */
    private Map<Integer, Monster> monsters;
    /** Formulaマップ */
    private Map<String, Formula> formulas;

    /** コンストラクタ */
    private ConfigLoader() throws RpgException {
        // 設定読込
        conf = loadConfig("config", "Config.xml");
        // 世界観の生成
        worlds = new Worlds();
        worlds.setWorlds(XMLUtil.loadWorldJaxb("config", "Worlds.xml"));
        // ストーリーの生成
        scenes = loadStories("config/stories");
        // プレーヤーの生成
        players = loadPlayers("config", "Players.xml");
        // モンスターの生成
        monsters = loadMonsters("config", "Monsters.xml");
        // 計算式
        formulas = XMLUtil.loadFormulas("config", "Formula.xml");

    }

    /**
     * インスタンスが生成済みならば、生成済みのものを返す。
     */
    public static ConfigLoader getInstance() throws RpgException  {
        if (instance == null) {
            instance = new ConfigLoader();
        }
        return instance;
    }

    /**
     * ストーリーをロードする。
     */
    public Map<Integer, Scene> loadStories(String directory) throws RpgException {
        File[] storyFiles = Paths.get(directory).toFile().listFiles();
        Map<Integer, Scene> map = new HashMap<>();

        for (File xml : storyFiles) {
            if (isDebug) System.out.println(xml.getName());
            if (xml.getName().matches(".*.xml")) {
                Scene story = XMLUtil.loadStory(xml);
                int key = story.getSceneNo();
                if (map.containsKey(key)) {
                    System.out.println("シーン番号が重複しています");
                    System.exit(-1);
                }
                map.put(story.getSceneNo(), story);
            }
        }
        return map;
    }

    public Map<String, Player> loadPlayers(String directory, String fileName) throws RpgException {
        Map<String, Player> map = new HashMap<>();
        List<Player> playerList = XMLUtil.loadPlayer(directory + "/" + fileName);

        for (Player player : playerList) {
            map.put(player.getName(), player);
        }
        return map;
    }

    public Map<Integer, Monster> loadMonsters(String directory, String fileName) throws RpgException {
        Map<Integer, Monster> map = new HashMap<>();
        List<Monster> monsterList = XMLUtil.loadMonsters(directory + "/" + fileName);

        for (Monster mons : monsterList) {
            map.put(mons.getNo(), mons);
        }
        return map;
    }

    public Config loadConfig(String directory, String fileName) throws RpgException {
        return XMLUtil.loadConfig(directory, fileName);
    }

    public Monster callMonster(int low, int hight) {
        System.out.println("low: " + low + " high: " + hight);
        Random rnd = new Random();
        int monsterNo = rnd.nextInt(hight) + low;
        System.out.print("No: " + monsterNo);

        Monster mst = monsters.get(monsterNo);
        System.out.println("　" + mst.getName());
        return mst.clone();
    }

}
