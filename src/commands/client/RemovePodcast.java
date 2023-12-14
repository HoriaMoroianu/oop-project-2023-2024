package commands.client;

import app.audio.collections.Podcast;
import app.clients.Client;
import app.clients.Host;
import app.management.Library;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;

public final class RemovePodcast extends Command {
    private final String name;
    public RemovePodcast(final CommandInput commandInput) {
        super(commandInput);
        name = commandInput.getName();
    }

    @Override
    protected ObjectNode executeCommand() {
        Library.getLibrary().updateMusicPlayers();
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

        if (!host.getPodcastsNames().contains(name)) {
            message = username + " doesn't have a podcast with the given name.";
            return new ObjectMapper().valueToTree(this);
        }

        Podcast podcastToDelete = host.getPodcasts().stream()
                .filter(album -> album.getName().equals(name))
                .findFirst().orElse(null);

        if (!podcastToDelete.getListeners().isEmpty()) {
            message = username + " can't delete this podcast.";
            return new ObjectMapper().valueToTree(this);
        }

        Library.getLibrary().getPodcasts().remove(podcastToDelete);
        host.getPodcasts().remove(podcastToDelete);

        message = username + " deleted the podcast successfully.";
        return new ObjectMapper().valueToTree(this);
    }
}
