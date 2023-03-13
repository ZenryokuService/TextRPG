package jp.zenryoku.rpg.data.config;

import lombok.Data;
import java.util.List;
import jp.zenryoku.rpg.data.param.*;

@Data
public class Job extends StoryConfig {
    private List<Command> commandList;
    private List<Params> paramList;
}
