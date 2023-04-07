package jp.zenryoku.rpg.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import jp.zenryoku.rpg.JavaFXInitializer;
import jp.zenryoku.rpg.exception.RpgException;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AudioUtil {
    private MediaPlayer player;

    // このクラスを使用するときにはJavaFXの環境をロードする
    static {
        JavaFXInitializer.launch();
    }
    public AudioUtil(String path) throws RpgException {
        if (path == null || "".equals(path)) {
            return;
        }
        try {
            URL url = Paths.get(path).toFile().toURI().toURL();
            Media media = new Media(url.toExternalForm());
            player = new MediaPlayer(media);
        } catch (MalformedURLException e) {
            throw new RpgException(e.getMessage());
        }

    }

    public void play() throws RpgException {
        player.play();
    }
    public void stop() throws RpgException {
        player.stop();
    }

}
