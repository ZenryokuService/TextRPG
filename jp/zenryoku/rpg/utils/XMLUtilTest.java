package jp.zenryoku.rpg.utils;



import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * テストクラス XMLUtilTest.
 *
 * @author (あなたの名前)
 * @version (バージョン番号もしくは日付)
 */
public class XMLUtilTest
{
    /**
     * テストクラス XMLUtilTest のためのデフォルトのコンストラクタ
     */
    public XMLUtilTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
    }

    
    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
    }
    
    @Test
    public void test01() {
        XMLUtil.createConfig("Config.xml");
    }
    
        @Test
    public void testLoadObject() {
        String[] filesA = new String[] {"Worlds.xml", "Nature.xml!", "Regions.xml", "Creatures.xml"};

        for (String files : filesA) {
            try {
                XMLUtil.loadWorlds("config", files);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
