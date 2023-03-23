package jp.zenryoku.rpg;

import jp.zenryoku.rpg.utils.XMLUtil;
import org.junit.Test;

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
    public void testExportMonsterTypes() {
        XMLUtil.exportMonsterTypeJaxb("config/bak", "bkMonsterTypes.xml");
    }

}
