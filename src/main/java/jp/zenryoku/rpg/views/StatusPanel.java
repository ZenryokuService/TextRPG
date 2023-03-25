package jp.zenryoku.rpg.views;

import jp.zenryoku.rpg.ConfigLoader;
import jp.zenryoku.rpg.TextRpgMain;
import jp.zenryoku.rpg.character.Player;
import jp.zenryoku.rpg.data.param.Item;
import jp.zenryoku.rpg.data.param.Params;
import jp.zenryoku.rpg.exception.RpgException;

import javax.swing.*;
import javax.swing.text.html.parser.Entity;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * ステータス表示、所持アイテム表示を行うパネルを開く。
 */
public class StatusPanel extends JFrame implements ActionListener {
    private static boolean isDebug = true;

    public StatusPanel() {
    }
    public StatusPanel(Player player) throws RpgException {
        super();
        if (player == null) {
            throw new RpgException("プレーヤーが生成されていません。");
        }
        init(player);
    }

    public void init(Player player) {
        JDialog dialog = createJDialog();
        JButton but = new JButton("Close");
        but.addActionListener(this);

        JPanel panel = createRootPanel();
        panel.add(but, BorderLayout.SOUTH);
        dialog.add(panel);

        JPanel center = new JPanel();
        center.setLayout(new GridLayout(1,1));

        JPanel cLeft = createCenterLeftPanel(player);
        center.add(cLeft);

        JPanel cRight = createCenterRight(player);
        center.add(cRight);
        panel.add(center, BorderLayout.CENTER);

        dialog.setVisible(true);
    }
    protected JDialog createJDialog() {
        JDialog dialog = new JDialog(this);
        dialog.setBounds(100, 150, 400, 500);
//        dialog.setSize(500, 800);
        return dialog;
    }

    protected JPanel createRootPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        return panel;
    }

    /** ステータス表示部分の作成 */
    protected JPanel createCenterLeftPanel(Player player) {
        // 名前
        JLabel name = new JLabel(player.getName());
        //panel.add(name, BorderLayout.NORTH);

        JPanel cLeft = new JPanel();
        cLeft.setLayout(new BoxLayout(cLeft, BoxLayout.Y_AXIS));

        // ステータスの数 + 1でアイテムも高さをそろえる
        int statusSize = 0;
        cLeft.add(name);
        Params[] list = sortMap(player.getStatus());

        for (Params p : list) {
            JLabel lbl = new JLabel(p.getName() + " : " + formatString(p.getValue(), 3, true));
            lbl.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            lbl.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
            cLeft.add(lbl);
            statusSize++;
        }
        return cLeft;
    }

    protected JPanel createCenterRight(Player player) {
        JPanel cRight = new JPanel();
        cRight.setLayout(new BoxLayout(cRight, BoxLayout.Y_AXIS));

        List<Item> items = player.getItems();

        JLabel itemName = new JLabel("アイテム");
        itemName.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        cRight.add(itemName);

        //cRight.add(Box.createRigidArea(new Dimension(10, 10)));
        int statusSize = player.getStatus().size();
        if (isDebug) System.out.println("ItemSize: " + items.size());
        for (int i = 0; i < statusSize - 1; i++) {
            if (i < (items.size())) {
                Item it = items.get(i);
                JLabel lbl = new JLabel(it.getName());
                setEmptyVorder(lbl);
                cRight.add(lbl);
            } else {
                JLabel tmp = new JLabel("-");
                setEmptyVorder(tmp);
                cRight.add(tmp);
                //cRight.add(Box.createRigidArea(new Dimension(10, 0)));
            }
        }
        return cRight;
    }
    private Params[] sortMap(Map<String, Params> map) {
        Set<String> set = map.keySet();

        Params[] arr = new Params[map.size()];
        for (String key : set) {
            Params p = map.get(key);
            arr[p.getIndex()] = p;
        }
        return arr;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.removeAll();
        this.setVisible(false);
    }

    protected String formatString(int s, int size, boolean isRight) {
        return formatString(String.valueOf(s), size, isRight);
    }

    protected String formatString(String str, int size, boolean isRight) {
        String result = null;
        if (isRight) {
            result =  String.format("%" + size + "s", str);
        } else {
            result =  String.format("%-" + size + "s", str);
        }
        return result;
    }

    protected void setEmptyVorder(JComponent com) {
        com.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }
}
