package jp.zenryoku.rpg;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * クラス TextRPGMain の注釈をここに書きます.
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
public class TextRPGMain extends JFrame
{
    private static ConfigGenerator config;
    
    public static void main(String[] args) {
        TextRPGMain main = new TextRPGMain();
        config = ConfigGenerator.getInstance();
        main.run("Text RPG");
        
    }
    
    
    public void run(String title) {
        setTitle(title);
        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        int xPos = (int) windowSize.getWidth() / 4;
        int yPos = (int) windowSize.getHeight() / 5;
        setBounds(xPos, yPos, xPos * 2, yPos * 3);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TitleLabel titleLabel = new TitleLabel("Text RPG", windowSize);
        RpgTextArea textarea = new RpgTextArea(windowSize);

        JPanel titlePanel = new JPanel();
        titlePanel.add(titleLabel);
        
        JPanel textPanel = new JPanel();
        textPanel.add(textarea);

        Container contentPane = getContentPane();
        contentPane.add(titlePanel, BorderLayout.NORTH);
        contentPane.add(textPanel, BorderLayout.CENTER);

        setVisible(true);
        
        RpgProgress story = new RpgProgress(this, textarea);
        story.run(xPos + 200, yPos + 100);
    }
}
