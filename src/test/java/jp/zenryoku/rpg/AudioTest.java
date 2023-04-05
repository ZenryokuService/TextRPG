package jp.zenryoku.rpg;

import jp.zenryoku.rpg.exception.RpgException;
import jp.zenryoku.rpg.utils.AudioUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.fail;

public class AudioTest {
    private AudioUtil target;

    @Before
    public void initAudio() {
        try {
            target = new AudioUtil("game/stories/html/title.mp3");
        } catch (RpgException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testAudio() {
        try {

            target.play();
            Thread.sleep(2000);
            target.stop();
        } catch (RpgException | InterruptedException e) {
            fail(e.getMessage());
        }
    }
}
