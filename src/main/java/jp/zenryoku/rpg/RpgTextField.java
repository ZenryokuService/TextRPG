package jp.zenryoku.rpg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class RpgTextField extends JTextField implements KeyListener, FocusListener {
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
        if (panelMap == null) {
            panelMap = new HashMap<>();
            Component[] coms = main.getComponents();
            for (Component c : coms) {
                allComponents((Container) c, "commandPanel", panelMap);
            }
        }
        if (KeyEvent.VK_ENTER == e.getKeyCode()) {
            JPanel p = (JPanel) panelMap.get("commandPanel");
            this.setText("");
            p.setVisible(false);
            panelMap = null;
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
