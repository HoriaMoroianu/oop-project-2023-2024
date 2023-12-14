package commands.client;

import app.Announcement;
import app.clients.Client;
import app.clients.Host;
import app.management.Library;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;

public final class AddAnnouncement extends Command {
    private final String name;
    private final String description;

    public AddAnnouncement(final CommandInput commandInput) {
        super(commandInput);
        name = commandInput.getName();
        description = commandInput.getDescription();
    }

    @Override
    protected ObjectNode executeCommand() {
        // TODO solve duplicate
        Client client = Library.getLibrary().getClient(username);
        if (client == null) {
            message = "The username " + username + " doesn't exist.";
            return new ObjectMapper().valueToTree(this);
        }

        if (client.getClass() != Host.class) {
            message = username + " is not a host.";
            return new ObjectMapper().valueToTree(this);
        }

        Host host = (Host) client;

        if (host.getAnnouncementsNames().contains(name)) {
            message = username + " has already added an announcement with this name.";
            return new ObjectMapper().valueToTree(this);
        }

        host.getAnnouncements().add(new Announcement(name, description));
        message = username + " has successfully added new announcement.";
        return new ObjectMapper().valueToTree(this);
    }
}
