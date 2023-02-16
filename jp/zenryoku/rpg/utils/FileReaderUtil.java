package jp.zenryoku.rpg.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.beans.XMLDecoder;

/**
 * クラス FileReaderUtil の注釈をここに書きます.
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
public class FileReaderUtil
{
  private static final String SEP = System.lineSeparator();
  private static final String SPACER = "                       "; 
  
  /**
   * pathで指定したファイルを読込したStringBuilderを返却する。
   * 
   * @param path ファイルへのパス
   * @return StringBuilder
   */
  public static StringBuilder readFile(String path) {
    StringBuilder build = new StringBuilder();
    BufferedReader buf = newBufferedReader(path);
    
    String line = null;
    try {
        while( (line = buf.readLine()) != null) {
            build.append(line + SEP);
        }
    } catch (IOException ie) {
        ie.printStackTrace();
        System.exit(-1);
    }
    return build;
  }

    /**
   * pathで指定したファイルを読込したStringBuilderを返却する。
   * 
   * @param path ファイルへのパス
   * @param isTitle 中央寄せフラグ
   * @return StringBuilder
   */
  public static StringBuilder readFile(String path, boolean isTitle) {
    StringBuilder build = new StringBuilder();
    BufferedReader buf = newBufferedReader(path);
    
    String line = null;
    try {
        while( (line = buf.readLine()) != null) {
            if (isTitle) {
                build.append(SPACER + line + SEP);
            } else {
                build.append(line + SEP);
            }
        }
    } catch (IOException ie) {
        ie.printStackTrace();
        System.exit(-1);
    }
    return build;
  }
  
  /**
   * 指定したファイルを、読み込むBufferedReaderを生成する。
   * @param path
   * @return BufferedReader
   */
  public static BufferedReader newBufferedReader(String path) {
      Path p = Paths.get(path);
      BufferedReader buf = null;
      try {
          buf = Files.newBufferedReader(p);
      } catch (IOException e) {
          e.printStackTrace();
          System.exit(-1);
      }
      return buf;
  }
  
    /**
   * 指定したファイルを、読み込むBufferedReaderを生成する。
   * @param path
   * @return BufferedReader
   */
  public static BufferedInputStream newBufferedInputStream(String path) throws FileNotFoundException {
      Path p = Paths.get(path);
      BufferedInputStream buf =  new BufferedInputStream(
                                      new FileInputStream(path));
      return buf;
  }
}
