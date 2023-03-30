package jp.zenryoku.rpg;

import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Color;

/**
 * クラス TitleLabel の注釈をここに書きます.
 * JLabelを拡張して、テキストRPGのタイトルをセットするラベルを作成する。
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
public class TitleLabel extends JLabel
{
    public TitleLabel(String title, Dimension windowSize) {
        super(title);
        int width = (int) windowSize.getWidth();
        int height = (int) windowSize.getHeight() / 16 - 20;
        Dimension labelSize = new Dimension(width, height);
        setOpaque(true);
        setPreferredSize(labelSize);
        setBackground(Color.GREEN);
        setHorizontalAlignment(JLabel.CENTER);
    }
    
}
