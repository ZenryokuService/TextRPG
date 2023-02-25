package jp.zenryoku.rpg.utils;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.bind.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;
import java.beans.XMLDecoder;
import jp.zenryoku.rpg.exception.RpgException;
import jp.zenryoku.rpg.data.config.*;
import java.lang.reflect.Field;


/**
 * クラス XMLUtil の注釈をここに書きます.
 * XMLファイルの読込からオブジェクト生成を行います。
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
public class XMLUtil
{
    private static final boolean isDebug = true;
    private static final String SEP = System.lineSeparator();

        /**
     * XMLドキュメント(ファイル)を読み込む。
     * @param directory ファイルの配置しているディレクトリ
     * @param fileName ファイル名
     * @return Documentオブジェクト
     * @throws RpgException ファイルの読み込みエラー
     */
    private static Document loadDocumentBuilder(String directory, String fileName) throws RpgException {
        //creating a constructor of file class and parsing an XML files
        Path path = Paths.get(directory, fileName);
        File file = path.toFile();// ("/Monster.xml");
        //an instance of factory that gives a document builder
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //an instance of builder to parse the specified xml file
        DocumentBuilder db = null;
        Document doc = null;
        try {
            db = dbf.newDocumentBuilder();
            doc = db.parse(file);
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            throw new RpgException("パーサー設定" + ": " + e.getMessage());
        } catch (SAXException e) {
            e.printStackTrace();
            throw new RpgException("MessageConst.ERR_XML_PERSE"+ ": " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RpgException("MessageConst.ERR_IOEXCEPTION" + ": " + e.getMessage());
        }
        return doc;
    }
    
    /**
     * 各種設定ファイルを読み込むときに、ファイル名に対応した
     * クラスを生成して返却する。
     *
     * @param fileName
     * @return ConfigRpptを継承しているクラス
     */
    public static ConfigIF createConfig(String fileName) {
        ConfigIF conf = null;
        switch (fileName) {
            case StoryConfig.WORLD_XML:
                conf = new World();
                break;
            case StoryConfig.NATURE_XML:
                conf = new Nature();
                break;
            case StoryConfig.CREATURE_XML:
                conf = new Creature();
                break;
            case StoryConfig.REGIONS_XML:
                conf = new Region();
                break;
            case StoryConfig.EFFECTS_XML:
                conf = new Effect();
                break;
            case StoryConfig.STM_XML:
                conf = new STM();
                break;
            case StoryConfig.CPMMADS_XML:
                conf = new Command();
                break;
            case StoryConfig.JOB_XML:
                conf = new Job();
                break;
            case StoryConfig.MONSTER_TYPE_XML:
                conf = new MonsterType();
            case StoryConfig.MONSTERS_XML:
                /** TODO-[MonsterクラスはPlayerクラスを継承する] */
                //conf = new Monster();
                break;
            default:
                System.out.println("想定外の設定ファイル名です。 : " + fileName);
                System.exit(-1);
        }
        return conf;
    }

    /**
     * 各種設定ファイルを読み込むときに、タグ名に対応した
     * クラスを生成してWorldクラスにセットしてWorldクラスを返却する。
     *
     * @param tagName
     * @param level
     * @return ConfigRpptを継承しているクラス
     */
    public static ConfigIF createConfig(String tagName, int level, ConfigIF conf) {
        Class<? extends ConfigIF> child = null;
        if (StoryConfig.WORLD_TAG.equals(tagName) && level == 0) {
            return new World();
        } else if (StoryConfig.NATURE_TAG.equals(tagName) && level == 1) {
            // 自然
            World world = (World) conf;
            world.setNature(new Nature());
            return world.getNature();
        } else if (StoryConfig.CLIMATE_TAG.equals(tagName) && level == 2) {
            // 気候
            printData("Climeate: ", tagName, String.valueOf(level));
            Nature nature = (Nature) conf;
            nature.getClimateList().add(new Climate());
        } else if (StoryConfig.COUNTRY_LIST_TAG.equals(tagName) && level == 1) {
            // 国リスト
            printData("Countries: ", tagName, String.valueOf(level));
            World world = (World) conf;
            world.setCountries(new ArrayList<>());
        } else if (StoryConfig.COUNTRY_TAG.equals(tagName) && level == 2) {
            // 国
            printData("Country: ", tagName, String.valueOf(level));
            World world = (World) conf;
            List<Country> list = world.getCountries();
            list.add(new Country());
            return list.get(list.size() - 1);
        } else if (StoryConfig.CREATURE_LIST_TAG.equals(tagName) && level == 2) {
            // 生物リスト
            printData("Creatures: ", tagName, String.valueOf(level));
            World world = (World) conf;
            world.setCreatures(new ArrayList<>());
        } else if (StoryConfig.CREATURE_TAG.equals(tagName) && level == 1) {
            printData("Creature: ", tagName, String.valueOf(level));
            World world = (World) conf;
            List<Creature> list = world.getCreatures();
            list.add(new Creature());
            return list.get(list.size() - 1);
        } else if (StoryConfig.REGION_LIST_TAG.equals(tagName) && level == 1) {
            printData("Regions: ", tagName, String.valueOf(level));
            World world = (World) conf;
            world.setRegions(new ArrayList<>());
        } else if (StoryConfig.REGION_TAG.equals(tagName) && level == 2) {
            printData("Region: ", tagName, String.valueOf(level));
            World world = (World) conf;
            List<Region> list = world.getRegions();
            list.add(new Region());
            return list.get(list.size() - 1);
        } else if (StoryConfig.CIVILIZATION_LIST_TAG.equals(tagName) && level == 1) {
            printData("Civilizations: ", tagName, String.valueOf(level));
            World world = (World) conf;
            world.setCivilizations(new ArrayList<>());
        } else if (StoryConfig.CIVILIZATION_TAG.equals(tagName) && level == 2) {
            printData("Civilization: ", tagName, String.valueOf(level));
            World world = (World) conf;
            List<Civilization> list = world.getCivilizations();
            list.add(new Civilization());
            return list.get(list.size() - 1);
        } else if (StoryConfig.CULTURE_LIST_TAG.equals(tagName) && level == 1) {
            printData("Cultures: ", tagName, String.valueOf(level));
            World world = (World) conf;
            world.setCultures(new ArrayList<>());
        } else if (StoryConfig.CULTURE_TAG.equals(tagName) && level == 2) {
            printData("Culture: ", tagName, String.valueOf(level));
            World world = (World) conf;
            List<Culture> list = world.getCultures();
            list.add(new Culture());
            return list.get(list.size() - 1);
        }
        return conf;
    }
    
        private static void setXmlTagValue(Node node, ConfigIF conf) {
        String xmlTagName = node.getNodeName();
        String xmlTagValue = node.getFirstChild().getTextContent().trim();
        if (xmlTagValue != null && "".equals(xmlTagValue) == false) {
            searchField(xmlTagName, xmlTagValue, conf);
        }
    }
    private static void printVlue(ConfigIF confClass, int level, Node node)
    {
        if (level < 1) {
            // タグ改装が１より下の時は何もしない
            return ;
        }
        // 属性の有無
        if (node.hasAttributes()) {
            NamedNodeMap map = node.getAttributes();
            int len = map.getLength();
            for (int i = 0; i < len; i++) {
                Node n = map.item(i);
                if (isDebug) System.out.println(" 属性: " + n.getTextContent());
            }
        }
        // 子タグがあるとき
        if (node.hasChildNodes() && level >= 2)  {
            String xmlTagName = node.getNodeName();
            String xmlTagValue = node.getFirstChild().getTextContent().trim();

            Field targetField = null;
//            if (isDebug) System.out.println("フィールド取得 " + xmlTagName + " : " );
            // フィールドがあるか
            //targetField = searchField(xmlTagName, xmlTagValue, confClass);

            if (!"".equals(xmlTagValue)) {

                if (isDebug) {
                    System.out.println("xmlTagName: " + xmlTagName + SEP
                            + "xmlTagValue: " + xmlTagValue + SEP
                            + "ConfigIF: " + confClass.getClass().getName());
                }
                try {
                    handleClassOrStr(confClass, xmlTagName, xmlTagValue);
                    if (targetField != null ) {

                        targetField.setAccessible(true);
                        //targetField.set(confClass, xmlTagValue);
                        targetField.setAccessible(false);
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    System.exit(-1);
                } catch (RpgException re) {
                    re.printStackTrace();
                    System.exit(-1);
                }

            } else {
                // タグのみの場合

            }
        }
    }

    private static Field searchField(String fieldName, String tagValue, ConfigIF confClass)
    {
        System.out.println("*** searchField " + SEP + fieldName + "  : " + tagValue + "***");
        Field f = null;
        try {
            if (StoryConfig.ID_TAG.equals(fieldName) || StoryConfig.NAME_TAG.equals(fieldName)
                    || StoryConfig.DESCRIPTION_TAG.equals(fieldName)) {
                f = confClass.getClass().getSuperclass().getDeclaredField(fieldName);
            } else {
                f = confClass.getClass().getDeclaredField(fieldName);
            }
            f.setAccessible(true);
            Class<?> clz = f.getType();
            if (clz == String.class) {
                System.out.println("1.GetField=>" + clz.getSimpleName());
                f.set(confClass, tagValue);
            } else if (clz == List.class) {
                System.out.println("2.GetField=>List: " + clz.getSimpleName());
            } else if (clz == World.class) {
                System.out.println("3.GetField=>World: " + clz.getSimpleName());
            } else if (clz == Country.class) {
                System.out.println("4.GetField=>Country" + clz.getSimpleName());
            } else if (clz == Nature.class) {
                System.out.println("5.GetField=>Nature: " + clz.getSimpleName());
            } else if (clz == Region.class) {
                System.out.println("6.GetField=>Region: " + clz.getSimpleName());
            } else if (clz == Civilization.class) {
                System.out.println("7.GetField=>Civilization: " + clz.getSimpleName());
            } else if (clz == Culture.class) {
                System.out.println("8.GetField=>Culture: " + clz.getSimpleName());
            }

        } catch (NoSuchFieldException ne) {
            if (isDebug) System.out.println("NoSuchFieldException : " + fieldName);
        } catch (SecurityException e) {
            if (isDebug) System.out.println("SecurityException: " + fieldName);
            e.printStackTrace();
            System.exit(-1);
        } catch (IllegalArgumentException  e) {
            e.printStackTrace();
            System.exit(-1);
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            if (f != null) f.setAccessible(false);
        }
        return f;
    }

    private static void printData(String title, String tagName, String tagVlue) {
        System.out.println(title + " TagName: " + tagName + " TagValue: " + tagVlue);
    }
    private static void handleClassOrStr(ConfigIF conf, String tagName, String tagValue) throws RpgException {
        //printData(conf.getClass().getSimpleName() + ": ", tagName, tagValue);
        ConfigIF child = null;
        StoryConfig parent = null;
        World world = null;
        List<Civilization> civilizations = null;
        switch (tagName) {
            case StoryConfig.ID_TAG:
            case StoryConfig.NAME_TAG:
            case StoryConfig.DESCRIPTION_TAG:
            case StoryConfig.IMG_TAG:
                parent = (StoryConfig) conf;
                parent.setId(tagName);
                break;
            case StoryConfig.COUNTRY_LIST_TAG:
                // 何もしない
                break;
            case StoryConfig.COUNTRY_TAG:
                Country country = (Country) conf;
//                Country country = new Country();
//                world = (World) conf;
//                world.getCountries().add(country);
                break;
            case StoryConfig.NATURE_TAG:
                Nature nature = (Nature) conf;
//                world = (World) conf;
//                world.setNature(new Nature());
                break;
            case StoryConfig.CLIMATE_TAG:
                printData("Climeate: " + conf.getClass().getSimpleName() + " ", tagName, tagValue);
                Climate climete = (Climate) conf;
                climete.setId(tagValue);
                break;
            case StoryConfig.CREATURE_LIST_TAG:
                // 何もしない
                break;
            case StoryConfig.CREATURE_TAG:
                printData("Creature: " + conf.getClass().getSimpleName() + " ", tagName, tagValue);
                List<Creature> creatureList = ((World) conf).getCreatures();
                world.getCreatures().add(new Creature());
                break;
            case StoryConfig.CIVILIZATION_LIST_TAG:
                // 何もしない
                break;
            case StoryConfig.CIVILIZATION_TAG:
                printData("Civilization: ", tagName, tagValue);
                Civilization civilization = (Civilization) conf;
                break;
            case StoryConfig.CHARACTER_TAG:
                printData("Character: ", tagName, tagValue);
                Civilization civilization1 = (Civilization) conf;
                civilization1.setCharacter(tagValue);
                break;
            case StoryConfig.ARTS_TAG:
                printData("Arts: ", tagName, tagValue);
                Civilization civilization2 = (Civilization) conf;
                civilization2.setArts(tagValue);
                break;
            case StoryConfig.STRUCTURE_TAG:
                printData("Structure: ", tagName, tagValue);
                Civilization civilization3 = (Civilization) conf;
                civilization3.setStructures(tagValue);
                break;
            case StoryConfig.TECHNOLOGY_TAG:
                printData("Technology: ", tagName, tagValue);
                Civilization civilization4 = (Civilization) conf;
                civilization4.setTechnology(tagValue);
                break;
            case StoryConfig.REGION_LIST_TAG:
                // 何もしない
                break;
            case StoryConfig.REGION_TAG:
                printData("Region: ", tagName, tagValue);
                world = (World) conf;
                world.getRegions().add(new Region());
                break;
            case StoryConfig.CULTURE_LIST_TAG:
                // 何もしない
                break;
            case StoryConfig.CULTURE_TAG:
                printData("Culture: ", tagName, tagValue);
                world = (World) conf;
                world.getCultures().add(new Culture());
                break;
            default:
                Field field = searchField(tagName, tagValue, conf);
                if (field == null) throw new RpgException("対応するタグがありません: " + tagName + SEP + conf.getClass().getName());
                try {
                    field.setAccessible(true);
                    field.set(conf, tagValue);
                    field.setAccessible(false);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
        }

    }
    private static Object createInstance(String className) throws RpgException {
        Object res = null;
        switch (className) {
            case "List":
                res = new ArrayList<>();
                break;
            case "Nature":
                res = new Nature();
                break;
            default:
                throw new RpgException("対象のクラスがありません。" + className);
        }
        return res;
    }

    private static Object createInstance(String className, String value, ConfigIF confClass) throws RpgException {
        Object res = null;
        if (value == null || "".equals(value)) {
            return createInstance(className);
        }
        switch (className) {
            case "List":
                res = new ArrayList<>();
                break;
            case "Nature":
                Nature n = new Nature();
                n.setPath(value);
                res = n;
                break;
            default:
                throw new RpgException("対象のクラスがありません。" + className);
        }
        return res;
    }
    
    /**
     * Worldタグのチェックを行い、Worldクラスを生成する。
     * @param tagName <world>タグ
     * @param level　階層レベルは１
     */
    private static World createLevel1(String tagName, int level) throws RpgException {
        if (level != 1) {
            throw new RpgException("worldタグは<class>タグの直下においてください。階層=" + level);
        }
        if (StoryConfig.WORLD_TAG.equals(tagName) == false) {
            throw new RpgException("worldタグの名前が間違っています。" + tagName);
        }
        return new World();
    }
    
    /**
     * World以下に付随するクラスの生成を行う。
     * 
     * @param tagName タグ名
     * @param 階層レベル2以上
     */
    public static StoryConfig createLevel2(String tagName, int level) throws RpgException {
        StoryConfig conf = null;
        if (level <= 1) {
            new RpgException("階層レベルが不適切です。" + level);
        }
        
        if (StoryConfig.COUNTRY_TAG.equals(tagName)) {
            
        }
        return conf;
    }
    /**
     * 世界の設定ファイルを読み込み、Worldクラスを生成する。
     * Worlds.xmlには複数の世界が定義され散る可能性があるので、List<World>になっている。
     *
     * @param directory XMLファイルがあるしディレクトリ
     */
    public static List<World> loadWorlds(String directory) {
        List<World> worldList = new ArrayList<>();
        // 空オブジェクトのクラスを取得する
        ConfigIF worldClass = null;
        try {
            Document doc = loadDocumentBuilder(directory, StoryConfig.WORLD_XML);
            doc.normalizeDocument();
            if (isDebug) System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

                NodeList list = doc.getChildNodes();
                int len = list.getLength();
                int level = 0;
                for (int i = 0; i < len; i++) {
                    Node node = list.item(i);
                    String s = node.getNodeName();
                    if (s.startsWith("#") == false) {
                        if (isDebug) {
                            System.out.println("Lv: " + level);
                            System.out.println("名前: " + s);
                        }
                        if ("class".equals(s) == false) {
                            System.out.println("XMLの初めは<class>タグで開始してください。" + s);
                            System.exit(-1);
                        }
                        // 設定クラスのインスタンス
                        worldClass = createConfig(StoryConfig.WORLD_XML);
                        worldList.add((World) worldClass);
                    }
                    if (node.hasChildNodes()) {
                        level = level + 1;
                        childNodes(worldClass, node.getChildNodes(), level);
                    }
                }

        } catch (RpgException e) {
            e.printStackTrace();
        } catch (ClassCastException ce) {
            ce.printStackTrace();
        }
        return worldList;
    }
    
    private static void childNodes(ConfigIF confClass, NodeList list, int level) throws ClassCastException 
    {
        int len = list.getLength();
        for (int i = 0; i < len; i++) {
            Node node = list.item(i);
            String s = node.getNodeName();
            if (s.startsWith("#") == false) {
                if (isDebug) {
                    System.out.print("*** Lv: " + level);
                    System.out.println(" TAG名前: " + s);
                }
                printVlue(confClass, level, node);
            }
            if (node.hasChildNodes()) {
                level = level + 1;
                //if(isDebug) System.out.print(level + " : " + node.getNodeName());
                childNodes(confClass, node.getChildNodes(), level);
                level = level - 1;
                System.out.println();
            }
        }
    }
        
    private static Field searchField(String fieldName, ConfigIF confClass)
    {
        System.out.println("*** searchField ***");
        Field f = null;
        try {
            f = confClass.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            Object clz = f.get(confClass);
            f.setAccessible(false);
            System.out.println("GetField=>" + clz.toString());
            if (clz instanceof List) {
                System.out.println("GetField=>List: " + clz.toString());
            } else if (confClass instanceof World) {
                System.out.println("GetField=>World: " + clz.toString());
            } else if (confClass instanceof Country) {
                System.out.println("GetField=>Country" + clz.toString());
            } else if (confClass instanceof Nature) {
                System.out.println("GetField=>Nature: " + clz.toString());
            } else if (confClass instanceof Region) {
                System.out.println("GetField=>Region: " + clz.toString());
            } else if (confClass instanceof Civilization) {
                System.out.println("GetField=>Civilization: " + clz.toString());
            } else if (confClass instanceof Culture) {
                System.out.println("GetField=>Culture: " + clz.toString());
            }

        } catch (NoSuchFieldException ne) {
            if (isDebug) System.out.println("NoSuchFieldException : " + fieldName);
        } catch (SecurityException e) {
            if (isDebug) System.out.println("SecurityException: " + fieldName);
            e.printStackTrace();
            System.exit(-1);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return f;
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
