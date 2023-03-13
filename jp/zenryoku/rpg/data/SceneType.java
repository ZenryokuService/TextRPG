package jp.zenryoku.rpg.data;


/**
 * Enumeration class SceneType - write a description of the enum class here
 * 
 * @author (your name here)
 * @version (version number or date here)
 */
public enum SceneType
{
    /** 基本的なシーン */
    STORY(0)
    /** バトルシーン */
    , BATTLE(1)
    /** ショップシーン */
    , SHOP(2)
    /** エフェクトシーン */
    , EFFECT(3)
    /** プレーヤー選択 */
    , PLAYER_SELECT(4)
    ;
    
    /** シーンタイプ(番号) */
    private int sceneType;
    
    private SceneType(int sceneType) {
        this.sceneType = sceneType;
    }
}
