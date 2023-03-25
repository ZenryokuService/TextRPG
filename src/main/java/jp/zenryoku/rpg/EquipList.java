package jp.zenryoku.rpg;

import jp.zenryoku.rpg.character.Player;
import jp.zenryoku.rpg.data.param.Item;
import jp.zenryoku.rpg.views.EquipPanel;
import lombok.Data;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.List;

/**
 * 装備リストを表示するためのクラス
 */
@Data
public class EquipList extends JList  {

    private JLabel bukiValue;
    private JLabel boguValue;
    private List<Item> items;

    /** デフォルトコンストラクタ */
    public EquipList() {
    }

    public EquipList(DefaultListModel model) {
        super(model);
    }

    public static DefaultListModel createModel(Player player) {
        List<Item> items = player.getItems();
        DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < items.size(); i++) {
            Item it = items.get(i);
            model.add(i, it.getName());
        }
        return model;
    }
}
