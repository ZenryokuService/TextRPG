package jp.zenryoku.rpg;

import jp.zenryoku.rpg.data.Formula;
import jp.zenryoku.rpg.data.config.*;
import jp.zenryoku.rpg.data.param.*;
import lombok.Data;
import jp.zenryoku.rpg.character.*;
import jp.zenryoku.rpg.utils.XMLUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.io.File;
import java.nio.file.Paths;


import jp.zenryoku.rpg.exception.RpgException;

/**
 * XMLファイルに定義した設定、プレーヤーなど全てを読み込む。
 */
@Data
public class ConfigLoader {
    /** デバックのONろOFF */
    private static final boolean isDebug = false;
    /** このクラスのインスタンス、必ず一つ */
    private static ConfigLoader instance;
    /** アイテムマップのデータ */
    public static final int ITEM = 0;
    /** 武器マップのデータ */
    public static final int WEP = 1;
    /** 防具マップのデータ */
    public static final int ARM = 2;
    /** パラメータの最大文字列長 */
    public static int MAX_PARAM_SIZE;
    /** 設定(Config.xml) */
    private Config conf;
    /** Worlds：世界観 */
    private Worlds worlds;
    /** ストーリーマップ */
    private Map<Integer, Scene> scenes;
    /** コマンドマップ */
    private Map<String, Command> commandMap;
    /** 職業マップ */
    private Map<String, Job> jobs;
    /** プレーヤーマップ */
    private Map<String, Player> players;
    /** モンスタータイプマップ */
    private Map<String, MonsterType> monsterTypeMap;
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
    /** パラメータマップ */
    private Map<String, Params> paramsMap;
    /** 使用する金額の単位 */
    private Params currentMooney;

    /** コンストラクタ */
    private ConfigLoader() throws RpgException {
        // 設定読込
        conf = loadConfig("config", "Config.xml");
        // 世界観の生成
        worlds = new Worlds();
        worlds.setWorlds(XMLUtil.loadWorldJaxb("config", "Worlds.xml"));
        // ストーリーの生成
        scenes = loadStories("config/stories/");
        // コマンドの生成
        commandMap = loadCommands("config", "Command.xml");
        // 職業の生成
        jobs = loadJobs("config", "Jobs.xml");
        // プレーヤーの生成
        players = loadPlayers("config", "Players.xml");
        // モンスタータイプ
        monsterTypeMap = loadMonsterType("config", "MonsterTypes.xml");
        // モンスターの生成
        monsters = loadMonsters("config", "Monsters.xml");
        // 計算式
        formulas = XMLUtil.loadFormulas("config", "Formula.xml");
        // アイテムと武器防具
        itemMap = new HashMap<>();
        wepMap = new HashMap<>();
        armMap = new HashMap<>();
        loadItems("config", "Items.xml");
        // 通貨取得
        conf.getMoney().forEach(mon -> {
            // デフォルトの通貨はvalue=0とする
            if (mon.getValue() == 0) {
                currentMooney = mon;
            }
        });

    }

    /**
     * コンストラクタ
     * @param path 設定フィルのあるルートディレクトリ「/]は最後につけない。
     * @throws RpgException
     */
    private ConfigLoader(String path) throws RpgException {
        // 設定読込
        conf = loadConfig(path, "Config.xml");
        // 世界観の生成
        worlds = new Worlds();
        worlds.setWorlds(XMLUtil.loadWorldJaxb(path, "Worlds.xml"));
        // ストーリーの生成
        scenes = loadStories(path + "/stories/");
        // 職業の生成
        jobs = loadJobs(path, "Jobs.xml");
        // プレーヤーの生成
        players = loadPlayers(path, "Players.xml");
        // モンスタータイプ
        monsterTypeMap = loadMonsterType(path, "MonsterTypes.xml");
        // モンスターの生成
        monsters = loadMonsters(path, "Monsters.xml");
        // 計算式
        formulas = XMLUtil.loadFormulas(path, "Formula.xml");
        // アイテムと武器防具
        itemMap = new HashMap<>();
        wepMap = new HashMap<>();
        armMap = new HashMap<>();
        loadItems(path, "Items.xml");
        // 通貨取得
        conf.getMoney().forEach(mon -> {
            // デフォルトの通貨はvalue=0とする
            if (mon.getValue() == 0) {
                currentMooney = mon;
            }
        });
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

    public static ConfigLoader getInstance(String path) throws RpgException  {
        if (instance == null) {
            instance = new ConfigLoader(path);
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
            if (xml.getName().matches(".*.xml")) {
                Scene story = XMLUtil.loadStory(xml);
                int key = story.getSceneNo();
                if (map.containsKey(key)) {
                    System.out.println("シーン番号が重複しています: " + key);
                    System.exit(-1);
                }
                if (isDebug) System.out.println("loadXML: " + story.getSceneNo());
                map.put(story.getSceneNo(), story);
            } else if (xml.isDirectory()) {
                if (isDebug) System.out.println("Child dir: " + xml.getPath());
                Map<Integer, Scene> sceneMap2nd = loadStories(xml.getPath());
                map.putAll(sceneMap2nd);
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
        if (jobs == null || jobs.size() == 0) {
            throw new RpgException("Job.xmlがロードされてません。" + jobs);
        }
        Map<String, Player> map = new HashMap<>();
        List<Player> playerList = XMLUtil.loadPlayer(directory + "/" + fileName);

        for (Player player : playerList) {
            Job job = jobs.get(player.getJobStr());
            if (isDebug) System.out.println("*** " + job.getName() + " ***");
            player.setJob(job);
            map.put(player.getName(), player);
        }
        return map;
    }

    public Map<String, Job> loadJobs(String directory, String fileName) throws RpgException {
        if (commandMap == null || commandMap.size() == 0) {
            throw new RpgException("先にコマンドを生成してください。");
        }
        String path = directory + "/" + fileName;
        Path p = Paths.get(path);

        Map<String, Job> map = XMLUtil.loadJobs(path);

        Set<String> set = map.keySet();
        for (String key : set) {
            Job job = map.get(key);
            List<String> commands = job.getCommandStr();
            List<Command> cmdList = new ArrayList<>();
            for (String cmd : commands) {
                if (isDebug) System.out.println(job.getName() + " = Load cmd: " + cmd);
                cmdList.add(commandMap.get(cmd));
            }
            job.setCommandList(cmdList);
            cmdList = null;
        }
        return map;
    }

    public Map<String, Command> loadCommands(String directory, String fileName) throws RpgException {
        List<Command> commandList = XMLUtil.loadCommands(directory, fileName);
        Map<String, Command> map = new HashMap<>();

        for (Command cmd : commandList) {
            map.put(cmd.getId(), cmd);
        }
        return map;
    }

    public Map<String, MonsterType> loadMonsterType(String directory, String fileName) throws RpgException {
        String path = directory + "/" + fileName;
        Path p = Paths.get(path);

        Map<String, MonsterType> map = XMLUtil.loadMonsterType(path);
        Set<String> set = map.keySet();
        List<Command> cmdList = new ArrayList<>();
        for (String key : set) {
            MonsterType type = map.get(key);
            List<String> cmds = type.getCommandStr();
            for (String cmd : cmds) {
                cmdList.add(commandMap.get(cmd));
            }
            type.setCommandList(cmdList);
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
            if (isDebug) System.out.println("Type: " + mons.getTypeStr());
            MonsterType type = monsterTypeMap.get(mons.getTypeStr());
            mons.setType(type);
            map.put(mons.getNo(), mons);
        }
        return map;
    }

    /**
     * 設定定義ファイルを読み込む、ConfigLoaderのparamMapにParansをセット。
     * @param directory 対象ディレクトリ
     * @param fileName 対象ファイル
     * @return　Config 設定クラス
     * @throws RpgException
     */
    public Config loadConfig(String directory, String fileName) throws RpgException {
        Config conf = XMLUtil.loadConfig(directory, fileName);
        List<Params> paramsList = conf.getParams();
        paramsMap = new HashMap<>();

        int maxSize = 0;
        for (Params params : paramsList) {
            if (params.getName().length() > maxSize) {
                maxSize = params.getName().length();
            }
            paramsMap.put(params.getKey(), params);
        }
        MAX_PARAM_SIZE = maxSize;

        List<Params> moneys = conf.getMoney();
        return conf;
    }

    /**
     * モンスターを指定して呼び出す。
     * モンスターを呼び出す処理。ほんとはインスタンスをコピーして
     * モンスターを増やしたかったが、インスタンスの参照が変わらないので。
     * モンスターの値をリセットするようにしている。
     *
     * @param no モンスター番号
     * @return モンスターオブジェクト
     */
    public Monster callMonster(int no) {
        Monster mst = monsters.get(no);
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

    /**
     * モンスターをランファムに呼び出す。
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

    /**
     * @param sel 選択項目
     * @return ITEM, WEP, ARMのいずれか
     */
    public static int isKeyInMap(Select sel) throws RpgException {
        String shohinCd = sel.getShohinCd();
        return isKeyInMap(shohinCd);
    }

    /**
     * Item, Wepon, Armorのいずれかの商品コードを以下のように判定する・
     * ITEM = 0: アイテムの商品コード
     * WEP = 1: 武器の商品コード
     * ITEM = 2: 防具の商品コード
     * @param shohinCd 商品コード
     * @return ITEM, WEP, ARMのいずれか
     */
    public static int isKeyInMap(String shohinCd) throws RpgException {
        // 商品コード＝アイテム、武器、防具のID
        Map<String, Item> itemMap = ConfigLoader.getInstance().getItemMap();
        Map<String, Wepon> wepMap = ConfigLoader.getInstance().getWepMap();
        Map<String, Armor> armMap = ConfigLoader.getInstance().getArmMap();
        if ("".equals(shohinCd)) {
            throw new RpgException("商品コードが空です・");
        }
        // 各キーは重複していない
        boolean b1 = itemMap.containsKey(shohinCd);
        boolean b2 = wepMap.containsKey(shohinCd);
        boolean b3 = armMap.containsKey(shohinCd);

        int isItemOrWepOrArm = 0;
        if (b1) {
            isItemOrWepOrArm = ITEM;
        }
        if (b2) {
            isItemOrWepOrArm = WEP;
        }
        if (b3) {
            isItemOrWepOrArm = ARM;
        }
        if (b1 == false && b2 == false && b3 == false) {
            throw new RpgException("想定外の商品コードです。" + shohinCd);
        }
        return isItemOrWepOrArm;
    }
    /**
     * 選択項目の文言にセットされている商品コードを商品名に書き換える。
     * そして、対象の商品コードからアイテムを取得する。以下の３バターンがある。
     * ITEM = Item
     * WEP = Wepon
     * ARM = Armor
     *
     * @param sel 選択オブジェクト
     * @param itemType -1の場合はチェックを行わない
     * @return Item 取得したアイテム(Items.xmlに定義しているアイテム)
     * @throws RpgException
     */
    public static Item getItemFromShohinCd(Select sel, int itemType) throws RpgException {
        String shohinCd = sel.getShohinCd();
        // 各マップにはキーが重複していない事を前提とする
        Map<String, Item> itemMap = ConfigLoader.getInstance().getItemMap();
        Map<String, Wepon> wepMap = ConfigLoader.getInstance().getWepMap();
        Map<String, Armor> armMap = ConfigLoader.getInstance().getArmMap();
        int shohinHandle = isKeyInMap(sel);
        if (itemType > -1 && itemType != shohinHandle) {
            throw new RpgException("商品コードと、指定が合いいません。" + shohinCd);
        }
        Item retItem = null;
        switch(shohinHandle) {
            case ITEM:
                Item it = itemMap.get(shohinCd);
                retItem = it;
                sel.setMongon(it.getName());
                break;
            case WEP:
                Wepon wep = wepMap.get(shohinCd);
                retItem = wep;
                sel.setMongon(wep.getName());
                break;
            case ARM:
                Armor arm = armMap.get(shohinCd);
                sel.setMongon(arm.getName());
                retItem = arm;
                break;
        }
        return retItem;
    }

    /**
     * 商品コードからアイテムを取得する。以下の３バターンがある。
     * ITEM = Item
     * WEP = Wepon
     * ARM = Armor
     *
     * @param shohinCd 選択オブジェクト
     * @return Item 取得したアイテム(Items.xmlに定義しているアイテム)
     * @throws RpgException
     */
    public static Item getItemFormShohinCd(String shohinCd) throws RpgException {
        // 各マップにはキーが重複していない事を前提とする
        Map<String, Item> itemMap = ConfigLoader.getInstance().getItemMap();
        Map<String, Wepon> wepMap = ConfigLoader.getInstance().getWepMap();
        Map<String, Armor> armMap = ConfigLoader.getInstance().getArmMap();

        int shohinHandle = isKeyInMap(shohinCd);

        Item retItem = null;
        switch(shohinHandle) {
            case ITEM:
                Item it = itemMap.get(shohinCd);
                retItem = it;
                break;
            case WEP:
                Wepon wep = wepMap.get(shohinCd);
                retItem = wep;
                break;
            case ARM:
                Armor arm = armMap.get(shohinCd);
                retItem = arm;
                break;
        }
        return retItem;
    }

    /**
     * でフォルト設定されている通貨の名前をかどうか判定する。
     * @param target
     * @return
     */
    public boolean isCurrentMoney(String target, boolean isKigo) {
        if (isKigo) {
            return currentMooney.getKey().equals(target);
        } else {
            return currentMooney.getName().equals(target);
        }
    }

    /**
     * 金額の場合は、式から記号をさk所する。
     * @param formula 金額を含む効果式
     * @return 金額の記号を削除した式
     */
    public String convertMoneyStr(String formula) {
        String key = currentMooney.getKey();
        return formula.replaceAll(key, "0");
    }
}
