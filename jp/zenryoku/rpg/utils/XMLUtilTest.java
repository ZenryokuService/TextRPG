package jp.zenryoku.rpg.utils;

import jp.zenryoku.rpg.data.config.*;
import jp.zenryoku.rpg.character.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

/**
 * テストクラス XMLUtilTest.
 *
 * @author (あなたの名前)
 * @version (バージョン番号もしくは日付)
 */
public class XMLUtilTest
{
    private static final String SEP = System.lineSeparator();
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
    
    //@Test
    public void test01() {
        XMLUtil.createConfig("Config.xml");
    }

    @Test
    public void testExportConfig() {
        try {
            XMLUtil.exportConfigJaxb("config", "Config.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
        @Test
    public void testLoadConfig() {
        try {
            Config conf = XMLUtil.loadConfig("config", "Config.xml");
            assertNotNull(conf);
            List<String> views = conf.getViews();
            assertEquals("name", views.get(0));
            assertEquals("HP", views.get(1));
            assertEquals("MP", views.get(2));
            assertEquals("LV", views.get(3));
            
        } catch (Exception e) {
            e.printStackTrace();
            
        }
    }

    @Test
    public void testLoadWorlds() {
        String[] filesA = new String[] {"Worlds.xml"};

        for (String files : filesA) {
            try {
                List<World> confList = XMLUtil.loadWorldJaxb("config", "bkWorlds.xml");
                assertTrue(confList.size() > 0);
                World world = confList.get(0);
                assertNotNull(world);
                assertEquals("テスト用の世界",world.getName());
                
                World world1 = confList.get(1);
                assertNotNull(world1);
                assertEquals("上の世界",world1.getName());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testExportWorlds() {
        String[] filesA = new String[] {"Worlds.xml"};

        try {
            List<World> confList = XMLUtil.exportWorldJaxb("config", "akWorlds.xml");
            if (confList == null) {
                return;
            }
            assertTrue(confList.size() > 0);
            for (World world : confList) {
                assertNotNull(world);
                assertEquals("ちきゅう",world.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testExportPlayer() {

        try {
            XMLUtil.exportPlayerJaxb("config", "bk_Players.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testExportMonster() {

        try {
            XMLUtil.exportMonsterJaxb("config", "bk_Monsters.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadPlayers() {
        try {
            List<Player> list = XMLUtil.loadPlayer("config/Players.xml");
            assertNotEquals(0, list.size());
            
        } catch (Exception e) {
            e.printStackTrace();
            
        }
    }
    
    @Test
    public void testLoadMonsters() {
        try {
            List<Monster> list = XMLUtil.loadMonsters("config/Monsters.xml");
            assertNotEquals(0, list.size());
            
        } catch (Exception e) {
            e.printStackTrace();
            
        }
    }
    
    @Test
    public void testExportStory() {
        try {
            XMLUtil.exportStoryJaxb("config", "Story_000.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadStory() throws Exception {
        Scene st = XMLUtil.loadStory("./config/Story_000_bk.xml");
        assertNotNull(st);
        assertEquals("First", st.getId());
    }
    
    private void printWorld(World world) {
        print("World#ID: ", world.getId());
        print("World#Name: ", world.getName());
        print("World#Description: ", world.getDescription());
        List<Country> cList = world.getCountries();
        for(Country c : cList) {
            print("Country#ID: ", c.getId());
            print("Country#Name: ", c.getName());
            print("Country#Description: ", c.getDescription());
        }
        List<Region> rList = world.getRegions();
        for(Region r : rList) {
            print("Region#ID: ", r.getId());
            print("Region#Name: ", r.getName());
            print("Region#Description: ", r.getDescription());
        }
        List<Creature> creList = world.getCreatures();
        for(Creature cre : creList) {
            print("Creature#ID: ", cre.getId());
            print("Creature#Name: ", cre.getName());
            print("Creature#Description: ", cre.getDescription());

        }
        List<Civilization> ciList = world.getCivilizations();
        for(Civilization ci : ciList) {
            print("Civilization#ID: ", ci.getId());
            print("Civilization#Name: ", ci.getName());
            print("Civilization#Description: ", ci.getDescription());
        }
        List<Culture> cuList = world.getCultures();
        for(Culture cu : cuList) {
            print("Culture#ID: ", cu.getId());
            print("Culture#Name: ", cu.getName());
            print("Culture#Description: ", cu.getDescription());
            print("Culture#Norn: ", cu.getNorm());
            print("Culture#Habit: ", cu.getHabit());
            print("Culture#Values: ", cu.getValues());
            print("Culture#Life_style: ", cu.getLife_style());
            print("Culture#Organization: ", cu.getOrganization());
            print("Culture#Social_structure: ", cu.getSocial_structure());
            print("Culture#Social_system: ", cu.getSocial_system());
            print("Culture#Way_of_thinking: ", cu.getWay_of_thinking());
        }
        Nature n = world.getNature();
        List<Climate> clList = n.getClimateList();
        for(Climate cl : clList) {
            print("Climate#ID: ", cl.getId());
            print("Climate#Name: ", cl.getName());
            print("Climate#Description: ", cl.getDescription());
        }
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
    
    private void print(String title, String value) {
        System.out.println(title + value);
    }
}
