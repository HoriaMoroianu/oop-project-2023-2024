package commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import lombok.Getter;

@Getter
public abstract class Command {
    private String command;

    @JsonProperty("user")
    private String username;

    private Integer timestamp;

    public Command(final CommandInput commandInput) {
        command = commandInput.getCommand();
        username = commandInput.getUsername();
        timestamp = commandInput.getTimestamp();
    }

    public abstract ObjectNode executeCommand();
}
