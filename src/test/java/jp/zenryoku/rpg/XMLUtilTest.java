package jp.zenryoku.rpg;

import jp.zenryoku.rpg.exception.RpgException;
import jp.zenryoku.rpg.utils.XMLUtil;
import org.junit.Test;

import static org.junit.Assert.fail;

public class XMLUtilTest {
    @Test
    public void testExportConfig() {
        XMLUtil.exportConfigJaxb("config/bak", "bkConfig.xml");
    }

    @Test
    public void testExportItems() {
        XMLUtil.exportItemsJaxb("config/bak", "bkItems.xml");
    }

    @Test
    public void testExportJob() {
        XMLUtil.exportJobJaxb("config/bak", "bkJobs.xml");
    }

    @Test
    public void testExportCommand() {
        XMLUtil.exportCommandJaxb("config/bak", "bkCommands.xml");
    }

    @Test
    public void testLoadCommand() {
        try {
            XMLUtil.loadCommands("config/bak", "bkCommands.xml");
        } catch (RpgException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testExportMonsterTypes() {
        XMLUtil.exportMonsterTypeJaxb("config/bak", "bkMonsterTypes.xml");
    }

    @Test
    public void testExportPlayers() {
        XMLUtil.exportPlayerJaxb("config/bak", "bkPlayer.xml");
    }
}
