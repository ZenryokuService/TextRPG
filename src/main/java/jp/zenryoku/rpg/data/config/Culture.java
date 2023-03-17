package jp.zenryoku.rpg.data.config;

import lombok.Data;

/**
 * 文化を表す
 */
@Data
public class Culture extends StoryConfig {
    /** 生活様式、テキストファイルへのパス */
    private String life_style;
    /** 生活習慣、テキストファイルへのパス */
    private String habit;
    /** 価値観、テキストファイルへのパス */
    private String values;
    /** 世界観、テキストファイルへのパス */
    private String view_of_world;
    /** 規範、テキストファイルへのパス */
    private String norm;
    /** 思考様式、テキストファイルへのパス */
    private String way_of_thinking;
    /** 社会制度、テキストファイルへのパス */
    private String social_system;
    /** 社会構造、テキストファイルへのパス */
    private String social_structure;
    /** 組織、テキストファイルへのパス */
    private String organization;
}
