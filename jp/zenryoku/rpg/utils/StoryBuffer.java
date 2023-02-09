package jp.zenryoku.rpg.utils;


/**
 * クラス StoryBuffer の注釈をここに書きます.
 * 文字列の保持クラス、シングルトンで常にインスタンスは一つ。
 * 
 * @author (あなたの名前)
 * @version (バージョン番号もしくは日付)
 */
public final class StoryBuffer 
{
    private static final String SEP = System.lineSeparator();
    private static StoryBuffer instance;
    private static final StringBuilder BUF = new StringBuilder();

    /** コンストラクタ */
    private StoryBuffer() {
    }
    
    /**
     * このクラスのインスタンスを返却する。
     * インスタン生成済みの場合はそのままインスタンスを返却する。
     * @return StoryBuffer
     */
    public static StoryBuffer getInstance() {
        if (instance == null) {
            instance = new StoryBuffer();
        }
        return instance;
    }

    /**
     * 文字列をバッファに追加。最後に開業を入れる
     */
    public void append(String str) {
        BUF.append(str);
    }

    /**
     * 文字列をバッファに追加。改行のあるなしをハンドルする。
     */
    public void append(String str, boolean isKaigyo) {
        if(isKaigyo) {
            // 文末に改行
            BUF.append(str + SEP);
        } else {
            // 改行なし
            BUF.append(str);
        }
    }

    /**
     * 文字列を返却する。
     */
    public String toString() {
        return BUF.toString();
    }
    
    /**
     * 文字列バッファの中身をすべて削除します。
     */
    public void clear() {
        BUF.delete(0, BUF.length());
    }
}
