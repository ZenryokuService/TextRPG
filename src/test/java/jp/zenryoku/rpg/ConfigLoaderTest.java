package jp.zenryoku.rpg;

import jp.zenryoku.rpg.data.param.Params;
import jp.zenryoku.rpg.exception.RpgException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigLoaderTest {
    @Test
    public void testiIKey() {
        try  {
            assertTrue(ConfigLoader.getInstance().isCurrentMoney("ニギ", false));
            assertFalse(ConfigLoader.getInstance().isCurrentMoney("dokus", true));
        } catch (RpgException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testConveret() throws RpgException {
        String f = ConfigLoader.getInstance().convertMoneyStr("NIG + 100");
        assertEquals("0 + 100", f);
    }

    @Test
    public void testStatusMap() throws RpgException {
        Params p = ConfigLoader.getInstance().getParamsMap().get("HP");
        assertNotNull(p);
        assertEquals("HP", p.getKey());

        Params p1 = ConfigLoader.getInstance().getParamsMap().get("BPK");
        assertNotNull(p1);
        assertEquals("BPK", p1.getKey());

    }

}
