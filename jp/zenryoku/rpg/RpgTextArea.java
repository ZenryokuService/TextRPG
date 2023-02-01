package jp.zenryoku.rpg;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

/**
 * クラス RpgTextArea の注釈をここに書きます.
 * テキストRPGの表示する文字列をこの領域に出力(描画)する。
 * 背景は黒、イメージはドラ○エのような感じにしたい。
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
public class RpgTextArea extends JTextArea
{
    /** コンストラクタ */
    public RpgTextArea(Dimension size) {
        int widthCol = (int) size.getWidth() / 19;
        int heightRow = (int) size.getHeight() / 28;

        setColumns(widthCol);
        setRows(heightRow);
        // 背景の描画準備
        setOpaque(true);
        // フォントの設定
        setFont(createTextFont());
        // 背景を黒にする
        setBackground(Color.BLACK);
        // 白い文字の設定
        setForeground(Color.WHITE);
        // 白いボーダーの設定
        Border border = BorderFactory.createLineBorder(Color.GREEN);
        setBorder(BorderFactory.createCompoundBorder(border,
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        setWrapStyleWord(true);
        setLineWrap(true);
    }
    
    /** Fontを作成して返却 */
    private Font createTextFont() {
        Font font = new Font(Font.DIALOG_INPUT, Font.BOLD, 14);
        return font;
    }
}
