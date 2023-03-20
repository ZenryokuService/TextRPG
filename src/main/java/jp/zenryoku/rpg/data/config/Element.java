package jp.zenryoku.rpg.data.config;

import lombok.Data;
/**
 * このゲームで使用する要素・エレメントを定義する。
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
@Data
public class Element
{
    /** ID */
    private String id;
    /** 要素名 */
    private String name;

    public Element() {
    }

    public Element(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
