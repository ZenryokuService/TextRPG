package jp.zenryoku.rpg.data.config;

/**
 * 国を表すクラス。
 * 基本的にゲーム作成者の指定する、設定情報を保持する。
 * 他のクラス情報と連携するとは、IDで取得する。
 */
public class Country extends StoryConfig {
    /** この国が依存する文明圏　*/
    private String civilzationId;
    /** この国が依存する文化 */
    private String cultureId;
    /** この国が依存する自然 */
    private String natureId;
    /** 国家(国家の名前がないときは国家なし) */
    private String nation;
    /** 国家制度(封建・民主主義・社会主義・その他) */
    private String nationSystem;

    public Country() {
    }
}
