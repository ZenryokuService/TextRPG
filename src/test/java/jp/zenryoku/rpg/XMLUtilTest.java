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
        XMLUtil.exportItemsJaxb("config/", "Items.xml");
    }
}
