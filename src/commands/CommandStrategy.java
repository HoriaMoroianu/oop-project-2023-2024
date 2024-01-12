package commands;

import app.clients.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import app.management.Library;
import fileio.input.CommandInput;

import java.util.ArrayList;

public abstract class CommandStrategy {
    protected final String command;
    protected final Integer timestamp;
    protected final String username;

    protected String message;
    protected ObjectMapper objectMapper = new ObjectMapper();
    protected ObjectNode outputNode;

    public CommandStrategy(final CommandInput commandInput) {
        command = commandInput.getCommand();
        username = commandInput.getUsername();
        timestamp = commandInput.getTimestamp();
        outputNode = objectMapper.createObjectNode();
    }

    protected abstract ObjectNode executeCommand();

    /**
     * Updates the application timestamp and executes this command
     * @return ObjectNode that contains the output of the command
     */
    public ObjectNode performCommand() {
        Library.getLibrary().setTimestamp(timestamp);

        outputNode.put("command", command);
        outputNode.put("user", username);
        outputNode.put("timestamp", timestamp);

        return userDisabled() ? outputNode : executeCommand();
    }

    private boolean userDisabled() {
        User user = Library.getLibrary().getUsers().get(username);

        String userDisabledCommands = "search, select, load, playPause, repeat, shuffle, "
                + "forward, backward, like, next, prev, createPlaylist, addRemoveInPlaylist, "
                + "switchVisibility, follow, changePage, printCurrentPage";

        if (user == null || user.isOnlineStatus() || !userDisabledCommands.contains(command)) {
            return false;
        }

        message = username + " is offline.";
        outputNode.put("message", message);

        if (command.equals("search")) {
            ArrayList<String> results = new ArrayList<>();
            outputNode.put("results", objectMapper.valueToTree(results));
        }

        return true;
    }
}
