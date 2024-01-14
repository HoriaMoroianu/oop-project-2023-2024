package commands;

import app.audio.collections.Album;
import app.audio.collections.AudioTrack;
import app.audio.collections.Playlist;
import app.audio.collections.Podcast;
import app.audio.files.Song;
import app.clients.Artist;
import app.clients.Client;
import app.clients.Host;
import app.clients.User;
import app.clients.services.Page;
import app.clients.services.SearchBar;
import app.management.Library;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;

public final class SearchbarCommands extends CommandStrategy {
    private final String type;
    private final Filter filter;
    private final Integer itemNumber;
    private static final int MAX_SEARCH_RESULTS = 5;

    public SearchbarCommands(final CommandInput commandInput) {
        super(commandInput);
        type = commandInput.getType();
        itemNumber = commandInput.getItemNumber();
        filter = commandInput.getFilters() != null ? new Filter(commandInput.getFilters()) : null;
    }

    @Override
    protected ObjectNode executeCommand() {
        return switch (command) {
            case "search" -> search();
            case "select" -> select();
            default -> null;
        };
    }

    private ObjectNode search() {
        User user = Library.getLibrary().getUsers().get(username);
        user.getMusicPlayer().updateMusicPlayer();
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
                audioTracks = searchAlbum(getAlbumsByArtistOrder());
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

        ArrayList<String> results = new ArrayList<>();

        if ("song, playlist, podcast, album".contains(type)) {
            message = "Search returned " + audioTracks.size() + " results";
            audioTracks.forEach(audioTrack -> results.add(audioTrack.getName()));
            user.getSearchBar().setSearchedTracks(audioTracks, type);
        } else {
            message = "Search returned " + clients.size() + " results";
            clients.forEach(client -> results.add(client.getUsername()));
            user.getSearchBar().setSearchedClients(clients, type);
        }

        outputNode.put("message", message);
        outputNode.put("results", objectMapper.valueToTree(results));
        return outputNode;
    }

    private ObjectNode select() {
        User user = Library.getLibrary().getUsers().get(username);
        SearchBar searchBar = user.getSearchBar();
        Page currentPage = user.getCurrentPage();

        searchBar.setSelectedTrack(null);

        if (!searchBar.isSearchConducted()) {
            message = "Please conduct a search before making a selection.";
            outputNode.put("message", message);
            return outputNode;
        }

        if (itemNumber > searchBar.getFoundResults()) {
            message = "The selected ID is too high.";
            outputNode.put("message", message);
            return outputNode;
        }

        switch (searchBar.getSearchType()) {
            case "song", "playlist", "podcast", "album":
                ArrayList<AudioTrack> searchedTracks = searchBar.getSearchedTracks();
                searchBar.setSelectedTrack(searchedTracks.get(itemNumber - 1));
                message = "Successfully selected " + searchBar.getSelectedTrack().getName() + ".";
                break;

            case "artist":
                Artist artist = (Artist) searchBar.getSearchedClients().get((itemNumber - 1));
                currentPage.switchPage(Page.Type.ARTIST, artist, user);
                message = "Successfully selected " + artist.getUsername() + "'s page.";
                break;

            case "host":
                Host host = (Host) searchBar.getSearchedClients().get((itemNumber - 1));
                currentPage.switchPage(Page.Type.HOST, host, user);
                message = "Successfully selected " + host.getUsername() + "'s page.";
                break;

            default:
                break;
        }

        searchBar.clearSearchResults();
        outputNode.put("message", message);
        return outputNode;
    }

    private ArrayList<Playlist> userAccessiblePlaylists() {
        ArrayList<Playlist> userAccessible = new ArrayList<>();

        for (Playlist playlist : Library.getLibrary().getPlaylists()) {
            if (playlist.getOwner().equals(username) || playlist.getVisibility().equals("public")) {
                userAccessible.add(playlist);
            }
        }
        return userAccessible;
    }

    private ArrayList<Album> getAlbumsByArtistOrder() {
        ArrayList<String> artistNames = new ArrayList<>(Library.getLibrary().getArtists().keySet());
        return Library.getLibrary().getAlbums().stream()
                .sorted(Comparator.comparingInt(o -> artistNames.indexOf(o.getOwner())))
                .collect(Collectors.toCollection(ArrayList::new));
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
                .limit(MAX_SEARCH_RESULTS)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<AudioTrack> searchPodcast(final ArrayList<Podcast> podcasts) {
        return podcasts.stream()
                .filter(filter::filterByName)
                .filter(filter::filterByOwner)
                .limit(MAX_SEARCH_RESULTS)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<AudioTrack> searchPlaylist(final ArrayList<Playlist> playlists) {
        return playlists.stream()
                .filter(filter::filterByName)
                .filter(filter::filterByOwner)
                .limit(MAX_SEARCH_RESULTS)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<AudioTrack> searchAlbum(final ArrayList<Album> albums) {
        return albums.stream()
                .filter(filter::filterByName)
                .filter(filter::filterByOwner)
                .filter(filter::filterByDescription)
                .limit(MAX_SEARCH_RESULTS)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<Client> searchArtist(final HashMap<String, Artist> artists) {
        return artists.values().stream()
                .filter(filter::filterByName)
                .limit(MAX_SEARCH_RESULTS)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<Client> searchHost(final HashMap<String, Host> hosts) {
        return hosts.values().stream()
                .filter(filter::filterByName)
                .limit(MAX_SEARCH_RESULTS)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
