package commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import lombok.Getter;

@Getter
public abstract class Command {
    private String command;
    private Integer timestamp;

    @JsonProperty("user")
    private String username;
    // TODO 'message' here?

    public Command(final CommandInput commandInput) {
        command = commandInput.getCommand();
        username = commandInput.getUsername();
        timestamp = commandInput.getTimestamp();
    }

    public abstract ObjectNode executeCommand();
}
