package jp.zenryoku.rpg.views;

import jp.zenryoku.rpg.ConfigLoader;
import jp.zenryoku.rpg.TextRpgMain;
import jp.zenryoku.rpg.action.CommandMenu;
import jp.zenryoku.rpg.action.SelectMenu;
import jp.zenryoku.rpg.character.Monster;
import jp.zenryoku.rpg.character.Player;
import jp.zenryoku.rpg.data.SceneType;
import jp.zenryoku.rpg.exception.RpgException;
import jp.zenryoku.rpg.RpgTextArea;
import jp.zenryoku.rpg.data.config.*;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Random;

/**
 * クラス InputSelector の注釈をここに書きます.
 * テキストRPGの選択肢を表示するためのポップアップを作成する。
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
public class InputSelector extends JPopupMenu implements ActionListener
{
    /** 改行コード */
    private static final String SEP = System.lineSeparator();
    /** テキストエリア、文言の表示を行う */
    private static RpgTextArea textArea;
    /** JFrameを継承しているメインクラス */
    private static TextRpgMain main;
    /** プレーヤー */
    private Player player;
    /** モンスター */
    private Monster monster;
    /** 戦闘フラグ */
    boolean isBattle;

    /**
     * Storyクラスから必要な情報を取得して
     * それぞれのシーンに対応した、文章とActionを生成する。
     */
    public InputSelector() {
        super();
        addSeparator();
        // Storyファイルのデータ
    }

    /**
     * 選択肢のポップアップを作成する。
     * @param story シーンを表示するための情報を保持するクラス
     *
     */
    public InputSelector(Scene story, TextRpgMain main) throws RpgException {
        super(story.getId());
        this.main = main;
        this.textArea = main.getTextArea();
        addSeparator();
        openSelectMenu(story);
    }
    
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        try {
            finalize();
        } catch(Throwable t) {
            System.out.println("ポップアップの削除に失敗しました");
            System.exit(-1);
        }
    }

    private void openSelectMenu(Scene story) {
        List<Select> selects = story.getSelects();
        if (selects == null) {
            createSingleSelect(story);
            return;
        }
        for(Select sel : selects) {
            SelectMenu menu = new SelectMenu(sel);
            menu.addActionListener(this);
            add(menu);
            addSeparator();
        }
    }

    private void createSingleSelect(Scene story) {
        Select sel = new Select(story.getNextScene(), "すすむ");
        SelectMenu act = new SelectMenu(sel);
        add(act);
    }

    private void playScene(Scene nextScene, SelectMenu item) throws RpgException {
        isBattle = false;
        switch (nextScene.getSceneType()) {
            case PLAYER_SELECT:
                String command = item.getActionCommand();
                player = ConfigLoader.getInstance().getPlayers().get(command);
                main.setPlayer(player);
            case STORY:
                textArea.setText(convertStory(item.getNextStory(), player));
                break;
            case BATTLE:
                int low = nextScene.getMonsterNoLow();
                int high = nextScene.getMonsterNoHigh();
                int rnd = new Random().nextInt(high) + low;
                monster = ConfigLoader.getInstance().getMonsters().get(rnd);
                isBattle = true;
                break;
            case SHOP:
                break;
            case EFFECT:
                break;
        }
    }

    private String convertStory(String story, Player play) {
        String newStory = null;
        if (play != null) {
            //System.out.println("*** Testing ***");
            newStory = story.replaceAll("\\$player.name", play.getName());
        } else {
            newStory = story;
        }
        return newStory;
    }

    /**
     * 表示したメニューの押下時に処理を行う。アクション用のメソッド,
     * @param event the event to be processed
     */
    public void actionPerformed(ActionEvent event) {
        removeAll();
        SelectMenu selectItem = null;
        CommandMenu cmdItem = null;
        Object menuItem = event.getSource();

        // 押下したメニューを閉じる
        this.setVisible(false);

        // TODO-[バトルシーンを選択したときにコマンドメニューは取得していない]
        if ( menuItem instanceof SelectMenu) {
            selectItem = (SelectMenu) event.getSource();
            selectProcess(selectItem);
        } else if (menuItem instanceof CommandMenu) {
            cmdItem = (CommandMenu) event.getSource();
            //commandProcess(cmdItem);
        }
    }

    /**
     * シーンタイプが、BATTLE以外の時実行する処理(Process)
     *
     * @param selectItem　JMenuItemの拡張クラス
     */
    private void selectProcess(SelectMenu selectItem) {
        // 次のシーンを表示する
        try {
            Scene nextScene = ConfigLoader.getInstance().getScenes().get(selectItem.getNextSceneNo());
            playScene(nextScene, selectItem);
            System.out.println("*** " + nextScene.getName() + " : " + nextScene.getSceneType() + " ***");
            if (isBattle == false) {
                System.out.println("Select");
                openSelectMenu(nextScene);
            } else {
                System.out.println("Battle");
                preCommandProcess();
            }
            openMenuWindow();
        } catch (RpgException e) {
            e.printStackTrace();
        }
    }

    /**
     * シーンタイプがBATTLEの時の処理(Process)
     */
    private void preCommandProcess() throws RpgException {
        StringBuilder build = new StringBuilder();
        build.append(monster.getName() + "があらわれた" + SEP);
        if (monster.isTalk()) {
            build.append(monster.getMessage() + SEP);
        }
        textArea.setText(build.toString());
        // コマンドの取得
        if (this.player == null ) {
            throw new RpgException("プレーヤーがインスタンス化されていません。");
        }
        if (this.monster == null) {
            throw new RpgException("モンスターがインスタンス化されていません。");
        }
        List<Command> cmdList = player.getJob().getCommandList();
        for (Command cmd : cmdList) {
            CommandMenu menu = new CommandMenu(cmd);
            menu.addActionListener(this);
            add(menu);
            addSeparator();
        }
    }

    private void openMenuWindow() {
        Dimension windowSize = main.displaySise();
        int xPos = (int) windowSize.getWidth() / 4;
        int yPos = (int) windowSize.getHeight() / 5;
        show(main, xPos - 30, yPos + 220);
    }
}
