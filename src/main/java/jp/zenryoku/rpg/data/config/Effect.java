package jp.zenryoku.rpg.data.config;

import jp.zenryoku.rpg.exception.RpgException;
import lombok.Data;

import java.util.List;

@Data
public class Effect implements ConfigIF {
    /** 記号 */
    private String kigo;
    /** 表示文字  */
    private String disp;
    /** 効果式　*/
    private String siki;
    /** メッセージ */
    private String message;
    /** 演算子 */
    private String ope;
    /** 数字 */
    private int num;
    /** TS */
    private String ts;
    /** ターン数  */
    private int turn;
    /** 効果式リスト */
    private List<Effect> effList;

    public Effect() {
    }

    /**
     * 通常の、「POI:どく:ZHP-10%:どくをうけた」のような定義を読み込む。
     * @param kigo 記号
     * @param disp 表示する文字
     * @param siki 効果式
     * @param message 効果発動時の表示メッセージ
     */
    public Effect(String kigo, String disp, String siki, String message) {
        this.kigo = kigo;
        this.disp = disp;
        this.siki = siki;
        this.message = message;
    }

    /**
     * シーンから実行されるイベントに対応する。効果式を保持する。
     *
     * @param kigo 記号
     * @param ope 演算子
     * @param num 数字
     * @param ts TS
     * @param turn ターン数
     * @throws RpgException
     */
    public Effect(String kigo, String ope, String num, String ts, String turn) throws RpgException {

        try {
            this.kigo = kigo;
            this.ope = ope;
            this.num = Integer.parseInt(num);
            this.ts = ts;
            this.turn = Integer.parseInt(turn);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RpgException("効果式が不適切です。" + "num: " + num + " turn: " + turn);
        }
    }
}
