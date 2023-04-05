package jp.zenryoku.rpg;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

public class JavaFXInitializer {
    public static void launch() {
        Platform.startup(() ->{
            new JFXPanel();
        });
    }
}