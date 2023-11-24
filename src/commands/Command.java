package commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.User;
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

    public ObjectNode performCommand() {
        Library.getLibrary().setTimestamp(timestamp);
        // TODO REFACTOR THIS:
        User user = Library.getLibrary().getUsers().get(username);
        if (user != null) {
            user.getMusicPlayer().updateMusicPlayer();
        }
        return executeCommand();
    }

    protected abstract ObjectNode executeCommand();
}
