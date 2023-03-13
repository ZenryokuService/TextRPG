package jp.zenryoku.rpg.data.config;


import lombok.Data;

/**
 * クラス Select の注釈をここに書きます.
 * 
 * @author (あなたの名前)
 * @version (バージョン番号もしくは日付)
 */
@Data
public class Select
{
    /** 選択番号 */
    private int nextScene;
    /** 選択肢の文言 */
    private String mongon;
    
    public Select() {
    }
    
    public Select(int no, String mongon) {
        this.nextScene = no;
        this.mongon = mongon;
    }
}
