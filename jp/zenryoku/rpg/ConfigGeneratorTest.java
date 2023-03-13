package jp.zenryoku.rpg;

import jp.zenryoku.rpg.*;
import jp.zenryoku.rpg.exception.*;
import jp.zenryoku.rpg.data.config.*;
import jp.zenryoku.rpg.character.*;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * テストクラス ConfigGeneratorTest.
 *
 * @author (Takunoji)
 * @version (1.0)
 */
public class ConfigGeneratorTest
{
    private ConfigGenerator target;
    
    /**
     * テストクラス ConfigGeneratorTest のためのデフォルトのコンストラクタ
     */
    public ConfigGeneratorTest()
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
        try {
            target = ConfigGenerator.getInstance();
        } catch(RpgException e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
        target = null;
    }
    
    @Test
    public void testLoadStory() throws RpgException {
        Map<Integer, Scene> map = target.loadStories("config/stories");
        assertNotEquals(0, map.size());

        Scene story = map.get(0);
        assertEquals("First", story.getId());
    }
    
    @Test
    public void testLoadPlayers() throws RpgException {
        Map<String, Player> map = target.getPlayers();
        assertNotNull(map.get("シフト＝キー"));
    }
}
