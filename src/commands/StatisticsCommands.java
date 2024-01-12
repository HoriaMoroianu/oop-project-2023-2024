package commands;

import app.audio.collections.Album;
import app.audio.collections.Playlist;
import app.audio.files.Song;
import app.clients.Artist;
import app.clients.Client;
import app.management.Library;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public final class StatisticsCommands extends CommandStrategy {
    private final ArrayList<String> result = new ArrayList<>();
    private static final int MAX_RESULTS = 5;

    public StatisticsCommands(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        outputNode.remove("user");

        return switch (command) {
            case "getAllUsers" -> getAllUsers();
            case "getOnlineUsers" -> getOnlineUsers();
            case "getTop5Songs" -> getTopSongs();
            case "getTop5Albums" -> getTopAlbums();
            case "getTop5Playlists" -> getTopPlaylists();
            case "getTop5Artists" -> getTopArtists();
            default -> null;
        };
    }

    private ObjectNode getAllUsers() {
        Library.getLibrary().getUsers().values()
                .stream().map(Client::getUsername).forEach(result::add);

        Library.getLibrary().getArtists().values()
                .stream().map(Client::getUsername).forEach(result::add);

        Library.getLibrary().getHosts().values()
                .stream().map(Client::getUsername).forEach(result::add);

        outputNode.put("result", objectMapper.valueToTree(result));
        return outputNode;
    }

    private ObjectNode getOnlineUsers() {
        result.addAll(Library.getLibrary().getOnlineUsers());
        result.sort(String::compareTo);

        outputNode.put("result", objectMapper.valueToTree(result));
        return outputNode;
    }

    private ObjectNode getTopSongs() {
        ArrayList<Song> songs = new ArrayList<>(Library.getLibrary().getSongs());
        songs.sort(Comparator.comparingInt(Song::getLikesReceived).reversed());
        songs.stream().limit(MAX_RESULTS).forEach(song -> result.add(song.getName()));

        outputNode.put("result", objectMapper.valueToTree(result));
        return outputNode;
    }

    private ObjectNode getTopAlbums() {
        HashMap<Album, Integer> albumLikes = new HashMap<>();
        for (Album album : Library.getLibrary().getAlbums()) {
            albumLikes.put(album, album.likesReceived());
        }

        albumLikes.entrySet().stream().sorted((o1, o2) -> !o1.getValue().equals(o2.getValue())
                ? Integer.compare(o2.getValue(), o1.getValue())
                : o1.getKey().getName().compareTo(o2.getKey().getName()))
            .limit(MAX_RESULTS).map(Map.Entry::getKey).map(Album::getName).forEach(result::add);

        outputNode.put("result", objectMapper.valueToTree(result));
        return outputNode;
    }

    private ObjectNode getTopPlaylists() {
        ArrayList<Playlist> playlists = new ArrayList<>();

        for (Playlist playlist : Library.getLibrary().getPlaylists()) {
            if (playlist.getVisibility().equals("public")) {
                playlists.add(playlist);
            }
        }

        playlists.sort(Comparator.comparingInt(Playlist::getFollowers).reversed());
        playlists.stream().limit(MAX_RESULTS).forEach(playlist -> result.add(playlist.getName()));

        outputNode.put("result", objectMapper.valueToTree(result));
        return outputNode;
    }

    private ObjectNode getTopArtists() {
        HashMap<String, Integer> artistLikes = new HashMap<>();

        for (Artist artist : Library.getLibrary().getArtists().values()) {
            int likes = artist.getAlbums().stream().mapToInt(Album::likesReceived).sum();
            artistLikes.put(artist.getUsername(), likes);
        }

        artistLikes.entrySet().stream().sorted((o1, o2) -> !o1.getValue().equals(o2.getValue())
                ? Integer.compare(o2.getValue(), o1.getValue()) // sort descending by likes
                : o1.getKey().compareTo(o2.getKey()))           // sort ascending by username
            .limit(MAX_RESULTS).map(Map.Entry::getKey).forEach(result::add);

        outputNode.put("result", objectMapper.valueToTree(result));
        return outputNode;
    }
}
