package jp.zenryoku.rpg;

import jp.zenryoku.rpg.character.Player;
import jp.zenryoku.rpg.data.config.Scene;
import jp.zenryoku.rpg.exception.RpgException;
import jp.zenryoku.rpg.views.InputSelector;

import javax.swing.*;
import java.awt.*;
import java.util.List;
/**
 * テキストRPGのメインクラス。
 */
public class TextRpgMain extends JFrame {
    private ConfigLoader config;
    private String TITLE_PANEL = "タイトル";
    private RpgTextArea textArea;

    /**
     * テキストRPGの実行処理。
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            TextRpgMain main = new TextRpgMain();
        } catch (RpgException e) {

        }
    }

    /**
     * コンストラクタ。
     * 1. 設定ファイルを読み込む
     * 2. 初期画面を表示
     *
     * @throws RpgException
     */
    public TextRpgMain() throws RpgException {
        // 設定XMLのよみこみ
        config = ConfigLoader.getInstance();
        // 初期画面の表示
        initView();
    }

    /**
     * 起動しているPCの、ディスプレイ・サイズを取得する
     * @return Dimension ディスプレイ・サイズ
     */
    public Dimension displaySise() {
        return Toolkit.getDefaultToolkit().getScreenSize();

    }

    public RpgTextArea getTextArea() {
        return this.textArea;
    }

    public void setPlayer(Player player) throws RpgException {
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

    public void initView() {
        Dimension windowSize = displaySise();
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

        Scene story = null;
        story = config.getScenes().get(0);
        // 1. 文章の表示
        textArea.setText(story.getStory());
        // シーンタイプを取得
        //story.getSceneType();
        try {
            // . 入力を受ける
            InputSelector pop = new InputSelector(story,this);
            pop.show(this, xPos - 30, yPos + 220);
        } catch (RpgException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "alert", e.getMessage()
                    , JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
