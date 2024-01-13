package commands;

import app.clients.Artist;
import app.clients.Client;
import app.clients.User;
import app.clients.services.ClientStats;
import app.management.Library;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class WrappedCommands extends CommandStrategy {
    private ObjectNode resultNode = objectMapper.createObjectNode();

    public WrappedCommands(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        Client client = Library.getLibrary().getClient(username);
        Library.getLibrary().updateMusicPlayers();

        if (Library.getLibrary().getUsers().get(username) != null) {
            return userWrapped((User) client);
        }

        if (Library.getLibrary().getArtists().get(username) != null) {
            return artistWrapped((Artist) client);
        }

        if (Library.getLibrary().getHosts().get(username) != null) {
//            return ...
        }
        return null;
    }

    private ObjectNode userWrapped(final User user) {
        ClientStats userStats = user.getClientStats();

        if (userStats.getSongsListened().isEmpty() && userStats.getEpisodesListened().isEmpty()) {
            message = "No data to show for user " + username + ".";
            outputNode.put("message", message);
            return outputNode;
        }

        if (!userStats.getArtistsListened().isEmpty()) {
            LinkedHashMap<String, Integer> topArtists = getTopStats(userStats.getArtistsListened());
            resultNode.put("topArtists", objectMapper.valueToTree(topArtists));
        }

        if (!userStats.getGenresListened().isEmpty()) {
            LinkedHashMap<String, Integer> topGenres = getTopStats(userStats.getGenresListened());
            resultNode.put("topGenres", objectMapper.valueToTree(topGenres));
        }

        if (!userStats.getSongsListened().isEmpty()) {
            LinkedHashMap<String, Integer> topSongs = getTopStats(userStats.getSongsListened());
            resultNode.put("topSongs", objectMapper.valueToTree(topSongs));
        }

        if (!userStats.getAlbumsListened().isEmpty()) {
            LinkedHashMap<String, Integer> topAlbums = getTopStats(userStats.getAlbumsListened());
            resultNode.put("topAlbums", objectMapper.valueToTree(topAlbums));
        }

        if (!userStats.getEpisodesListened().isEmpty()) {
            LinkedHashMap<String, Integer> topEpisodes = getTopStats(userStats.getEpisodesListened());
            resultNode.put("topEpisodes", objectMapper.valueToTree(topEpisodes));
        }

        outputNode.put("result", objectMapper.valueToTree(resultNode));
        return outputNode;
    }

    private ObjectNode artistWrapped(final Artist artist) {
        ClientStats artistStats = artist.getClientStats();

        if (artistStats.getListeners().isEmpty()) {
            message = "No data to show for user " + username + ".";
            outputNode.put("message", message);
            return outputNode;
        }

        if (!artistStats.getAlbumsListened().isEmpty()) {
            LinkedHashMap<String, Integer> topAlbums = getTopStats(artistStats.getAlbumsListened());
            resultNode.put("topAlbums", objectMapper.valueToTree(topAlbums));
        }

        if (!artistStats.getSongsListened().isEmpty()) {
            LinkedHashMap<String, Integer> topSongs = getTopStats(artistStats.getSongsListened());
            resultNode.put("topSongs", objectMapper.valueToTree(topSongs));
        }

        ArrayList<String> topFans = new ArrayList<>(getTopStats(artistStats.getListeners()).keySet());
        resultNode.put("topFans", objectMapper.valueToTree(topFans));
        resultNode.put("listeners", artistStats.getListeners().size());

        outputNode.put("result", objectMapper.valueToTree(resultNode));
        return outputNode;
    }

    // TODO host

    private LinkedHashMap<String, Integer> getTopStats(final HashMap<String, Integer> stats) {
        return stats.entrySet().stream().sorted((o1, o2) ->
                !o1.getValue().equals(o2.getValue())
                        ? Integer.compare(o2.getValue(), o1.getValue())
                        : o1.getKey().compareTo(o2.getKey()))
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (x, y) -> y, LinkedHashMap::new));
    }
}
