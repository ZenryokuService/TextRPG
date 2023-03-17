package jp.zenryoku.rpg.action;

import jp.zenryoku.rpg.ConfigLoader;
import jp.zenryoku.rpg.data.config.Scene;
import jp.zenryoku.rpg.data.config.Select;
import jp.zenryoku.rpg.exception.RpgException;

import javax.swing.*;

public class SelectMenu extends JMenuItem {
    /** 選択肢 */
    private Select select;

    public SelectMenu() {
        super();
    }

    public SelectMenu(Select select) {
        super(select.getMongon());
        this.select = select;
    }

    public String getNextStory() throws RpgException {
        Scene scene = ConfigLoader.getInstance().getScenes().get(select.getNextScene());
        return scene.getStory();
    }

    public int getNextSceneNo() {
        return select.getNextScene();
    }

    public Select getSelect() { return this.select; }
}
