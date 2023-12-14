package commands.client;

import app.Event;
import app.clients.Artist;
import app.clients.Client;
import app.management.Library;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;

public final class RemoveEvent extends Command {
    private String name;

    public RemoveEvent(final CommandInput commandInput) {
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

        if (client.getClass() != Artist.class) {
            message = username + " is not an artist.";
            return new ObjectMapper().valueToTree(this);
        }

        Artist artist = (Artist) client;

        if (!artist.getEventsNames().contains(name)) {
            message = username + " doesn't have an event with the given name.";
            return new ObjectMapper().valueToTree(this);
        }

        Event eventToDelete = artist.getEvents().stream()
                .filter(event -> event.getName().equals(name))
                .findFirst().orElse(null);

        artist.getEvents().remove(eventToDelete);
        message = username + " deleted the event successfully.";
        return new ObjectMapper().valueToTree(this);
    }
}
