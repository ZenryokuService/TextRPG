package jp.zenryoku.rpg.data.config;

import lombok.Data;

/**
 * 文明を表すクラス。
 * 基本的にゲーム作成者の指定する、設定情報を保持する。
 */
@Data
public class Civilization extends StoryConfig {
    /** 文字、テキストファイルへのパス */
    private String character;
    /** 建造物、テキストファイルへのパス */
    private String structures;
    /** 技術、テキストファイルへのパス */
    private String technology;
    /** 芸術、テキストファイルへのパス */
    private String arts;
}
