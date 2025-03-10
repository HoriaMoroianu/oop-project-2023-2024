package commands;

import app.clients.Artist;
import app.clients.Client;
import app.clients.Host;
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
    private final ObjectNode resultNode = objectMapper.createObjectNode();
    private static final Integer MAX_RESULTS = 5;

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
            return hostWrapped((Host) client);
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

        LinkedHashMap<String, Integer> topArtists = getTopStats(userStats.getArtistsListened());
        resultNode.put("topArtists", objectMapper.valueToTree(topArtists));

        LinkedHashMap<String, Integer> topGenres = getTopStats(userStats.getGenresListened());
        resultNode.put("topGenres", objectMapper.valueToTree(topGenres));

        LinkedHashMap<String, Integer> topSongs = getTopStats(userStats.getSongsListened());
        resultNode.put("topSongs", objectMapper.valueToTree(topSongs));

        LinkedHashMap<String, Integer> topAlbums = getTopStats(userStats.getAlbumsListened());
        resultNode.put("topAlbums", objectMapper.valueToTree(topAlbums));

        LinkedHashMap<String, Integer> topEpisodes = getTopStats(userStats.getEpisodesListened());
        resultNode.put("topEpisodes", objectMapper.valueToTree(topEpisodes));

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
            resultNode.put("topAlbums",
                    objectMapper.valueToTree(getTopStats(artistStats.getAlbumsListened())));
        }

        if (!artistStats.getSongsListened().isEmpty()) {
            resultNode.put("topSongs",
                    objectMapper.valueToTree(getTopStats(artistStats.getSongsListened())));
        }

        ArrayList<String> topFans = new ArrayList<>();
        topFans.addAll(getTopStats(artistStats.getListeners()).keySet());

        resultNode.put("topFans", objectMapper.valueToTree(topFans));
        resultNode.put("listeners", artistStats.getListeners().size());

        outputNode.put("result", objectMapper.valueToTree(resultNode));
        return outputNode;
    }

    private ObjectNode hostWrapped(final Host host) {
        ClientStats hostStats = host.getClientStats();

        if (hostStats.getListeners().isEmpty()) {
            message = "No data to show for user " + username + ".";
            outputNode.put("message", message);
            return outputNode;
        }

        if (!hostStats.getEpisodesListened().isEmpty()) {
            resultNode.put("topEpisodes",
                    objectMapper.valueToTree(getTopStats(hostStats.getEpisodesListened())));
        }

        resultNode.put("listeners", hostStats.getListeners().size());

        outputNode.put("result", objectMapper.valueToTree(resultNode));
        return outputNode;
    }

    private LinkedHashMap<String, Integer> getTopStats(final HashMap<String, Integer> stats) {
        return stats.entrySet().stream().sorted((o1, o2) ->
                !o1.getValue().equals(o2.getValue())
                        ? Integer.compare(o2.getValue(), o1.getValue())
                        : o1.getKey().compareTo(o2.getKey()))
                .limit(MAX_RESULTS)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (x, y) -> y, LinkedHashMap::new));
    }
}
