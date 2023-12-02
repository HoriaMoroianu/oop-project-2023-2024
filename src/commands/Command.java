package commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import app.admin.Library;
import fileio.input.CommandInput;
import lombok.Getter;

@Getter
public abstract class Command {
    protected final String command;
    protected final Integer timestamp;

    @JsonProperty("user")
    protected String username;
    protected String message;

    public Command(final CommandInput commandInput) {
        command = commandInput.getCommand();
        username = commandInput.getUsername();
        timestamp = commandInput.getTimestamp();
    }

    /**
     * Updates the application timestamp and executes this command
     * @return ObjectNode that contains the output of the command
     */
    public ObjectNode performCommand() {
        Library.getLibrary().setTimestamp(timestamp);
        return executeCommand();
    }

    protected abstract ObjectNode executeCommand();
}
