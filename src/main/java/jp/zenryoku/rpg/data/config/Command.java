package jp.zenryoku.rpg.data.config;

import jp.zenryoku.rpg.data.Formula;

public class Command implements ConfigIF {
    private String id;
    private String name;
    private Formula formula;

    private boolean hasChild;
    
    public Command() {
    }
    
    public Command(String id, String name, Formula formula) {
        this.id = id;
        this.name = name;
        this.formula = formula;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Formula getFormula() {
        return formula;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }
}
