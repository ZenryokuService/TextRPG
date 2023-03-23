package jp.zenryoku.rpg.utils;

import jp.zenryoku.rpg.data.Formula;
import jp.zenryoku.rpg.data.Formulas;
import jp.zenryoku.rpg.data.SceneType;
import jp.zenryoku.rpg.data.config.*;
import jp.zenryoku.rpg.data.config.Element;
import jp.zenryoku.rpg.data.param.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.bind.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.*;

import jp.zenryoku.rpg.exception.RpgException;
import jp.zenryoku.rpg.character.*;


/**
 * クラス XMLUtil の注釈をここに書きます.
 * XMLファイルの読込からオブジェクト生成を行います。
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
public class XMLUtil
{
    private static final boolean isDebug = false;
    private static final String SEP = System.lineSeparator();
    private static final String SPACER = "                          ";

    private static void printData(String title, String tagName, String tagVlue) {
        System.out.println(title + " TagName: " + tagName + " TagValue: " + tagVlue);
    }

    private static boolean isEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }
    
    /**
     * XMLファイルをロードして対象のクラスを生成する。
     * @param path　Pathsクラスで取得する
     * @param clz 取得するクラスオブジェクト
     */
    private static Object loadXml(Path path, Class clz) {
        Object o = null;
        try {
            o = JAXB.unmarshal(path.toFile(), clz);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return o;
    }

    /**
     * XMLファイルをロードして対象のクラスを生成する。
     * @param file　Fileクラスで取得する
     * @param clz 取得するクラスオブジェクト
     */
    private static Object loadXml(File file, Class clz) {
        Object o = null;
        try {
            o = JAXB.unmarshal(file, clz);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return o;
    }
    
    /**
     * 引数にあるパスからファイルを読み取り文字列を返却する。
     * @param path テキストファイルへのパス
     */
    public static String loadText(String path, boolean hasSpace) {
        StringBuilder build = new StringBuilder();
        
        Path filePath = Paths.get(path);
        try {
            BufferedReader read = Files.newBufferedReader(filePath);
            String line = null;
            while((line = read.readLine()) != null) {
                if (hasSpace) {
                    build.append(SPACER + line + SEP);
                } else {
                    build.append(line + SEP);
                }
            }
        } catch (IOException e) {
            System.out.println("パス指定が不適切です" + path);
            e.printStackTrace();
            System.exit(-1);
        }
        return build.toString();
    }

    public static Map<String, Formula> loadFormulas(String directory, String fileName) throws RpgException {
        Path path = Paths.get(directory, fileName);
        Formulas formulas = null;

        try {
            formulas = JAXB.unmarshal(path.toFile(), Formulas.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        List<Formula> list = formulas.getFormulas();
        
        Map<String, Formula> map = new HashMap<>();
        for (Formula f : list) {
            if ("".equals(f.getFormulaStr()) || f.getFormulaStr() == null) {
                throw new RpgException("効果式がありません");
            }
            if (isDebug) System.out.println("ID: " + f.getId() + " Str: " + f.getFormulaStr());
            map.put(f.getId(), f);
        }
        return map;
    }
    
    public static Config loadConfig(String directory, String fileName) {
        Path path = Paths.get(directory, fileName);
        Config config = null;

        try {
            config = JAXB.unmarshal(path.toFile(), Config.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return config;
    }

    public static void exportConfigJaxb(String directory, String fileName) {
        Path path = Paths.get(directory, fileName);
        Config conf = createConfig();
        
        //worlds.setWorlds(list);
        JAXBContext ctx = null;
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            ctx =  JAXBContext.newInstance(Config.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(conf, writer);
            JAXB.marshal(conf, path.toFile());
            writer.close();
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        Marshaller sharl = null;

    }

    private static Config createConfig() {
        Config conf = Config.getInstance();
        // 表示項目
        List<String> views = new ArrayList<>();
        views.add("HP");
        views.add("MP");
        views.add("LV");
        conf.setViews(views);
        // 通貨
        List<Params> money = new ArrayList<>();
        money.add(new Params("NIG", "ニギ", 0));
        money.add(new Params("GLD", "ゴールド", 0));
        conf.setMoney(money);
        // 使用言語
        List<Params> langs = new ArrayList<>();
        langs.add(new Params("CLANG", "シーラン", 0));
        langs.add(new Params("MAGILAN", "魔族後", 0));
        conf.setLanguages(langs);
        // エレメント
        List<Element> elments = new ArrayList<>();
        elments.add(new Element("FIR", "火"));
        elments.add(new Element("WIN", "風"));
        elments.add(new Element("WAT", "水"));
        elments.add(new Element("EAT", "土"));
        conf.setElement(elments);
        // プレーヤーステータス
        List<Params> playerStatus = new ArrayList<>();
        playerStatus.add(new Params("HP", "ヒットポイント", 0, "生命力・体力"));
        playerStatus.add(new Params("MP", "マジックポイント", 0, "魔法力"));
        playerStatus.add(new Params("LV", "マジックポイント", 0, "強さの段階"));
        playerStatus.add(new Params("POW", "ちから", 0, "攻撃力、武器・防具の持てる合計重量を示す。"));
        playerStatus.add(new Params("AGI", "すばやさ", 0, "行動の速さ、相手と５以上の差があるとき２回攻撃。"));
        playerStatus.add(new Params("INT", "かしこさ", 0, "魔法・術などの効果量を示す。"));
        playerStatus.add(new Params("DEX", "きようさ", 0, "使える武器、防具、魔法・術などの種類が増える。"));
        playerStatus.add(new Params("KSM", "カリスマ", 0, "人やモンスターに好かれる度合、統率力を示す。"));
        playerStatus.add(new Params("JLV", "じゅくれんど", 0, "武器を使用する回数により武器の扱いがうまくなる。"));
        playerStatus.add(new Params("ATK", "攻撃力", 0, "物理的な攻撃力を示す。"));
        playerStatus.add(new Params("DEF", "防御力", 0, "物理的な防御力を示す。"));
        playerStatus.add(new Params("MPW", "魔法威力", 0, "魔法・術、単体の効果値。"));
        playerStatus.add(new Params("TSM", "魔除け力", 0, "防具・アクセサリ内にある。魔法・術に対する魔除けの類の力、防具にのみついている。"));
        playerStatus.add(new Params("BPK", "バックパック", 0, "道具の持てる数"));
        conf.setParams(playerStatus);
        // プレーヤー状態
        List<State> stateList = new ArrayList<>();
        stateList.add(new State("POI", "HP-10%@1"));
        stateList.add(new State("MAH", "NAC@2"));
        conf.setStatusList(stateList);
        // アイテム
        List<Params> items = new ArrayList<>();
        items.add(new Params("WEV", "武器攻撃力", 0));
        items.add(new Params("ARV", "防具防御力", 0));
        conf.setItems(items);
        return conf;
    }
    
    public static List<World> loadWorldJaxb(String directory, String fileName) {
        Path path = Paths.get(directory, fileName);
        List<World> worldList = new ArrayList<>();

        try {
            Worlds w = JAXB.unmarshal(path.toFile(), Worlds.class);
            worldList = w.getWorlds();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return worldList;
    }

    public static List<World> exportWorldJaxb(String directory, String fileName) {
        Path path = Paths.get(directory, fileName);
        World world = createWorld();
        World world2 = createWorld();
        world2.setId("pppp");
        world2.setName("テスト用の世界");

        Worlds worlds = new Worlds();
        List<World> list = worlds.getWorlds();
        list.add(world2);
        list.add(world);
        //worlds.setWorlds(list);
        JAXBContext ctx = null;
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            ctx =  JAXBContext.newInstance(Worlds.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(worlds, writer);
            JAXB.marshal(worlds, path.toFile());
            writer.close();
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        Marshaller sharl = null;


        return null;
    }

    public static void exportMonsterJaxb(String directory, String fileName) {
        Path path = Paths.get(directory, fileName);
        Monster monster = createMonster();
        monster.setName("スライム");
        Monsters ppp = new Monsters();
        
        List<Monster> list = new ArrayList<>();
        list.add(monster);
        ppp.setMonsters(list);

        //worlds.setWorlds(list);
        JAXBContext ctx = null;
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            ctx =  JAXBContext.newInstance(Monsters.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(ppp, writer);
            JAXB.marshal(ppp, path.toFile());
            writer.close();
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        Marshaller sharl = null;
    }

    
    public static void exportPlayerJaxb(String directory, String fileName) {
        Path path = Paths.get(directory, fileName);
        Player player = createPlayer();
        Player player1 = createPlayer();
        player1.setName("大河");
        Players ppp = new Players();
        
        List<Player> list = new ArrayList<>();
        list.add(player);
        list.add(player1);
        ppp.setPlayers(list);

        //worlds.setWorlds(list);
        JAXBContext ctx = null;
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            ctx =  JAXBContext.newInstance(Players.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(ppp, writer);
            JAXB.marshal(ppp, path.toFile());
            writer.close();
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        Marshaller sharl = null;
    }
    
    public static List<Player> loadPlayer(String path) throws RpgException {
        List<Player> playerList = null;
        Path p = Paths.get(path);
        Players players = (Players) loadXml(p, Players.class);
        
        playerList = players.getPlayers();
        if (playerList.size() == 0) {
            throw new RpgException("プレーヤーが設定されていません" + path);
        }
        return playerList;
    }
    
    public static List<Monster> loadMonsters(String path) throws RpgException {
        List<Monster> monsterList = null;
        Path p = Paths.get(path);
        Monsters monsters = (Monsters) loadXml(p, Monsters.class);
        
        monsterList = monsters.getMonsters();
        if (monsterList.size() == 0) {
            throw new RpgException("プレーヤーが設定されていません" + path);
        }
        return monsterList;
    }

    public static Items loadItems(String path) throws RpgException {
        Items items = null;
        Path p = Paths.get(path);
        items = (Items) loadXml(p, Items.class);

        List<Item> itemList = items.getItems();
        List<Wepon> wepList = items.getWepons();
        List<Armor> armList = items.getArmors();
        if (itemList.size() == 0) {
            throw new RpgException("アイテムが設定されていません" + path);
        }
        if (wepList.size() == 0) {
            throw new RpgException("武器が設定されていません" + path);
        }
        if (armList.size() == 0) {
            throw new RpgException("防具が設定されていません" + path);
        }
        return items;
    }

    public static void exportItemsJaxb(String directory, String fileName) {
        Path path = Paths.get(directory, fileName);
        ArrayList<Item> itList = new ArrayList<>();
        itList.add(new Item("yakuso", "やくそう", new Formula("HP+10")));
        itList.add(new Item("dokuso", "どくそう", new Formula("HP+10")));
        itList.add(new Item("yakuso", "プロテイン", new Formula("POW+10")));

        ArrayList<Wepon> wepList = new ArrayList<>();
        wepList.add(new Wepon("donosord", "どうのつるぎ", 10, new Formula("POW+1"), new Formula("WIT+1")));
        wepList.add(new Wepon("tetunosord", "てつのつるぎ", 15, new Formula("POW+1"), new Formula("WIT+1")));

        ArrayList<Armor> arList = new ArrayList<>();
        arList.add(new Armor("donoarmor", "どうのよろい", 8, new Formula("POW+1"), new Formula("WIT+2")));
        arList.add(new Armor("tetunoarmor", "てつのよろい", 12, new Formula("POW+1"), new Formula("WIT+2")));

        Items items = new Items();
        items.setItems(itList);
        items.setWepons(wepList);
        items.setArmors(arList);
        JAXBContext ctx = null;
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            ctx = JAXBContext.newInstance(Items.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(items, writer);
            JAXB.marshal(items, path.toFile());
            writer.close();
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        Marshaller sharl = null;
    }

    public static void exportStoryJaxb(String directory, String fileName) {
        Path path = Paths.get(directory, fileName);
        Scene story = createStory();
        ArrayList<Select> selList = new ArrayList<>();
        selList.add(new Select(1, "ニューゲーム"));
        selList.add(new Select(2, "コンチニュー"));
        story.setSelects(selList);
        
        //worlds.setWorlds(list);
        JAXBContext ctx = null;
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            ctx =  JAXBContext.newInstance(Scene.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(story, writer);
            JAXB.marshal(story, path.toFile());
            writer.close();
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        Marshaller sharl = null;
    }
    
    /**
     * Story_XXX.xml内のpathタグにセットされた値があれば
     * 対象のファイルを読み込む。テキストファイルの想定。
     * @param path 読み込む絵tキスとファイルのパス
     */
    public static Scene loadStory(String path) throws RpgException {
        Path p = Paths.get(path);
        Scene story = (Scene) loadXml(p, Scene.class);
        
        if (isEmpty(story.getStory()) && isEmpty(story.getPath())) {
            throw new RpgException("story, pathのどちらかを定義してください。");
        }
        // PATH指定ありの場合は対象のファイルを読み込む
        if (isEmpty(story.getPath()) == false) {
            story.setStory(loadText(story.getPath(), story.isCenter()));
        }
        // エフェクトシーンでは計算式が必須
        if (SceneType.EFFECT.equals(story.getSceneType())) {
            List<Select> selList = story.getSelects();
            for (Select s : selList) {
                if (s.getFormula() == null) {
                    throw new RpgException("エフェクトシーンはformulaタグを記入してください。: " + story.getName());
                }
            }
        }
        return story;
    }

    /**
     * Jobs.xmlをロードして、Mapに格納する。
     * JobのIDをキーに、Jobクラスをセットする。
     * @param path ファイルのパス
     * @return Map<String, Job>
     * @throws RpgException
     */
    public static Map<String, Job> loadJobs(String path) {
        Path p = Paths.get(path);
        Jobs jobs = (Jobs) loadXml(p, Jobs.class);

        List<Job> jobList = jobs.getJobs();
        Map<String, Job> map = new HashMap<>();
        for (Job job : jobList) {
            map.put(job.getId(), job);
        }
        return map;
    }

    public static Scene loadJobs(File file) throws RpgException {
        Scene story = (Scene) loadXml(file, Scene.class);
        if (isEmpty(story.getStory()) && isEmpty(story.getPath())) {
            throw new RpgException("story, pathのどちらかを定義してください。");
        }
        // PATHしてありの場合は対象のファイルを読み込む
        if (isEmpty(story.getPath()) == false) {
            story.setStory(loadText(story.getPath(), story.isCenter()));
        }
        return story;
    }

    public static Map<String, MonsterType> loadMonsterType(String path) {
        Path p = Paths.get(path);
        MonsterTypes jobs = (MonsterTypes) loadXml(p, MonsterTypes.class);

        List<MonsterType> jobList = jobs.getMonsterTypes();
        Map<String, MonsterType> map = new HashMap<>();
        for (MonsterType job : jobList) {
            map.put(job.getId(), job);
        }
        return map;
    }



    /**
     * Story_XXX.xml内のpathタグにセットされた値があれば
     * 対象のファイルを読み込む。テキストファイルの想定。
     * @param file 読み込む絵tキスとファイルのパス
     */
    public static Scene loadStory(File file) throws RpgException {
        Scene story = (Scene) loadXml(file, Scene.class);
        if (isEmpty(story.getStory()) && isEmpty(story.getPath())) {
            throw new RpgException("story, pathのどちらかを定義してください。");
        }
        // PATHしてありの場合は対象のファイルを読み込む
        if (isEmpty(story.getPath()) == false) {
            story.setStory(loadText(story.getPath(), story.isCenter()));
        }
        return story;
    }

    public static void exportJobJaxb(String directory, String fileName) {
        Path path = Paths.get(directory, fileName);
        Jobs jobs = createJobs();

        //worlds.setWorlds(list);
        JAXBContext ctx = null;
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            ctx =  JAXBContext.newInstance(Jobs.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(jobs, writer);
            JAXB.marshal(jobs, path.toFile());
            writer.close();
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        Marshaller sharl = null;
    }

    public static void exportMonsterTypeJaxb(String directory, String fileName) {
        Path path = Paths.get(directory, fileName);
        MonsterTypes jobs = createMonsterTypes();

        //worlds.setWorlds(list);
        JAXBContext ctx = null;
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            ctx =  JAXBContext.newInstance(MonsterTypes.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(jobs, writer);
            JAXB.marshal(jobs, path.toFile());
            writer.close();
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        Marshaller sharl = null;
    }

    public static MonsterTypes createMonsterTypes() {
        MonsterTypes jobs = new MonsterTypes();
        List<MonsterType> jobList = new ArrayList<>();

        MonsterType yusha = new MonsterType();
        yusha.setId("BRV");
        yusha.setName("ゆうしゃ");
        List<Command> commandList = new ArrayList<>();
        commandList.add(new Command("ATK", "こうげき", new Formula("ATK", "ATK", "HP-")));
        commandList.add(new Command("BST", "大こうげき", new Formula("BST", "ATK + 3", "HP-")));
        commandList.add(new Command("INR", "いのり", new Formula("INE", "HP + 3", "HP+")));
        yusha.setCommandList(commandList);

        List<Params> paramList = new ArrayList<>();
        paramList.add(new Params("POW", "ちから", 110, "こうげき力"));
        paramList.add(new Params("AGI", "すばやさ", 120, "すばやさ"));
        paramList.add(new Params("INT", "かしこさ", 60, "まほうこうげき力"));
        paramList.add(new Params("LUK", "うんのよさ", 120, "うんのよさ"));
        yusha.setParamList(paramList);
        jobList.add(yusha);

        MonsterType warria = new MonsterType();
        warria.setId("WAR");
        warria.setName("せんし");
        List<Command> commandList1 = new ArrayList<>();
        commandList1.add(new Command("ATK", "こうげき", new Formula("ATK", "ATK", "HP-")));
        commandList1.add(new Command("BST", "大こうげき", new Formula("BST", "ATK + 3", "HP-")));
        commandList1.add(new Command("IKR", "戦士の怒り", new Formula("IKR", "HP + 3", "HP+")));
        warria.setCommandList(commandList);

        List<Params> paramList1 = new ArrayList<>();
        paramList1.add(new Params("POW", "ちから", 120, "こうげき力"));
        paramList1.add(new Params("INT", "かしこさ", 30, "まほうこうげき力"));
        paramList1.add(new Params("LUK", "うんのよさ", 120, "うんのよさ"));
        warria.setParamList(paramList);
        jobList.add(warria);

        jobs.setMonsterTypes(jobList);
        return jobs;
    }


    public static Jobs createJobs() {
        Jobs jobs = new Jobs();
        List<Job> jobList = new ArrayList<>();

        Job yusha = new Job();
        yusha.setId("BRV");
        yusha.setName("ゆうしゃ");
        List<Command> commandList = new ArrayList<>();
        commandList.add(new Command("ATK", "こうげき", new Formula("ATK", "ATK", "HP-")));
        commandList.add(new Command("BST", "大こうげき", new Formula("BST", "ATK + 3", "HP-")));
        commandList.add(new Command("INR", "いのり", new Formula("INE", "HP + 3", "HP+")));
        yusha.setCommandList(commandList);

        List<Params> paramList = new ArrayList<>();
        paramList.add(new Params("POW", "ちから", 110, "こうげき力"));
        paramList.add(new Params("AGI", "すばやさ", 120, "すばやさ"));
        paramList.add(new Params("INT", "かしこさ", 60, "まほうこうげき力"));
        paramList.add(new Params("LUK", "うんのよさ", 120, "うんのよさ"));
        yusha.setParamList(paramList);
        jobList.add(yusha);

        Job warria = new Job();
        warria.setId("WAR");
        warria.setName("せんし");
        List<Command> commandList1 = new ArrayList<>();
        commandList1.add(new Command("ATK", "こうげき", new Formula("ATK", "ATK", "HP-")));
        commandList1.add(new Command("BST", "大こうげき", new Formula("BST", "ATK + 3", "HP-")));
        commandList1.add(new Command("IKR", "戦士の怒り", new Formula("IKR", "HP + 3", "HP+")));
        warria.setCommandList(commandList);

        List<Params> paramList1 = new ArrayList<>();
        paramList1.add(new Params("POW", "ちから", 120, "こうげき力"));
        paramList1.add(new Params("INT", "かしこさ", 30, "まほうこうげき力"));
        paramList1.add(new Params("LUK", "うんのよさ", 120, "うんのよさ"));
        warria.setParamList(paramList);
        jobList.add(warria);

        jobs.setJobs(jobList);
        return jobs;
    }



    public static Scene createStory() {
        Scene story = new Scene("First" ,"はじめの");
        story.setDescription("はじめのタイトル表示を行いニューゲーム、コンテニューを選択、実行する");
        story.setSceneNo(0);
        story.setSceneType(SceneType.STORY);
        story.setNextScene(1);
        story.setCanSelectNextScene(true);
        story.setPath("config/stories/Story_0001.xml");
        story.setStory("その日は朝から夜だった");
        story.setCenter(true);

        return story;
    }
    
    public static Player createPlayer() {
        Player player = new Player();
        player.setName("ゆうしゃ");
        player.setSex(SEX.MAN);
        Job job = new Job();
        job.setId("BRV");
        job.setName("ゆうしゃ");
        job.setDescription("ゆうきあるもの");
        List<Params> paramList = new ArrayList<>();
        paramList.add(new Params("POW","ちから", 100));
        paramList.add(new Params("AGI","すばやさ", 120));
        paramList.add(new Params("INT","かしこさ", 60));
        job.setParamList(paramList);
        List<Command> commandList = new ArrayList<>();
        commandList.add(new Command("BST","大こうげき", new Formula("ATK + 3")));
        commandList.add(new Command("INR","いのり", new Formula("HP + 3")));
        job.setCommandList(commandList);
        player.setJob(job);
        Map<String, Params> statusMap = new HashMap<>();
        // ステータス 
        Params hp = new Params("HP", "ヒットポイント", 12);
        statusMap.put(hp.getKey(), hp);
        Params mp = new Params("MP", "マジックトポイント", 12);
        statusMap.put(mp.getKey(), mp);
        Params pow = new Params("POW", "ちから", 6);
        statusMap.put(pow.getKey(), pow);
        Params agi = new Params("AGI", "すばやさ", 4);
        statusMap.put(agi.getKey(), agi);
        Params pInt = new Params("INT", "かしこさ", 1);
        statusMap.put(pInt.getKey(), pInt);
        Params luk = new Params("LUK", "うんのよさ", 10);
        statusMap.put(luk.getKey(), luk);
        player.setStatus(statusMap);
        // 装備
        player.setWepon(new Wepon("yakuso", "どうのつるぎ", 10, new Formula("POW+1"), new Formula("WIT+1")));
        player.setArmor(new Armor("yakuso", "どうのよろい", 8, new Formula("POW+1"), new Formula("WIT+2")));
        // アイテム
        List<Item> items = new ArrayList<>();
        items.add(new Item("yakuso", "やくそう", new Formula("HP+10")));
        items.add(new Item("dokuso", "どくそう", new Formula("HP-10")));
        items.add(new Item("protain", "プロテイン", new Formula("POW+10")));
        player.setItems(items);
        // 状態
        
        System.out.println(player.getName());
        return player;
    }
    
    public static Monster createMonster() {
        Monster player = new Monster();
        player.setName("スライム");
        // モンスタータイプ
        MonsterType type = new MonsterType();
        type.setId("SLM");
        type.setName("スライム");
        type.setDescription("ゆうきあるもの");
        List<Params> paramList = new ArrayList<>();
        paramList.add(new Params("POW","ちから", 100));
        paramList.add(new Params("AGI","すばやさ", 120));
        paramList.add(new Params("INT","かしこさ", 60));
        type.setParamList(paramList);
        List<Command> commandList = new ArrayList<>();
        commandList.add(new Command("BST","大こうげき", new Formula("ATK + 3")));
        commandList.add(new Command("INR","いのり", new Formula("HP + 3")));
        type.setCommandList(commandList);
        player.setType(type);
        
        Map<String, Params> statusMap = new HashMap<>();
        // ステータス 
        Params hp = new Params("HP", "ヒットポイント", 12);
        statusMap.put(hp.getKey(), hp);
        Params mp = new Params("MP", "マジックトポイント", 12);
        statusMap.put(mp.getKey(), mp);
        Params pow = new Params("POW", "ちから", 6);
        statusMap.put(pow.getKey(), pow);
        Params agi = new Params("AGI", "すばやさ", 4);
        statusMap.put(agi.getKey(), agi);
        Params pInt = new Params("INT", "かしこさ", 1);
        statusMap.put(pInt.getKey(), pInt);
        Params luk = new Params("LUK", "うんのよさ", 10);
        statusMap.put(luk.getKey(), luk);
        player.setStatus(statusMap);
        // 装備
        player.setWepon(new Wepon("yakuso", "どうのつるぎ", 10, new Formula("POW+1"), new Formula("WIT+1")));
        player.setArmor(new Armor("yakuso", "どうのよろい", 8, new Formula("POW+1"), new Formula("WIT+2")));
        // アイテム
        List<Item> items = new ArrayList<>();
        items.add(new Item("yakuso", "やくそう", new Formula("HP+10")));
        items.add(new Item("dokuso", "どくそう", new Formula("HP-10")));
        items.add(new Item("protain", "プロテイン", new Formula("POW+10")));
        player.setItems(items);
        // 状態
        
        System.out.println(player.getName());
        return player;
    }
    
    public static World createWorld() {
        World world = new World();
        world.setId("UpperWorld");
        world.setName("上の世界");
        world.setDescription("世界の説明");
        List<Nature> nList = new ArrayList<>();
        Nature nature = new Nature();
        nature.setId("natureId");
        nature.setName("自然名");
        nature.setDescription("自然の説明");
        nature.setPath("");
        nature.setFood_chain("食物連鎖");
        world.setNature(nature);
        List<Climate> cList = new ArrayList<>();
        Climate cli1 = new Climate();
        cli1.setId("A");
        cli1.setName("サンプル気候");
        cli1.setDescription("サンプル気候の説明");
        cList.add(cli1);

        cli1.setId("B");
        cli1.setName("B気候");
        cList.add(cli1);

        cli1.setId("C");
        cli1.setName("C気候");
        cList.add(cli1);

        cli1.setId("D");
        cli1.setName("D気候");
        cList.add(cli1);
        nature.setClimateList(cList);

        List<Civilization> ciList = world.getCivilizations();
        Civilization civ = new Civilization();
        civ.setId("civil1");
        civ.setName("文明１");
        civ.setDescription("文明１の説明");
        civ.setTechnology("文明１の技術");
        civ.setArts("文明１の芸術");
        civ.setStructures("文明１の社会構造");
        civ.setCharacter("文明１の文字");
        ciList.add(civ);
        
        

        Civilization civ1 = new Civilization();
        civ1.setId("civil2");
        civ1.setName("文明２");
        civ1.setDescription("文明２の説明");
        civ1.setTechnology("文明２の技術");
        civ1.setArts("文明２の芸術");
        civ1.setStructures("文明２の社会構造");
        civ1.setCharacter("文明２の文字");
        ciList.add(civ1);

        List<Country> couList = world.getCountries();
        Country country = new Country();
        country.setId("country1");
        country.setName("A国");
        country.setDescription("A国の説明");
        couList.add(country);

        Country country1 = new Country();
        country1.setId("country2");
        country1.setName("B国");
        country1.setDescription("B国の説明");
        couList.add(country1);

        List<Culture> culList = world.getCultures();
        Culture cult = new Culture();
        cult.setId("culture1");
        cult.setName("文化１");
        cult.setDescription("文化１の説明");
        cult.setOrganization("文化１の組織");
        cult.setValues("文化１の価値観");
        cult.setHabit("文化１の生活習慣");
        cult.setNorm("文化１の規範");
        cult.setLife_style("文化１の生活スタイル");
        cult.setView_of_world("文化１の世界観");
        cult.setSocial_structure("文化１の社会構造");
        culList.add(cult);

        return world;
    }
}
