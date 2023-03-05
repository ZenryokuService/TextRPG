package jp.zenryoku.rpg.data.param;

import lombok.Data;
/**
 * クラス Params の注釈をここに書きます.
 * プレーヤーのステータスを表す。
 * Config.xmlで定義した項目に依存する。
 * 
 * @author (Takunoji)
 * @version (1.0)
 */
@Data
public class Params
{
    /** キー、項目の取得に使用する */
    private String key;
    /** 名前 */
    private String name;
    /** 値、項目の値を示す(整数型とする */
    private int value;
    
    public Params() {
    }
    
    public Params(String key, String name, int value) {
        setKey(key);
        setName(name);
        setValue(value);

    }
}
