package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import lombok.Getter;

@Getter
public abstract class Command {
    private String command;
    private String user;
    private Integer timestamp;

    public Command(final CommandInput commandInput) {
        command = commandInput.getCommand();
        user = commandInput.getUsername();
        timestamp = commandInput.getTimestamp();
    }

    public abstract ObjectNode executeCommand();
}
