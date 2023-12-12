package commands.search_bar;

import app.audio.collections.Album;
import app.clients.Artist;
import app.clients.Client;
import app.clients.Host;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import app.audio.collections.AudioTrack;
import app.management.Library;
import app.audio.collections.Playlist;
import app.audio.collections.Podcast;
import app.audio.files.Song;
import app.clients.User;
import commands.Command;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

@JsonIgnoreProperties({ "type", "filter", "maxSearchResults" })
public final class Search extends Command {
    private final String type;
    private final Filter filter;
    private final int maxSearchResults = 5;

    @Getter
    private final ArrayList<String> results = new ArrayList<>();

    public Search(final CommandInput commandInput) {
        super(commandInput);
        type = commandInput.getType();
        filter = new Filter(commandInput.getFilters());
    }

    @Override
    public ObjectNode executeCommand() {
        User user = Library.getLibrary().getUsers().get(username);
        user.getMusicPlayer().removeTrack();

        ArrayList<AudioTrack> audioTracks = new ArrayList<>();
        ArrayList<Client> clients = new ArrayList<>();

        switch (type) {
            case "song":
                audioTracks = searchSongs(Library.getLibrary().getSongs());
                break;
            case "podcast":
                audioTracks = searchPodcast(Library.getLibrary().getPodcasts());
                break;
            case "playlist":
                audioTracks = searchPlaylist(userAccessiblePlaylists());
                break;
            case "album":
                audioTracks = searchAlbum(Library.getLibrary().getAlbums());
                break;
            case "artist":
                clients = searchArtist(Library.getLibrary().getArtists());
                break;
            case "host":
                clients = searchHost(Library.getLibrary().getHosts());
                break;
            default:
                break;
        }

        if ("song, playlist, podcast, album".contains(type)) {
            message = "Search returned " + audioTracks.size() + " results";
            audioTracks.forEach(audioTrack -> results.add(audioTrack.getName()));
            user.getSearchBar().setSearchedTracks(audioTracks, type);
        } else {
            message = "Search returned " + clients.size() + " results";
            clients.forEach(client -> results.add(client.getUsername()));
            user.getSearchBar().setSearchedClients(clients, type);
        }

        return new ObjectMapper().valueToTree(this);
    }

    private ArrayList<Playlist> userAccessiblePlaylists() {
        ArrayList<Playlist> userAccessible = new ArrayList<>();

        for (Playlist playlist : Library.getLibrary().getPlaylists()) {
            if (playlist.getOwner().equals(username)
                    || playlist.getVisibility().equals("public")) {
                userAccessible.add(playlist);
            }
        }
        return userAccessible;
    }

    private ArrayList<AudioTrack> searchSongs(final ArrayList<Song> songs) {
        return songs.stream()
            .filter(filter::filterByName)
            .filter(filter::filterByAlbum)
            .filter(filter::filterByTags)
            .filter(filter::filterByLyrics)
            .filter(filter::filterByGenre)
            .filter(filter::filterByReleaseYear)
            .filter(filter::filterByArtist)
            .limit(maxSearchResults)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<AudioTrack> searchPodcast(final ArrayList<Podcast> podcasts) {
        return podcasts.stream()
            .filter(filter::filterByName)
            .filter(filter::filterByOwner)
            .limit(maxSearchResults)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<AudioTrack> searchPlaylist(final ArrayList<Playlist> playlists) {
        return playlists.stream()
            .filter(filter::filterByName)
            .filter(filter::filterByOwner)
            .limit(maxSearchResults)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<AudioTrack> searchAlbum(final ArrayList<Album> albums) {
        return albums.stream()
                .filter(filter::filterByName)
                .filter(filter::filterByOwner)
                .filter(filter::filterByDescription)
                .limit(maxSearchResults)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<Client> searchArtist(final HashMap<String, Artist> artists) {
        return artists.values().stream()
                .filter(filter::filterByName)
                .limit(maxSearchResults)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<Client> searchHost(final HashMap<String, Host> hosts) {
        return hosts.values().stream()
                .filter(filter::filterByName)
                .limit(maxSearchResults)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
