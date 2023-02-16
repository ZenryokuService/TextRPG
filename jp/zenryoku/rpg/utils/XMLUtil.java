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

/**
 * クラス XMLUtil の注釈をここに書きます.
 * XMLファイルの読込からオブジェクト生成を行います。
 * 
 * @author (あなたの名前)
 * @version (バージョン番号もしくは日付)
 */
public class XMLUtil
{
    private static final boolean isDebug = false;

    private static NodeList getTagNode(Element e, String tagName) throws Exception {
        Element ele = null;
        NodeList nodeList = e.getElementsByTagName(tagName);
        if (nodeList.getLength() == 0) {
            throw new Exception("Node(" + e.getTagName() + ")に" + tagName + "は存在しません。");
        }
        return nodeList;
    }

    /**
     * XMLドキュメント(ファイル)を読み込む。
     * @param directory ファイルの配置しているディレクトリ
     * @param fileName ファイル名
     * @return Documentオブジェクト
     * @throws RpgException ファイルの読み込みエラー
     */
    private static Document loadDocumentBuilder(String directory, String fileName) throws Exception {
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
        }
        return doc;
    }
    
    public static Object readSettingXml(String path) {
      Object o = null;
      try {
        BufferedInputStream buf = FileReaderUtil.newBufferedInputStream(path);
        XMLDecoder decoder = new XMLDecoder(buf);

        o = decoder.readObject();
      } catch (FileNotFoundException e) {
          e.printStackTrace();
          System.exit(-1);
      }
        return o;
    }
}
