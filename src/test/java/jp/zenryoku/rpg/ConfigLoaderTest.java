package jp.zenryoku.rpg;

import jp.zenryoku.rpg.data.param.Params;
import jp.zenryoku.rpg.exception.RpgException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigLoaderTest {
    @Test
    public void testiIKey() {
        try  {
            assertTrue(ConfigLoader.getInstance().isCurrentMoney("ニギ"));
            assertFalse(ConfigLoader.getInstance().isCurrentMoney("dokus"));
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
}
