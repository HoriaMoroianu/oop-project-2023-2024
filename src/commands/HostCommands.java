package commands;

import app.audio.collections.Podcast;
import app.audio.files.Episode;
import app.clients.Client;
import app.clients.Host;
import app.clients.services.Announcement;
import app.management.Library;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import fileio.input.EpisodeInput;

import java.util.ArrayList;

public final class HostCommands extends CommandStrategy {
    private Client client;
    private Host host;
    private final String name;
    private final String description;
    private final ArrayList<EpisodeInput> episodes = new ArrayList<>();

    public HostCommands(final CommandInput commandInput) {
        super(commandInput);
        name = commandInput.getName();
        description = commandInput.getDescription();
        if (commandInput.getEpisodes() != null) {
            episodes.addAll(commandInput.getEpisodes());
        }
    }

    @Override
    protected ObjectNode executeCommand() {
        client = Library.getLibrary().getClient(username);

        return switch (command) {
            case "addPodcast" -> addPodcast();
            case "removePodcast" -> removePodcast();
            case "addAnnouncement" -> addAnnouncement();
            case "removeAnnouncement" -> removeAnnouncement();
            default -> null;
        };
    }

    private ObjectNode addPodcast() {
        if (clientNotHost()) {
            return outputNode;
        }

        if (host.getPodcastsNames().contains(name)) {
            message = username + " has another podcast with the same name.";
            outputNode.put("message", message);
            return outputNode;
        }

        ArrayList<Episode> podcastEpisodes = new ArrayList<>();
        ArrayList<String> episodesNames = new ArrayList<>();

        for (EpisodeInput episodeInput : episodes) {
            Episode episode = new Episode(episodeInput);

            if (episodesNames.contains(episode.getName())) {
                message = username + " has the same episode in this podcast.";
                outputNode.put("message", message);
                return outputNode;
            }

            podcastEpisodes.add(episode);
            episodesNames.add(episode.getName());
        }

        Podcast podcast = new Podcast(name, host.getUsername(), podcastEpisodes);
        Library.getLibrary().getPodcasts().add(podcast);

        host.getPodcasts().add(podcast);
        host.getSubscribedUsers().forEach(user ->
                user.updateNotifications("New Podcast", username));

        message = username + " has added new podcast successfully.";
        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode removePodcast() {
        if (clientNotHost()) {
            return outputNode;
        }

        Library.getLibrary().updateMusicPlayers();

        if (!host.getPodcastsNames().contains(name)) {
            message = username + " doesn't have a podcast with the given name.";
            outputNode.put("message", message);
            return outputNode;
        }

        Podcast podcastToDelete = host.getPodcasts().stream()
                .filter(album -> album.getName().equals(name))
                .findFirst().orElse(null);

        if (!podcastToDelete.getListeners().isEmpty()) {
            message = username + " can't delete this podcast.";
            outputNode.put("message", message);
            return outputNode;
        }

        Library.getLibrary().getPodcasts().remove(podcastToDelete);
        host.getPodcasts().remove(podcastToDelete);

        message = username + " deleted the podcast successfully.";
        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode addAnnouncement() {
        if (clientNotHost()) {
            return outputNode;
        }

        if (host.getAnnouncementsNames().contains(name)) {
            message = username + " has already added an announcement with this name.";
            outputNode.put("message", message);
            return outputNode;
        }

        host.getAnnouncements().add(new Announcement(name, description));
        host.getSubscribedUsers().forEach(user ->
                user.updateNotifications("New Announcement", username));

        message = username + " has successfully added new announcement.";
        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode removeAnnouncement() {
        if (clientNotHost()) {
            return outputNode;
        }

        if (!host.getAnnouncementsNames().contains(name)) {
            message = username + " has no announcement with the given name.";
            outputNode.put("message", message);
            return outputNode;
        }

        Announcement announcementToDelete = host.getAnnouncements().stream()
                .filter(announcement -> announcement.getName().equals(name))
                .findFirst().orElse(null);

        host.getAnnouncements().remove(announcementToDelete);
        message = username + " has successfully deleted the announcement.";
        outputNode.put("message", message);
        return outputNode;
    }

    private boolean clientNotHost() {
        if (client == null) {
            message = "The username " + username + " doesn't exist.";
            outputNode.put("message", message);
            return true;
        }

        if (client.getClass() != Host.class) {
            message = username + " is not a host.";
            outputNode.put("message", message);
            return true;
        }

        host = (Host) client;
        return false;
    }
}
