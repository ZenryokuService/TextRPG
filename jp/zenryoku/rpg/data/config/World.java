package jp.zenryoku.rpg.data.config;

import lombok.Data;

import java.util.List;

@Data
public class World extends StoryConfig {
    /** img */
    private String imgUrl;
    /** 自然 */
    private List<Nature> nature;
    /** 食物連鎖(説明、テキストファイルへのパス) */
    private String food_chain;
    /** 生物、Creture.xmlへのパス */
    private String creatures;
    /** 地域 */
    private List<Region> regions;
    /** 魔法などの発動する仕組み、説明、テキストファイルへのパス */
    private String logic;
    /** 文明 */
    private List<Civilzations> civilzations;
    /** 生活様式の説明、テキストファイルへのパス */
    private String life_style;
    /** 社会構造の説明、テキストファイルへのパス */
    private String social_structure;
    /** 組織の説明、テキストファイルへのパス */
    private String organization;
    /** 文化 */
    private List<Culture>  cultures;


}
