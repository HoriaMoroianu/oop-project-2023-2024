package commands;

import app.clients.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import app.management.Library;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;

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

    protected abstract ObjectNode executeCommand();

    /**
     * Updates the application timestamp and executes this command
     * @return ObjectNode that contains the output of the command
     */
    public ObjectNode performCommand() {
        Library.getLibrary().setTimestamp(timestamp);

        ObjectNode userDisabledNode = userDisabled();
        return (userDisabledNode != null) ? userDisabledNode : executeCommand();
    }

    private ObjectNode userDisabled() {
        User user = Library.getLibrary().getUsers().get(username);

        String userDisabledCommands = "search, select, load, playPause, repeat, shuffle, "
                + "forward, backward, like, next, prev, createPlaylist, addRemoveInPlaylist, "
                + "switchVisibility, follow, changePage, printCurrentPage";

        if (user == null || user.isOnlineStatus() || !userDisabledCommands.contains(command)) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        message = username + " is offline.";
        objectNode.put("command", command);
        objectNode.put("user", username);
        objectNode.put("timestamp", timestamp);
        objectNode.put("message", message);

        if (command.equals("search")) {
            objectNode.put("results",
                    objectMapper.valueToTree(new ArrayList<String>()));
        }

        return objectNode;
    }
}
