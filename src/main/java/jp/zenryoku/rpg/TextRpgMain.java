package jp.zenryoku.rpg;

import jp.zenryoku.rpg.character.Player;
import jp.zenryoku.rpg.data.config.Scene;
import jp.zenryoku.rpg.exception.RpgException;
import jp.zenryoku.rpg.views.InputSelector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
/**
 * テキストRPGのメインクラス。
 */
public class TextRpgMain extends JFrame implements KeyListener, MouseListener {
    public static final String CMD_TEXT = "CMD_TEXT";
    private ConfigLoader config;
    /** 後にプレーヤー表示するパネルの名前 */
    public final String TITLE_PANEL = "titlePanel";
    /** 後に何か表示するパネルの名前 */
    public final String HEADER_PANEL = "header";
    /** 文章表示するパネルの名前 */
    public static final String TEXT_PANEL = "textPanel";

    /** 文字表示のテキストエリア */
    private RpgTextArea textArea;
    /** ポップアップメニュー */
    private InputSelector inputSelector;
    /** コマンド入力用hのパネル */
    private JPanel txt;
    /** コマンド入力テキストフィールド */
    private RpgTextField field;
    /** プレーヤー */
    private Player player;
    /** シーン番号ラベル */
    private JLabel sceneNoLbl;
    /** HTML表示フラグ */
    private boolean viewHtml;



    /**
     * テキストRPGの実行処理。
     *
     * @param args
     */
    public static void main(String[] args) {
        TextRpgMain main = null;

        try {
            if (args.length == 0) {
                main = new TextRpgMain();
            } else if (args.length == 1) {
                main = new TextRpgMain(args[0]);
            }
        } catch (RpgException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(main
                    , e.getMessage()
                    , "初期化エラー"
                    , JOptionPane.ERROR_MESSAGE);
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
        addKeyListener(this);
        setFocusable(true);
        // 初期画面の表示
        initView();
    }

    public TextRpgMain(String path) throws RpgException {
        // 設定XMLのよみこみ
        config = ConfigLoader.getInstance(path);
        addKeyListener(this);
        setFocusable(true);
        // 初期画面の表示
        initView();
    }

    public void setViewHtml(boolean isView) {
        this.viewHtml = isView;
    }

    public boolean isViewHtml() { return viewHtml; }
    public void setSceneNoLbl(String num) {
        sceneNoLbl.setText("SceneNO: " + num);
    }
    /**
     * 起動しているPCの、ディスプレイ・サイズを取得する
     * @return Dimension ディスプレイ・サイズ
     */
    public static Dimension displaySise() {
        return Toolkit.getDefaultToolkit().getScreenSize();

    }

    public RpgTextArea getTextArea() {
        return this.textArea;
    }

    public Player getPlayer() { return this.player; }

    public void setPlayer(Player player) throws RpgException {
        Dimension windowSize = displaySise();
        this.player = player;
        List<String> views = config.getConf().getViews();

        JPanel titlePanel = (JPanel) selectComponent(TITLE_PANEL);
        titlePanel.removeAll();
        titlePanel.add(sceneNoLbl, JPanel.TOP_ALIGNMENT);
        titlePanel.add(Box.createRigidArea(new Dimension(windowSize.width / 6, 20)));

        JPanel ppp = new JPanel();
        ppp.setLayout(new BoxLayout(ppp, BoxLayout.Y_AXIS));
        ppp.add(new JLabel(player.getName()));

        for (String key : views) {
            System.out.println("key: " + key);
            ppp.add(new JLabel(key + ": " + player.getParams(key).getValue()));
        }

        titlePanel.add(ppp);
        titlePanel.add(Box.createRigidArea(new Dimension(windowSize.width / 10, 20)));
        // TODO-[所持金等の配置を修正したい]
        JLabel moneyLbl = new JLabel("所持金: " + player.getMoney());
        moneyLbl.setBackground(Color.YELLOW);
        titlePanel.add(moneyLbl);
        titlePanel.add(Box.createRigidArea(new Dimension(10, 20)));

        JLabel expLbl = new JLabel("経験値: " + player.getExp());
        expLbl.setBackground(Color.cyan);
        titlePanel.add(expLbl);
//        right.add(moeyLbl, BorderLayout.NORTH);
//        right.add(expLbl, BorderLayout.SOUTH);
    }

    public Component selectComponent(String name) throws RpgException {
        Component resComponent = null;
        Component[] components = getContentPane().getComponents();
        for (Component com : components) {
            System.out.println("First: " + com.getName());
            // TODO-[このコードが気に入らない]
            if (com instanceof JPanel && ((JPanel) com).getComponents().length != 0) {
                Component[] lv2Com = ((JPanel) com).getComponents();
                for (Component com2 : lv2Com) {
                    if (name.equals(com2.getName())) {
                        resComponent = com;
                    }
                }
            }
            if (name.equals(com.getName())) {
                resComponent = com;
            }
        }
        if (resComponent == null) throw new RpgException("名前が不適切です: " + name);
        return resComponent;
    }

    public void initView() {
        // 画面作成
        Dimension windowSize = displaySise();
        int xPos = (int) windowSize.getWidth() / 4;
        int yPos = (int) windowSize.getHeight() / 5;
        createView(xPos, yPos, windowSize);

        Scene story = null;
        // タイトル文章の表示 シーン番号０はタイトル画面
        story = config.getScenes().get(0);
        // シーンタイプを取得
        //story.getSceneType();
        try {
            story.playAudio();
            // . 入力を受けるポップアップメニュー
            inputSelector = new InputSelector(story,this);
            if (story.isHtml()) {
                inputSelector.changeHtml(story.getPath(), false);
            } else {
                inputSelector.changeText();
                textArea.setVisible(true);
                textArea.setText(inputSelector.convertStory(story.getStory(), player));
            }

            inputSelector.addKeyListener(this);
            inputSelector.show(this, xPos - 30, yPos + 220);
        } catch (RpgException e) {
            e.printStackTrace();
            openEndDialog(e.getMessage());
        }
    }

    private void createView(int xPos, int yPos, Dimension windowSize) {
        setBounds(xPos, yPos, xPos * 2, yPos * 3);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TitleLabel titleLabel = new TitleLabel("Text RPG", windowSize);
        textArea = new RpgTextArea(windowSize);
        textArea.addKeyListener(this);
        textArea.addMouseListener(this);

        JPanel titlePanel = new JPanel();
        titlePanel.setName(TITLE_PANEL);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));

        sceneNoLbl = new JLabel("Scene: 0");
        sceneNoLbl.setBounds(xPos, yPos, 100, 20);

        titlePanel.add(sceneNoLbl);
        titlePanel.add(titleLabel);

        JPanel textPanel = new JPanel();
        textPanel.setName(TEXT_PANEL);
        textPanel.add(textArea);


        Container contentPane = getContentPane();
        contentPane.add(titlePanel, BorderLayout.NORTH);
        contentPane.add(textPanel, BorderLayout.CENTER);

        // コマンドようのパネル
        createCommandPanel();
        setVisible(true);

    }

    private void createCommandPanel() {
        txt = new JPanel();
        txt.setName("commandPanel");
    }
    private void pressESC() {
        int res = JOptionPane.showConfirmDialog(null
                , "alert", "終了しますか？"
                , JOptionPane.YES_NO_OPTION);

        switch (res) {
            case JOptionPane.YES_OPTION:
                System.exit(0);
                break;
            case JOptionPane.NO_OPTION:
                break;
        }
    }

    private void presSpace() {
        if (txt.getComponents().length == 0) {
            JLabel lbl = new JLabel("Cmmand; ");
            field = new RpgTextField(CMD_TEXT, this);
            txt.add(lbl);
            txt.add(field);
            getContentPane().add(txt, BorderLayout.SOUTH);
        }
        Dimension size = getSize();
        size.setSize(size.getWidth(), size.getHeight()+ txt.getHeight());
        txt.setVisible(true);
        setVisible(true);
        txt.requestFocus();
        field.requestFocus();
    }

    public static void openEndDialog(String message) {
        JOptionPane.showMessageDialog(null, "alert", message
                , JOptionPane.INFORMATION_MESSAGE);
    }
    private void pressEnter() {
//        Component[] coms = getContentPane().getComponents();
//        for (Component com:coms) {
//            System.out.println("Com: " + com.getName());
//        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_ESCAPE:
                pressESC();
                break;
            case KeyEvent.VK_SPACE:
                presSpace();
                break;
            case KeyEvent.VK_ENTER:
                pressEnter();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     * マウスで他の部分をクリックすると
     * ポップアップが消えるのでそれを改修
     * @param e マウスイベント
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        inputSelector.show(e.getComponent(), e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
