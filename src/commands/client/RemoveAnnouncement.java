package commands.client;

import app.Announcement;
import app.clients.Client;
import app.clients.Host;
import app.management.Library;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;

public final class RemoveAnnouncement extends Command {
    private final String name;

    public RemoveAnnouncement(final CommandInput commandInput) {
        super(commandInput);
        name = commandInput.getName();
    }

    @Override
    protected ObjectNode executeCommand() {
        // TODO duplicate
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

        if (!host.getAnnouncementsNames().contains(name)) {
            message = username + " has no announcement with the given name.";
            return new ObjectMapper().valueToTree(this);
        }

        Announcement announcementToDelete = host.getAnnouncements().stream()
                .filter(announcement -> announcement.getName().equals(name))
                .findFirst().orElse(null);

        host.getAnnouncements().remove(announcementToDelete);
        message = username + " has successfully deleted the announcement.";
        return new ObjectMapper().valueToTree(this);
    }
}
