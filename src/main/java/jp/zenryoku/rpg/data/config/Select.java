package jp.zenryoku.rpg.data.config;


import jp.zenryoku.rpg.data.Formula;
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
    /** 商品コード　Item, Wepon, ArmorのIDと一致する */
    private String shohinCd;
    /** 金額 */
    private int money;
    /** エフェクトシーン用 計算式 */
    private Formula formula;
    
    public Select() {
    }

    /**
     * ストーリーシーンでの、選択肢
     * @param no
     * @param mongon
     */
    public Select(int no, String mongon) {
        this.nextScene = no;
        this.mongon = mongon;
    }

    /**
     * ショップシーンでの選択肢
     * @param shohin　商品名
     * @param money 金額
     */
    public Select(String shohin, int money) {
        this.shohinCd = shohin;
        this.money = money;
    }

    /**
     * ショッピング購入時のセレクト。
     * ショッピングシーンでは、次シーン番号は、以下のように扱う
     * ショッピング開始時: 0
     * 「はい」：1
     * 「いいえ」: 2
     *
     * @param mongon 商品名
     * @param shohinCd 商品コード
     * @param money 金額
     */
    public Select(int no, String mongon, String shohinCd, int money) {
        this.nextScene = no;
        this.mongon = mongon;
        this.shohinCd = shohinCd;
        this.money = money;
    }
}
