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
    private static final String SEP = System.lineSeparator();
    private static RpgTextArea textArea;
    private static TextRpgMain main;
    private Player play;
    private Monster monster;

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

    private void openCommandMenu() {
        List<Command> cmdList = play.getJob().getCommandList();
        for (Command cmd : cmdList) {
            CommandMenu menu = new CommandMenu(cmd);
            menu.addActionListener(this);
            add(menu);
            addSeparator();
        }
    }
    private boolean playScene(Scene nextScene, SelectMenu item) throws RpgException {
        boolean isBattle = false;
        switch (nextScene.getSceneType()) {
            case PLAYER_SELECT:
                String command = item.getActionCommand();
                play = ConfigLoader.getInstance().getPlayers().get(command);
                main.setPlayer(play);
            case STORY:
                textArea.setText(convertStory(item.getNextStory(), play));
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
        return isBattle;
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

    public void actionPerformed(ActionEvent event) {
        removeAll();
        SelectMenu selectItem = null;
        CommandMenu cmdItem = null;
        Object menuItem = event.getSource();

        // 押下したメニューを閉じる
        this.setVisible(false);

        System.out.println("*** " + menuItem.getClass().getSimpleName() + " ***");
        if ( menuItem instanceof SelectMenu) {
            selectItem = (SelectMenu) event.getSource();
            selectProcess(selectItem);
        } else if (menuItem instanceof CommandMenu) {
            cmdItem = (CommandMenu) event.getSource();
            commandProcess(cmdItem);
        }
    }

    private void selectProcess(SelectMenu selectItem) {
        // 次のシーンを表示する
        try {
            Scene nextScene = ConfigLoader.getInstance().getScenes().get(selectItem.getNextSceneNo());
            boolean isBattle = playScene(nextScene, selectItem);
            if (isBattle) {
                openCommandMenu();
            } else {
                openSelectMenu(nextScene);
            }

            Dimension windowSize = main.displaySise();
            int xPos = (int) windowSize.getWidth() / 4;
            int yPos = (int) windowSize.getHeight() / 5;
            show(main, xPos - 30, yPos + 220);
        } catch (RpgException e) {
            e.printStackTrace();
        }
    }

    private void commandProcess(CommandMenu commandMenu) {
        textArea.setText("");
        StringBuilder build = new StringBuilder();
        build.append(monster.getName() + "があらわれた" + SEP);
        if (monster.isTalk()) {
            build.append(monster.getMessage() + SEP);
        }
        textArea.setText(textArea.getText() + SEP + build.toString());
        //openCommandMenu(selectItem);

    }
}
