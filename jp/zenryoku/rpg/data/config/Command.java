package jp.zenryoku.rpg.data.config;

import lombok.Data;
import jp.zenryoku.rpg.data.*;

@Data
public class Command implements ConfigIF {
    private String id;
    private String name;
    private Formula formula;
    
    public Command() {
    }
    
    public Command(String id, String name, Formula formula) {
        this.id = id;
        this.name = name;
        this.formula = formula;
    }
}
