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
        if (story.getNextScene() == -1) {
            return;
        }
        List<Select> selects = story.getSelects();
        if (selects == null) {
            createSingleSelect(story);
            return;
        }
        SceneType type = story.getSceneType();
        if (SceneType.SHOP.equals(type)) {
            isShopping = true;
            bakNextScene = story;
        }
        if (SceneType.EFFECT.equals(type)) {
            isEffect = false;
        }
        for(Select sel : selects) {
            if (isShopping) {
                Item itm = ConfigLoader.getInstance().getItemFromShohinCd(sel, -1);
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
     * シーンオブジェクトの選択リストがない、もしくは空の場合
     * 次のシーン番号へすすむ選択肢を生成、追加する。
     * @param story シーンオブジェクト
     */
    private void createSingleSelect(Scene story) {
        if (story.getNextScene() == -1) {
            if (isDebug) System.out.println("Next -1");
            return;
        }
        System.out.println("Select " + story.getNextScene());
        Select sel = new Select(story.getNextScene(), "すすむ");
        SelectMenu act = new SelectMenu(sel);
        act.addActionListener(this);
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
                prepareItems(nextScene);
                break;
            case BATTLE:
                int low = nextScene.getMonsterNoLow();
                int high = nextScene.getMonsterNoHigh();
                int no = nextScene.getMonsterNo();
                if (no != 0) {
                    monster = ConfigLoader.getInstance().callMonster(no);
                } else {
                    monster = ConfigLoader.getInstance().callMonster(low, high);
                }
                if (isDebug) System.out.println("インスタンスID;3  " + monster.hashCode());
                isBattle = true;
                break;
        }
    }

    private void prepareItems(Scene scene) throws RpgException {
        List<String> list = scene.getItems();
        if (list != null && list.size() > 0) {
            for (String shihinCd : list) {
                System.out.println("--" + shihinCd);
                Item it = ConfigLoader.getItemFormShohinCd(shihinCd);
                if (isDebug) System.out.println("pre@areItem: " + it.getName());
                player.getItems().add(it);
            }
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
        if (isDebug) System.out.println("*** ActionPerformed ***");
        removeAll();
        SelectMenu selectItem = null;
        CommandMenu cmdItem = null;

        // 押下したメニューを閉じる
        this.setVisible(false);
        if (isDebug) System.out.println("battle: " + isBattle + " shopping: " + isShopping + " check: " + isChecking);
        try {
            if (isShopping) {
                System.out.println("*** Shopping ***");
                shoppingProcess((SelectMenu) event.getSource());
                return ;
            }
            if (isEffect) {
                System.out.println("*** Effect ***");
                processEffect((SelectMenu) event.getSource());
            }
            if (isBattle == false) {
                System.out.println("*** Story ***");
                selectItem = (SelectMenu) event.getSource();
                selectProcess(selectItem);
            } else {
                System.out.println("*** Battle ***");
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
        if (START == menu.getNextSceneNo()) { // 「これでよいですか？」を表示
            initShopping(menu, YES, NO);
            //isShopping = true;
        } else if (YES == menu.getNextSceneNo()) { // 「はい」を押下したとき
            if (isChecking) {// 「他にようはありますか？」→「はい」の処理
                System.out.println("* YES & Check:true");
                textArea.setText(bakNextScene.getStory());
                addSelectMenu(bakNextScene);
                isChecking = false;
            } else { // 「これでよいですか？」→「はい」の処理
                buyProcess(menu);
                isChecking = true;
            }
            openMenuWindow();
        } else if (NO == menu.getNextSceneNo()) { // 「いいえ」を押下したとき
            if (isChecking) { // 「他にようはありますか？」→「いいえ」を押下したとき
                if (isDebug) System.out.println("*** NO & Check true ***");
                printText("ありがとうございました。");
                isChecking = false;
                isShopping = false;
                Scene next = ConfigLoader.getInstance().getScenes().get(bakNextScene.getNextScene());
                textArea.setText(convertStory(next.getStory(), player));
                if (SceneType.EFFECT.equals(next.getSceneType())) {
                    prepareItems(next);
                }
                addSelectMenu(next);
            } else {
                // 「これでよいですか？」→　「いいえ」を押下したとき
                if (isDebug) System.out.println("* NO & Check:false ");
                textArea.setText(bakNextScene.getStory());
                addSelectMenu(bakNextScene);
            }
            openMenuWindow();
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

        Item it = ConfigLoader.getItemFromShohinCd(sel, -1);
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
        System.out.println("BPK: " + max.getValue() + " List-; " + player.getItems().size());
        player.getItems().add(it);
        System.out.println("List-After; " + player.getItems().size());
        main.setPlayer(player);
        printText("他にようはありますか？");
        addYesNoMenuItem(sel, YES, NO);
    }

    /**
     * シーンタイプが、BATTLE以外の時実行する処理(Process)
     *
     * @param selectItem　JMenuItemの拡張クラス
     */
    private void selectProcess(SelectMenu selectItem) throws RpgException {
        if (selectItem == null) {
            return;
        }
        // 次のシーンを表示する
        try {
            int nextSceneNo = selectItem.getNextSceneNo();
            System.out.println("selectProcess: SceneNo=" + nextSceneNo);
            Scene nextScene = ConfigLoader.getInstance().getScenes().get(nextSceneNo);
            if (nextScene == null) {
                throw new RpgException("指定のシーン番号が定義されていません。" + nextSceneNo);
            }
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
            if (nextScene.getSceneNo() == -1) {
                return;
            }
            System.out.println("OpenMenu");
            openMenuWindow();
        } catch (RpgException e) {
            e.printStackTrace();
            throw e;
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
        this.requestFocus();
    }

    /**
     * バトルシーンの実装
     * @param cmd コマンドオブジェクト
     */
    private void battleSceneProcess(Command cmd) throws RpgException {
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
            throw e;
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
        printText(player.getName() + "は、" + exp + "の経験値と" + money + "を取得、" + conf.getMoney().get(0).getName() + "を取得した。");
        player.addExp(exp);
        player.addMoney(money);
        main.setPlayer(player);
        isBattle = false;
        monster.finalize();
        monster = null;
        if (isDebug) System.out.println("インスタンスID4; " + monster);
        System.gc();
    }
}
