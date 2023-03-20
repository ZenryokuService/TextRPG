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
import jp.zenryoku.rpg.data.param.Params;
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
    /** ショップシーン中のフラグ */
    private boolean isShopping;
    /** 購入確認フラグ① */
    private boolean isChecking;
    /** エフェクトシーンフラグ */
    private boolean isEffect;
    /** 一時退避用のシーン */
    private Scene bakNextScene;
    /** アイテムマップのデータ */
    private final int ITEM = 0;
    /** 武器マップのデータ */
    private final int WEP = 1;
    /** 防具マップのデータ */
    private final int ARM = 2;


    /**
     * Storyクラスから必要な情報を取得して
     * それぞれのシーンに対応した、文章とActionを生成する。
     */
    public InputSelector() {
        super();
        isShopping = false;
        isBattle = false;
        isEffect = false;
        addSeparator();
    }

    /**
     * 選択肢のポップアップを作成する。
     * @param story シーンを表示するための情報を保持するクラス
     *
     */
    public InputSelector(Scene story, TextRpgMain main) throws RpgException {
        super(story.getId());
        this.main = main;
        textArea = main.getTextArea();
        isShopping = false;
        isBattle = false;
        isEffect = false;
        addSeparator();
        addSelectMenu(story);
    }

    /**
     * 表示しているポップアップメニューを削除。インスタンスの作尾j
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
        if (SceneType.SHOP.equals(story.getSceneType())) {
            isShopping = true;
            bakNextScene = story;
        }

        for(Select sel : selects) {
            if (isShopping) {
                Item itm = ConfigLoader.getInstance().getItemFormShohinCd(sel, -1);
                sel.setMongon(itm.getName());
                this.bakNextScene = story;
            }
            SelectMenu menu = new SelectMenu(sel);
            menu.addActionListener(this);
            add(menu);
            addSeparator();
        }
    }

    /**
     * Item, Wepon, Armorのいずれかの商品コードを以下のように判定する・
     * ITEM = 0: アイテムの商品コード
     * WEP = 1: 武器の商品コード
     * ITEM = 2: 防具の商品コード
     * @param sel 選択項目
     * @return ITEM, WEP, ARMのいずれか
     */
    private int isKeyInMap(Select sel) throws RpgException {
        // 商品コード＝アイテム、武器、防具のID
        Map<String, Item> itemMap = ConfigLoader.getInstance().getItemMap();
        Map<String, Wepon> wepMap = ConfigLoader.getInstance().getWepMap();
        Map<String, Armor> armMap = ConfigLoader.getInstance().getArmMap();
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
        if (b1 == false && b2 == false && b3 == false) {
            throw new RpgException("想定外の商品コードです。" + shohinCd);
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
                // 文字表示のみなので何もしない。
            case STORY:
                textArea.setText(convertStory(item.getNextStory(), player));
                break;
            case EFFECT:
                isEffect = true;
                textArea.setText(convertStory(item.getNextStory(), player));
                break;
            case BATTLE:
                int low = nextScene.getMonsterNoLow();
                int high = nextScene.getMonsterNoHigh();
                monster = ConfigLoader.getInstance().callMonster(low, high);
                if (isDebug) System.out.println("インスタンスID;3  " + monster.hashCode());
                isBattle = true;
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

        // 押下したメニューを閉じる
        this.setVisible(false);

        try {
            if (isShopping) {
                shoppingProcess((SelectMenu) event.getSource());
                return ;
            }
            if (isEffect) {
                processEffect((SelectMenu) event.getSource());
            }
            if (isBattle == false) {
                selectItem = (SelectMenu) event.getSource();
                selectProcess(selectItem);
            } else {
                cmdItem = (CommandMenu) event.getSource();
                battleSceneProcess(cmdItem.getCommand());
            }
        } catch (RpgException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(main
                    , e.getMessage()
                    , "実行時エラー"
                    , JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    private void processEffect(SelectMenu item) throws RpgException {
        Formula f = item.getSelect().getFormula();
        System.out.println("Formula: " + f.getFormulaStr());
        System.out.println("moey: " + player.getMoney());
        player.effect(f);
        System.out.println("money: " + player.getMoney());
        main.setPlayer(player);
        isEffect = false;
    }
    private void shoppingProcess(SelectMenu menu) throws RpgException {
        final int START = 0;
        final int YES = 1;
        final int NO = 2;
        Select sel = menu.getSelect();
        if (isDebug) {
            System.out.println("初品コード: " + sel.getShohinCd());
            System.out.println("金額: " + sel.getMoney());
            System.out.println("文言: " + sel.getMongon());
            System.out.println("次: " + sel.getNextScene());
        }
        if (START == menu.getNextSceneNo()) {
            initShopping(menu, YES, NO);
        } else if (YES == menu.getNextSceneNo()) {
            if (isChecking) {
                int zankin = player.getMoney() - sel.getMoney();
                player.setMoney(zankin);
                textArea.setText(bakNextScene.getStory());
                isChecking = false;
                addSelectMenu(bakNextScene);
                openMenuWindow();
            } else {
                buyProcess(menu);
                openMenuWindow();
            }
        } else if (NO == menu.getNextSceneNo()) {
            if (isChecking) {
                printText("ありがとうございました。");
                isShopping = false;
                isChecking = false;
                Scene next = ConfigLoader.getInstance().getScenes().get(bakNextScene.getNextScene());
                textArea.setText(next.getStory());
                addSelectMenu(next);
                openMenuWindow();
            } else {
                isChecking = false;
                textArea.setText(bakNextScene.getStory());
                addSelectMenu(bakNextScene);
                openMenuWindow();
            }
        }
    }

    private void addYesNoMenuItem(Select sel, final int YES, final int NO) {

        SelectMenu select1 = new SelectMenu(new Select(YES,  "はい", sel.getShohinCd(), sel.getMoney()));
        add(select1);
        addSeparator();
        select1.addActionListener(this);

        SelectMenu select2 = new SelectMenu(new Select( NO, "いいえ", sel.getShohinCd(), sel.getMoney()));
        add(select2);
        addSeparator();
        select2.addActionListener(this);

        openMenuWindow();
    }
    private void initShopping(SelectMenu menu, final int YES, final int NO) {
        Select sel = menu.getSelect();
        printText(menu.getSelect().getMongon() + "でよろしいですか？" + SEP);
        addYesNoMenuItem(sel, YES, NO);
    }

    /**
     * 購入処理。
     * @param menu 選択したメニュー
     * @throws RpgException
     */
    private void buyProcess(SelectMenu menu) throws RpgException {
        printText("お買い上げありがとうございます。" );
        Select sel = menu.getSelect();

        Item it = ConfigLoader.getItemFormShohinCd(sel, -1);
        // TODO-[アイテムの最大所持数の取得方法]
        int itemSize = player.getItems().size();
        // アイテムの最大所持数と比較する。
        Params max = ConfigLoader.getInstance().getParamsMap().get("BPK");
        if (max == null ) {
            throw new RpgException("BPKが設定されていません。in Config.xml");
        }
        int YES = 1;
        int NO = 2;
        if (itemSize < max.getValue()) {
            printText("アイテムが持ちきれません。" + SEP);
            addYesNoMenuItem(sel, YES, NO);
            return;
        }
        int zan = player.getMoney() - sel.getMoney();
        System.out.println(zan);
        player.setMoney(zan);
        player.getItems().add(it);
        main.setPlayer(player);
        printText("他にようはありますか？");
        isChecking = true;
        addYesNoMenuItem(sel, YES, NO);
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
                if (isDebug) System.out.println("Select");
                addSelectMenu(nextScene);
            } else {
                if (isDebug) System.out.println("Battle");
                this.bakNextScene  = nextScene;
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
                addSelectMenu(this.bakNextScene);
                this.bakNextScene = null;
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
