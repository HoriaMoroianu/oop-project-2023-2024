package commands.client;

import app.clients.User;
import app.management.Library;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;

public final class SwitchConnectionStatus extends Command {
    public SwitchConnectionStatus(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        User user = Library.getLibrary().getUsers().get(username);

        if (user == null) {
            message = "The username " + username + " doesn't exist.";
            return new ObjectMapper().valueToTree(this);
        }

        // TODO: <username> is not a normal user.

        user.switchOnlineStatus();
        message = username + " has changed status successfully.";
        return new ObjectMapper().valueToTree(this);
    }
}
