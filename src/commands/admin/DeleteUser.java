package commands.admin;

import app.clients.Client;
import app.management.Library;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;

public final class DeleteUser extends Command {
    public DeleteUser(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        Library.getLibrary().updateMusicPlayers();
        Client client = Library.getLibrary().getClient(username);

        if (client == null) {
            message = "The username " + username + " doesn't exist.";
            return new ObjectMapper().valueToTree(this);
        }

        if (!client.getContentGuests().isEmpty()) {
            message = username + " can't be deleted.";
            return new ObjectMapper().valueToTree(this);
        }

        client.deleteClient();
        message = username + " was successfully deleted.";
        return new ObjectMapper().valueToTree(this);
    }
}
