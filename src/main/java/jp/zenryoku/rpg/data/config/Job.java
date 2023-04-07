package jp.zenryoku.rpg.data.config;

import jp.zenryoku.rpg.data.param.Params;
import lombok.Data;
import java.util.List;

@Data
public class Job extends StoryConfig {
    private List<String> commandStr;
    private List<Command> commandList;
    private List<Params> paramList;
}
