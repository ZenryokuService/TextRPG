package jp.zenryoku.rpg.views;

import jp.zenryoku.rpg.ConfigLoader;
import jp.zenryoku.rpg.EquipList;
import jp.zenryoku.rpg.character.Player;
import jp.zenryoku.rpg.data.Formula;
import jp.zenryoku.rpg.data.param.Armor;
import jp.zenryoku.rpg.data.param.Item;
import jp.zenryoku.rpg.data.param.Params;
import jp.zenryoku.rpg.data.param.Wepon;
import jp.zenryoku.rpg.exception.RpgException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 装備を行うパ値を開く
 */
public class EquipPanel extends StatusPanel implements ListSelectionListener {
    private static boolean isDebug = true;
    /** プレーヤー */
    private Player player;
    private EquipList equipList;
    private JLabel bukiValue;
    private JLabel boguValue;
    private JLabel attackPow;
    private JLabel deffecePow;
    public EquipPanel(Player player) throws RpgException {
        super();
        if (player == null) {
            throw new RpgException("プレーヤーが生成されていません。");
        }
        init(player);
    }

    protected JPanel createCenterLeftPanel(Player player) {
        this.player = player;
        JPanel cLeft = new JPanel();
        cLeft.setLayout(new BoxLayout(cLeft, BoxLayout.Y_AXIS));

        JPanel bukiLine = new JPanel();
        JLabel buki = new JLabel(formatString("ぶき: ", 4, false));
        bukiLine.add(buki);
        if (player.getWepon() != null) {
            bukiValue = new JLabel(formatString(player.getWepon().getName(), 10, true));
        } else {
            bukiValue = new JLabel(formatString("そうびなし", 10, true));
        }
//        bukiValue.setName("bukiValue");
        bukiLine.add(bukiValue);
        cLeft.add(bukiLine);

        JPanel boguLine = new JPanel();
        JLabel bogu = new JLabel(formatString("ぼうぐ: ", 4, false));
        boguLine.add(bogu);
        if (player.getArmor() != null) {
            boguValue = new JLabel(formatString(player.getArmor().getName(), 10, true));
        } else {
            boguValue = new JLabel(formatString("そうびなし", 10, true));
        }
//        boguValue.setName("boguValue");
        boguLine.add(boguValue);
        cLeft.add(boguLine);

        JPanel num1Line = new JPanel();
        JLabel num1 = new JLabel(formatString("こうげき力: ", 5, false));
        num1Line.add(num1);
        attackPow = new JLabel(String.valueOf(player.getAttackPower()));
        num1Line.add(attackPow);
        cLeft.add(num1Line);

        JPanel num2Line = new JPanel();
        JLabel num2 = new JLabel(formatString("ぼうぎょ力: ", 5, false));
        num2Line.add(num2);
        deffecePow = new JLabel(String.valueOf(player.getDeffencePower()));
        num2Line.add(deffecePow);
        cLeft.add(num2Line);

        cLeft.add(Box.createRigidArea(new Dimension(20, 500)));

        return cLeft;
    }

    private JLabel getWponForumula(Wepon wep) throws RpgException {
        Formula f = ConfigLoader.getInstance().getFormulas().get("ATK");
        return new JLabel(formatString("ぼうぎょ力: ", 5, false));
    }
    protected JDialog createJDialog() {
        JDialog dialog = new JDialog(this);
        dialog.setBounds(100, 150, 300, 500);
//        dialog.setSize(500, 800);
        return dialog;
    }

    protected JPanel createCenterRight(Player player) {
        JPanel cRight = new JPanel();
        cRight.setLayout(new BoxLayout(cRight, BoxLayout.Y_AXIS));

        List<Item> items = player.getItems();

        if (isDebug) System.out.println("ItemSize: " + items.size());
        DefaultListModel list = EquipList.createModel(player);

        equipList = new EquipList(list);
        equipList.addListSelectionListener(this);
        equipList.setBukiValue(bukiValue);
        equipList.setBoguValue(boguValue);
        equipList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        cRight.add(equipList);
        return cRight;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        final String SOBI_NASHI = "そうびなし";
        if (e.getValueIsAdjusting() || equipList.getSelectedIndex() == -1) {
            return;
        }
        boolean noSobi = false;
        if (SOBI_NASHI.equals(bukiValue.getText()) || SOBI_NASHI.equals(boguValue.getText())) {
            noSobi = true;
        }
        List<Item> itemList = player.getItems();
        int idx = equipList.getSelectedIndex();
        Item item = itemList.get(idx);
        if (isDebug) System.out.println("select: " + item.getName());
        if (item instanceof Wepon) {
            if (isDebug) System.out.println("*** Wepon ***");
            if (noSobi == false) {
                itemList.add(player.getWepon());
            }
            bukiValue.setText(item.getName());
            player.setWepon((Wepon) item);
            attackPow.setText(String.valueOf(player.getAttackPower()));
        } else if (item instanceof Armor) {
            if (isDebug) System.out.println("*** Armor ***");
            if (noSobi == false) {
                itemList.add(player.getArmor());
            }
            boguValue.setText(item.getName());
            player.setArmor((Armor) item);
            deffecePow.setText(String.valueOf(player.getDeffencePower()));
        }
        if (noSobi) {
            DefaultListModel list = EquipList.createModel(player);
            equipList.removeAll();
            equipList.setListData(list.toArray());
        }
    }
}
