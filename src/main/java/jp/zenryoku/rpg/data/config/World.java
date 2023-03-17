package jp.zenryoku.rpg.data.config;

import lombok.Data;

import java.util.List;
import java.util.ArrayList;

@Data
public class World extends StoryConfig {
    /** img */
    private String imgUrl;
    /** 魔法などの発動する仕組み、説明、テキストファイルへのパス */
    private String logic;
    /** 国 */
    private List<Country> countries;
    /** 文明 */
    private List<Civilization> civilizations;
    /** 文化 */
    private List<Culture>  cultures;
    /** 自然 */
    private Nature nature;
    /** 組織の説明、テキストファイルへのパス */
    private String organization;
    /** 食物連鎖(説明、テキストファイルへのパス) */
    private String food_chain;
    /** 生物、Creture.xmlへのパス */
    private List<Creature> creatures;
    /** 地域 */
    private List<Region> regions;
    /** 生活様式の説明、テキストファイルへのパス */
    private String life_style;
    /** 社会構造の説明、テキストファイルへのパス */
    private String social_structure;

    public World() {
        countries = new ArrayList<>();
        civilizations = new ArrayList<>();
        cultures = new ArrayList<>();
        nature = new Nature();
        regions = new ArrayList<>();
    }
}
