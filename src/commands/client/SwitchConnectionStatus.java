package commands.client;

import app.clients.Client;
import app.clients.User;
import app.management.Library;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;

public final class SwitchConnectionStatus extends Command {
    public SwitchConnectionStatus(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        Client client = Library.getLibrary().getClient(username);
        if (client == null) {
            message = "The username " + username + " doesn't exist.";
            return new ObjectMapper().valueToTree(this);
        }

        if (client.getClass() != User.class) {
            message = username + " is not a normal user.";
            return new ObjectMapper().valueToTree(this);
        }

        ((User) client).switchOnlineStatus();
        message = username + " has changed status successfully.";
        return new ObjectMapper().valueToTree(this);
    }
}
