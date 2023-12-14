package commands.client;

import app.audio.collections.Podcast;
import app.audio.files.Episode;
import app.clients.Client;
import app.clients.Host;
import app.management.Library;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;
import fileio.input.EpisodeInput;

import java.util.ArrayList;

public final class AddPodcast extends Command {
    private final String name;
    private final ArrayList<EpisodeInput> episodes = new ArrayList<>();

    public AddPodcast(final CommandInput commandInput) {
        super(commandInput);
        name = commandInput.getName();
        episodes.addAll(commandInput.getEpisodes());
    }

    @Override
    protected ObjectNode executeCommand() {
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

        if (host.getPodcastsNames().contains(name)) {
            message = username + " has another podcast with the same name.";
            return new ObjectMapper().valueToTree(this);
        }

        ArrayList<Episode> podcastEpisodes = new ArrayList<>();
        ArrayList<String> episodesNames = new ArrayList<>();

        for (EpisodeInput episodeInput : episodes) {
            Episode episode = new Episode(episodeInput);

            if (episodesNames.contains(episode.getName())) {
                message = username + " has the same episode in this podcast.";
                return new ObjectMapper().valueToTree(this);
            }

            podcastEpisodes.add(episode);
            episodesNames.add(episode.getName());
        }

        Podcast podcast = new Podcast(name, host.getUsername(), podcastEpisodes);
        host.getPodcasts().add(podcast);
        Library.getLibrary().getPodcasts().add(podcast);

        message = username + " has added new podcast successfully.";
        return new ObjectMapper().valueToTree(this);
    }
}
