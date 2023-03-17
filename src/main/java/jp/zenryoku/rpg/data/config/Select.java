package jp.zenryoku.rpg.data.config;


import lombok.Data;

import javax.swing.*;

/**
 * クラス Select の注釈をここに書きます.
 * シーン選択、商品選択など選択時に使用する
 * @author (Takunoji)
 * @version (1.0)
 */
@Data
public class Select
{
    /** 選択番号 */
    private int nextScene;
    /** 選択肢の文言 */
    private String mongon;
    /** 商品名 */
    private String shohin;
    /** 金額 */
    private int money;
    
    public Select() {
    }
    
    public Select(int no, String mongon) {
        this.nextScene = no;
        this.mongon = mongon;
    }
}
