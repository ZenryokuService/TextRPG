package jp.zenryoku.rpg.views;

import jp.zenryoku.rpg.ConfigLoader;
import jp.zenryoku.rpg.TextRpgMain;
import jp.zenryoku.rpg.action.CommandMenu;
import jp.zenryoku.rpg.action.SelectMenu;
import jp.zenryoku.rpg.character.Monster;
import jp.zenryoku.rpg.character.Player;
import jp.zenryoku.rpg.data.Formula;
import jp.zenryoku.rpg.data.SceneType;
import jp.zenryoku.rpg.data.config.Command;
import jp.zenryoku.rpg.data.config.Config;
import jp.zenryoku.rpg.data.config.Scene;
import jp.zenryoku.rpg.data.config.Select;
import jp.zenryoku.rpg.data.param.Armor;
import jp.zenryoku.rpg.data.param.Item;
import jp.zenryoku.rpg.data.param.Wepon;
import jp.zenryoku.rpg.exception.RpgException;
import jp.zenryoku.rpg.RpgTextArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;
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
    /** モンスターを倒したときのフラグ */
    private static final int FINISH = 0;
    /** モンスターにダメージを与えたときのフラグ */
    private static final int HIT = 1;
    /** ダメージを受けたフラグ */
    private static final int RECIVE = 2;
    /** アイテムマップのデータ */
    private static final int ITEM = 0;
    /** 武器マップのデータ */
    private static final int WEP = 1;
    /** 防具マップのデータ */
    private static final int ARM = 2;



    private static final boolean isDebug = true;
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
    private boolean isBattle;
    /** 一時退避用のシーン */
    private Scene nextScene;

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
        addSelectMenu(story);
    }

    /**
     * 表示しているポップアップメニューを削除。
     *
     * @param visible true to make the popup visible, or false to
     */
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

    /**
     * 選択肢ポップアップを開く。
     * Sceneオブジェクト内の選択リストよりメニューを生成する。
     * @param story シーンオブジェクト
     */
    private void addSelectMenu(Scene story) throws RpgException {
        List<Select> selects = story.getSelects();
        if (selects == null) {
            createSingleSelect(story);
            return;
        }
        boolean shopFlg = false;
        if (SceneType.SHOP.equals(story.getSceneType())) {
            shopFlg = true;
        }

        for(Select sel : selects) {
            if (shopFlg) {
                convertShohinSelect(sel, story.getSceneNo());
            }
            SelectMenu menu = new SelectMenu(sel);
            menu.addActionListener(this);
            add(menu);
            addSeparator();
        }
    }

    private void convertShohinSelect(Select sel, int sceneNo) throws RpgException {
        // 各マップにはキーが重複していない事を前提とする
        Map<String, Item> itemMap = ConfigLoader.getInstance().getItemMap();
        Map<String, Wepon> wepMap = ConfigLoader.getInstance().getWepMap();
        Map<String, Armor> armMap = ConfigLoader.getInstance().getArmMap();
        String shohin = sel.getShohinCd();
        if ( shohin != null || "".equals(shohin) == false) {
            String shohinCd = sel.getShohinCd();
            int shohinHandle = isKeyInMap(sel, itemMap, wepMap, armMap);
            System.out.println("*** 商品コード : " + shohinCd);
            switch(shohinHandle) {
                case ITEM:
                    Item it = itemMap.get(shohinCd);
                    sel.setMongon(it.getName());
                    break;
                case WEP:
                    Wepon wep = wepMap.get(shohinCd);
                    sel.setMongon(wep.getName());
                    break;
                case ARM:
                    Armor arm = armMap.get(shohinCd);
                    sel.setMongon(arm.getName());
                    break;

            }
        } else {
            throw new RpgException("SHOPシーンでは、shohinタグを使ってください。: シーン番号 = " + sceneNo);
        }
    }

    /**
     * Item, Wepon, Armorのいずれかの商品コードを以下のように判定する・
     * ITEM = 0: アイテムの商品コード
     * WEP = 1: 武器の商品コード
     * ITEM = 2: 防具の商品コード
     * @param sel 選択項目
     * @param itemMap アイテムマップ
     * @param wepMap 武器マップ
     * @param armMap 防具マップ
     * @return ITEM, WEP, ARMのいずれか
     */
    private int isKeyInMap(Select sel, Map<String, Item> itemMap
            , Map<String, Wepon> wepMap, Map<String, Armor> armMap) {
        // 商品コード＝アイテム、武器、防具のID
        // 各キーは重複していない
        String shohinCd = sel.getShohinCd();
        boolean b1 = itemMap.containsKey(shohinCd);
        boolean b2 = wepMap.containsKey(shohinCd);
        boolean b3 = armMap.containsKey(shohinCd);

        int isItemOrWepOrArm = 0;
        if (b1) {
            isItemOrWepOrArm = ITEM;
        }
        if (b2) {
            isItemOrWepOrArm = WEP;
        }
        if (b3) {
            isItemOrWepOrArm = ARM;
        }
        return isItemOrWepOrArm;
    }
    /**
     * シーンオブジェクトの選択リストがない、もしくは空の場合
     * 次のシーン番号へすすむ選択肢を生成、追加する。
     * @param story シーンオブジェクト
     */
    private void createSingleSelect(Scene story) {
        Select sel = new Select(story.getNextScene(), "すすむ");
        SelectMenu act = new SelectMenu(sel);
        add(act);
    }

    /**
     * 各ストーリータイプ別の処理を行う。
     *
     * @param nextScene これから表示する、次のシーンオブジェクト
     * @param item 選択肢オブジェクト
     * @return trueの場合は、戦闘シーン、そうでない場合はストーリー表示
     *
     * @throws RpgException
     */
    private void playScene(Scene nextScene, SelectMenu item) throws RpgException {
        isBattle = false;
        switch (nextScene.getSceneType()) {
            case PLAYER_SELECT:
                String command = item.getActionCommand();
                player = ConfigLoader.getInstance().getPlayers().get(command);
                main.setPlayer(player);
            case SHOP:

                break;
            case STORY:
                textArea.setText(convertStory(item.getNextStory(), player));
                break;
            case BATTLE:
                int low = nextScene.getMonsterNoLow();
                int high = nextScene.getMonsterNoHigh();
                monster = ConfigLoader.getInstance().callMonster(low, high);
                if (isDebug) System.out.println("インスタンスID;3  " + monster.hashCode());
                isBattle = true;
                break;
            case EFFECT:
                break;
        }
    }

    /**
     * ストーリー内の"$player.XXX"を置換する。
     *
     * @param story 置換前のストーリー
     * @param play プレーヤー
     * @return 置換後のストーリー
     */
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

        if (isBattle == false) {
            selectItem = (SelectMenu) event.getSource();
            selectProcess(selectItem);
        } else {
            System.out.println("*** Testing ***");
            cmdItem = (CommandMenu) event.getSource();
            battleSceneProcess(cmdItem.getCommand());
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
                addSelectMenu(nextScene);
            } else {
                System.out.println("Battle");
                this.nextScene  = nextScene;
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
        presetCommandMenu();
    }

    /**
     * コマンドオブジェクトを選択したときの処理
     */
    private void presetCommandMenu() {
        List<Command> cmdList = player.getJob().getCommandList();
        for (Command cmd : cmdList) {
            CommandMenu menu = new CommandMenu(cmd);
            menu.addActionListener(this);
            add(menu);
            addSeparator();
        }
    }

    /**
     * ポップアップを表示する処理
     */
    private void openMenuWindow() {
        Dimension windowSize = main.displaySise();
        int xPos = (int) windowSize.getWidth() / 4;
        int yPos = (int) windowSize.getHeight() / 5;
        show(main, xPos - 30, yPos + 220);
    }

    /**
     * バトルシーンの実装
     * @param cmd コマンドオブジェクト
     */
    private void battleSceneProcess(Command cmd) {
        String tmp = textArea.getText();

        textArea.setText(tmp + SEP + player.getName() + "の" + cmd.getName() + "!");
        Formula f = cmd.getFormula();

        try {
            int damage = player.attack(f, monster);
            printHaNiWaDamage(player, monster, damage, HIT);

            if (monster.getParams(cmd.getFormula().getTarget()).getValue() <= 0) {
                printHaNiWaDamage(player, monster, 0, FINISH);
                finishBattle();
                // 次のシーンへ移動
                addSelectMenu(this.nextScene);
            } else {
                List<Command> mCmdList = monster.getType().getCommandList();
                Command mCmd = mCmdList.get(new Random().nextInt(mCmdList.size()));

                int mDamage = monster.attack(mCmd.getFormula(), player);
                printHaNiWaDamage(player, monster, mDamage, RECIVE);

                main.setPlayer(player);

                if (player.getParams(mCmd.getFormula().getTarget()).getValue() <= 0) {
                    JOptionPane.showMessageDialog(main
                            , player.getName() + "は力尽きた。。。"
                            , "GAME　OVER"
                            , JOptionPane.ERROR_MESSAGE);
                }
                presetCommandMenu();
            }
        } catch (RpgException e) {
            e.printStackTrace();
        }
        openMenuWindow();
    }

    /**
     * 画面に文字列を追加して表示する。
     * @param message 追加で表示するj文字列
     */
    private void printText(String message) {
        textArea.setText(textArea.getText() + SEP + message);
    }

    /**
     * ダメージを与えた、受けた、倒した。を表示する
     *
     * @param p プレーヤー
     * @param m モンスター
     * @param damage 対象のダメージ
     * @param state 状態フラグ
     *
     * @throws RpgException
     */
    private void printHaNiWaDamage(Player p, Monster m , int damage, int state) throws RpgException {
        switch (state) {
            case FINISH:
                printText(p.getName() + "は" + m.getName() + "をたおした。");
                isBattle = false;
                break;
            case HIT:
                printText(p.getName() + "は" + m.getName() + "に" + damage + "のダメージをあたえた");
                break;
            case RECIVE:
                printText(p.getName() + "は" + m.getName() + "に" + damage  + "のダメージをうけた");
                break;
            default:
                throw new RpgException("想定外のフラグです" + state);
        }
    }
    private void finishBattle() throws RpgException {
        int money = monster.getMoney();
        int exp = monster.getExp();

        Config conf = ConfigLoader.getInstance().getConf();
        printText(player.getName() + "は、" + exp + "の経験値と" + money + conf.getMoney().get(0).getName() + "を取得した。");
        isBattle = false;
        monster.finalize();
        monster = null;
        if (isDebug) System.out.println("インスタンスID4; " + monster);
        System.gc();
    }
}
