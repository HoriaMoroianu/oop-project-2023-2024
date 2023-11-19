package commands;

import fileio.input.CommandInput;

public abstract class Command {
    private String command;
    private String username;
    private Integer timestamp;

    public Command(final CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
    }

    public abstract void executeCommand();
}
