package jp.zenryoku.rpg;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BasicTest {
    @Test
    public void testString() {
        assertEquals("   123", String.format("%6s", "123"));
        assertEquals("123   ", String.format("%-6s", "123"));
    }
}
