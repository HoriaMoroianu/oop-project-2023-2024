package commands;

import fileio.input.CommandInput;

public abstract class Command {
    private String command;
    private String username;
    private Integer timestamp;

    public Command(final CommandInput commandInput) {
        command = commandInput.getCommand();
        username = commandInput.getUsername();
        timestamp = commandInput.getTimestamp();
    }

    public abstract void executeCommand();
}
