package jp.zenryoku.rpg;

import lombok.Data;
import jp.zenryoku.rpg.character.*;
import jp.zenryoku.rpg.exception.RpgException;
import jp.zenryoku.rpg.data.config.Scene;
import jp.zenryoku.rpg.views.InputSelector;
import java.awt.Component;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * クラス TextRPGMain の注釈をここに書きます.
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
@Data
public class TextRPGMain extends JFrame
{
    public static final String TITLE_PANEL = "titlePanel";
    private static ConfigGenerator config;
    private static boolean playGame;
    private Player player;
    private RpgTextArea textArea;
    
    public static void main(String[] args) {
        TextRPGMain main = new TextRPGMain();
        try {
            config = ConfigGenerator.getInstance();
            main.run("Text RPG");
        } catch(RpgException e) {
            e.printStackTrace();
        }
        
    }
    
    private void TextRPGMain() {
    }

    public void setPlayer(Player player) throws RpgException {
        this.player = player;
        List<String> views = config.getConf().getViews();
        JPanel titlePanel = (JPanel) selectComponent(TITLE_PANEL);
        JPanel ppp = new JPanel();
        ppp.setLayout(new BoxLayout(ppp, BoxLayout.Y_AXIS));
        ppp.add(new JLabel(player.getName()));
        
        for (String key : views) {
            //System.out.println("key: " + key);
            ppp.add(new JLabel(key + ": " + player.getParams(key).getValue()));
        }
        
        titlePanel.remove(0);
        titlePanel.add(ppp);
    }
    
    public Component selectComponent(String name) throws RpgException {
        Component resComponent = null;
        Component[] components = getContentPane().getComponents();
        for (Component com : components) {
            if (name.equals(com.getName())) {
                resComponent = com;
            }
        }
        if (resComponent == null) throw new RpgException("名前が不適切です: " + name);
        return resComponent;
    }
    public void run(String title) throws RpgException {
        setTitle(title);
        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        int xPos = (int) windowSize.getWidth() / 4;
        int yPos = (int) windowSize.getHeight() / 5;
        setBounds(xPos, yPos, xPos * 2, yPos * 3);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TitleLabel titleLabel = new TitleLabel("Text RPG", windowSize);
        textArea = new RpgTextArea(windowSize);

        JPanel titlePanel = new JPanel();
        titlePanel.setName(TITLE_PANEL);
        titlePanel.add(titleLabel);
        
        JPanel textPanel = new JPanel();
        textPanel.add(textArea);

        Container contentPane = getContentPane();
        contentPane.add(titlePanel, BorderLayout.NORTH);
        contentPane.add(textPanel, BorderLayout.CENTER);

        setVisible(true);
        
        // ゲーム中
        playGame = true;

        Scene story = null;
        // はじめのシーン番号
        int sceneNo = 0;
        story = config.getScenes().get(sceneNo);
        // 1. 文章の表示
        textArea.setText(story.getStory());
        // シーンタイプを取得
        //story.getSceneType();
        try {
            // . 入力を受ける
            InputSelector pop = new InputSelector(story, textArea, this);
            pop.show(this, xPos - 30, yPos + 220);
        } catch (RpgException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "alert", e.getMessage()
                , JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
