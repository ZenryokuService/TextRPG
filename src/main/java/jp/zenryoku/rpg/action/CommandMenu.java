package jp.zenryoku.rpg.action;

import jp.zenryoku.rpg.data.config.Command;

public class CommandMenu extends SelectMenu {

    private Command command;

    public CommandMenu() {
    }

    public CommandMenu(Command cmd) {
        super(cmd.getName());
        this.command = cmd;
    }

    public Command getCommand() { return command; }
    public void setCommand(Command cmd) { this.command = cmd; }
}
