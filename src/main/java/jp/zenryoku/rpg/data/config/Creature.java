package jp.zenryoku.rpg.data.config;

/**
 * 生物を表す。
 */
public class Creature extends StoryConfig {
    /** 生息地域(地域のIDで指定する) */
    private String regionId;
    /** 定住フラグ */
    private boolean isSettlement;
}
