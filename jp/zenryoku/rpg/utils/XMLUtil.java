package jp.zenryoku.rpg.utils;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.beans.XMLDecoder;
import jp.zenryoku.rpg.exception.RpgException;
import jp.zenryoku.rpg.data.config.ConfigIF;
import jp.zenryoku.rpg.data.config.Config;
import jp.zenryoku.rpg.data.config.StoryConfig;
import jp.zenryoku.rpg.data.config.World;
import jp.zenryoku.rpg.data.config.Nature;
import jp.zenryoku.rpg.data.config.Creature;
import jp.zenryoku.rpg.data.config.Region;
import jp.zenryoku.rpg.data.config.Effect;
import jp.zenryoku.rpg.data.config.STM;
import jp.zenryoku.rpg.data.config.Command;
import jp.zenryoku.rpg.data.config.Job;
import jp.zenryoku.rpg.data.config.MonsterType;


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
            case StoryConfig.CONFIG_XML:
                conf = new Config();
                break;
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
     * 設定ファイルを読み込み、設定クラスを生成する。
     *
     * @param directory
     * @param fileName
     */
    public static void loadWorlds(String directory, String fileName) {
        // 空オブジェクトのクラスを取得する
        ConfigIF instance = null;
        try {
            Document doc = loadDocumentBuilder(directory, fileName);
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
                        instance = createConfig(fileName);
                    }
                    if (node.hasChildNodes()) {
                        level = level + 1;
                        childNodes(node.getChildNodes(), level);
                    }
                }

        } catch (RpgException e) {
            e.printStackTrace();
        }        
    }
    
    private static void childNodes(NodeList list, int level) 
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
                printVlue(level, node);
            }
            if (node.hasChildNodes()) {
                level = level + 1;
                //if(isDebug) System.out.print(level + " : " + node.getNodeName());
                childNodes(node.getChildNodes(), level);
                level = level - 1;
                System.out.println();
            }
        }
    }

    private static void printVlue(int level, Node node) 
    {

        //System.out.print("Level: " + level);
        if (node.hasAttributes()) {
            NamedNodeMap map = node.getAttributes();
            int len = map.getLength();
            for (int i = 0; i < len; i++) {
                Node n = map.item(i);
                if (isDebug) System.out.println(" 属性: " + n.getTextContent());
            }
        }
        if (node.hasChildNodes() && level >= 2)  {
            if (!"".equals(node.getFirstChild().getTextContent().trim())) {
                if (isDebug) System.out.println("Node : " + node.getFirstChild().getTextContent());
            }
        }
    }

}
