package jp.zenryoku.rpg.character;

import jp.zenryoku.rpg.data.param.Item;
import jp.zenryoku.rpg.data.param.Params;
import org.junit.Before;
import org.junit.Test;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlayerTest {
    private Player target;
    @Before
    public void initTest() {
        target = new Player("サンプル");
        Map<String, Params> status = new HashMap<>();
        status.put("BPK", new Params("BPK", "アイテム保持量", 8));
        target.setStatus(status);
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item());
        itemList.add(new Item());
        itemList.add(new Item());
        itemList.add(new Item());
        target.setItems(itemList);
    }

    @Test
    public void testItemSize() {
        assertTrue(target.canHasMore());
        List<Item> items = target.getItems();
        items.add(new Item());
        items.add(new Item());
        items.add(new Item());
        items.add(new Item());
        assertFalse(target.canHasMore());
    }
}
