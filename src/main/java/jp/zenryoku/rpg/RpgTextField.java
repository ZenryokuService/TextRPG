package jp.zenryoku.rpg;

import jp.zenryoku.rpg.exception.RpgException;
import jp.zenryoku.rpg.views.EquipPanel;
import jp.zenryoku.rpg.views.StatusPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class RpgTextField extends JTextField implements KeyListener, FocusListener {
    /** ステータス表示コマンド　*/
    private static final String SUTATUS = "status";
    /** 装備を行うコマンド　*/
    private static final String EQUIP = "equip";
    /** 終了コマンド */
    private static final String EXIT = "exit";
    private TextRpgMain main;
    private Map<String, Component> panelMap;
    public RpgTextField(String name, TextRpgMain main) {
        super();
        setName(name);
        setColumns(30);
        addKeyListener(this);
        this.main = main;


    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            if (panelMap == null) {
                panelMap = new HashMap<>();
                Component[] coms = main.getComponents();
                for (Component c : coms) {
                    allComponents((Container) c, "commandPanel", panelMap);
                }
            }
            if (KeyEvent.VK_ENTER == e.getKeyCode()) {
                exeCommand(this.getText());
                JPanel p = (JPanel) panelMap.get("commandPanel");
                this.setText("");
                p.setVisible(false);
                panelMap = null;
            }
        } catch (RpgException re) {
            re.printStackTrace();
            TextRpgMain.openEndDialog(re.getMessage());
            System.exit(-1);
        }
    }

    private void allComponents(Container cont, final String comName, Map<String, Component> map) {
        JPanel pan = null;
        Component[] arr = cont.getComponents();
        for (Component c : arr) {
            if (comName.equals(c.getName())) {
                map.put(c.getName(), c);
                break;
            }
            allComponents((Container) c, comName, map);
        }
    }

    public void exeCommand(String command) throws RpgException {
        if (SUTATUS.equals(command)) {
            new StatusPanel(main.getPlayer());
        } else if (EQUIP.equals(command)) {
            new EquipPanel(main.getPlayer());
        } else if (EXIT.equals(command)) {
            TextRpgMain.openEndDialog("終了します。");
            System.exit(0);
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void focusGained(FocusEvent e) {
        System.out.println("Focus!");
        this.setText("");
    }

    @Override
    public void focusLost(FocusEvent e) {
        System.out.println("Focus lost!");
    }
}
