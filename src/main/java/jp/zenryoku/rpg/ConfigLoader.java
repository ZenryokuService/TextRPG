package jp.zenryoku.rpg;

import jp.zenryoku.rpg.data.Formula;
import jp.zenryoku.rpg.data.config.Config;
import jp.zenryoku.rpg.data.config.Scene;
import jp.zenryoku.rpg.data.param.Armor;
import jp.zenryoku.rpg.data.param.Item;
import jp.zenryoku.rpg.data.param.Items;
import jp.zenryoku.rpg.data.param.Wepon;
import lombok.Data;
import jp.zenryoku.rpg.data.config.Worlds;
import jp.zenryoku.rpg.character.*;
import jp.zenryoku.rpg.utils.XMLUtil;

import java.util.*;
import java.io.File;
import java.nio.file.Paths;


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
    /** アイテムマップ */
    private Map<String, Item> itemMap;
    /** 武器マップ */
    private Map<String, Wepon> wepMap;
    /** 防具マップ */
    private Map<String, Armor> armMap;

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
        // アイテムと武器防具
        itemMap = new HashMap<>();
        wepMap = new HashMap<>();
        armMap = new HashMap<>();
        loadItems("config", "Items.xml");

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
     * @param directory 読み込むファイルのあるディレクトリ
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

    /**
     * アイテム定義ファイルの場合は、アイテム、武器、防具を一つのファイルで
     * 管理するため、直接フィールドに登録する。
     *
     * @param directory 対象フォルダ
     * @param fileName 対象ファイル
     * @return Items これは、必要になった時でよい
     * @throws RpgException
     */
    public Items loadItems(String directory, String fileName) throws RpgException {
        Map<String, Player> map = new HashMap<>();
        Items items = XMLUtil.loadItems(directory + "/" + fileName);
        List<Item> itemList = items.getItems();
        List<Wepon> wepList = items.getWepons();
        List<Armor> armList = items.getArmors();

        // キーの重複チェックを行う
        List<String> checkerList = new ArrayList<>();
        for (Item it : itemList) {
            String itId = it.getId();
            if (checkerList.contains(itId) == false) {
                checkerList.add(itId);
                itemMap.put(itId, it);
            } else {
                throw new RpgException("ItemのIDが重複しています。" + itId);
            }
        }
        for (Wepon wep : wepList) {
            String wepId = wep.getId();
            if (checkerList.contains(wepId) == false) {
                checkerList.add(wepId);
                wepMap.put(wepId, wep);
            } else {
                throw new RpgException("WeponのIDが重複しています。" + wepId);
            }
        }
        for (Armor arm : armList) {
            String armId = arm.getId();
            if (checkerList.contains(armId) == false) {
                checkerList.add(armId);
                armMap.put(armId, arm);
            } else {
                throw new RpgException("AromorのIDが重複しています。" + armId);
            }
        }
        return items;
    }

    /**
     * プレーヤー定義ファイルを読み込む
     * @param directory 読み込むファイルのあるディレクトリ
     * @param fileName 対象ファイル
     * @return Map<String, Player> プレーヤー名をキーにしたマップ
     * @throws RpgException
     */
    public Map<String, Player> loadPlayers(String directory, String fileName) throws RpgException {
        Map<String, Player> map = new HashMap<>();
        List<Player> playerList = XMLUtil.loadPlayer(directory + "/" + fileName);

        for (Player player : playerList) {
            map.put(player.getName(), player);
        }
        return map;
    }

    /**
     * モンスター定義ファイルを読み込む
     * @param directory 対象ディレクトリ
     * @param fileName 対象ファイル
     * @return モンスター番号をキーにしたマップ
     * @throws RpgException
     */
    public Map<Integer, Monster> loadMonsters(String directory, String fileName) throws RpgException {
        Map<Integer, Monster> map = new HashMap<>();
        List<Monster> monsterList = XMLUtil.loadMonsters(directory + "/" + fileName);

        for (Monster mons : monsterList) {
            map.put(mons.getNo(), mons);
        }
        return map;
    }

    /**
     * 設定定義ファイルを読み込む
     * @param directory 対象ディレクトリ
     * @param fileName 対象ファイル
     * @return　Config 設定クラス
     * @throws RpgException
     */
    public Config loadConfig(String directory, String fileName) throws RpgException {
        return XMLUtil.loadConfig(directory, fileName);
    }

    /**
     * モンスターを呼び出す処理。ほんとはインスタンスをコピーして
     * モンスターを増やしたかったが、インスタンスの参照が変わらないので。
     * モンスターの値をリセットするようにしている。
     *
     * @param low 低いモンスター番号
     * @param hight 高いモンスター番号
     * @return ランダムに指定したモンスターオブジェクト
     */
    public Monster callMonster(int low, int hight) {
        if (isDebug) System.out.println("low: " + low + " high: " + hight);
        Random rnd = new Random();
        int monsterNo = rnd.nextInt(hight) + low;
        if (isDebug) System.out.print("No: " + monsterNo);
        Monster mst = monsters.get(monsterNo);
        if (isDebug) System.out.println("　" + mst.getName());
        Monster newMonster = null;
        if (isDebug) System.out.println("インスタンスID;1  " + mst.getStatus().hashCode());
        try {
            newMonster =  mst.clone();
            if (isDebug) System.out.println("インスタンスID;2  " + newMonster.getStatus().hashCode());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return newMonster;
    }

}
