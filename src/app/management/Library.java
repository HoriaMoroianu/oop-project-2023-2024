package app.management;

import app.audio.collections.Album;
import app.clients.Artist;
import app.clients.Client;
import app.clients.Host;
import app.clients.User;
import app.audio.collections.Playlist;
import app.audio.collections.Podcast;
import app.audio.files.Song;
import app.clients.services.Merch;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Getter
public final class Library {
    private static Library instance = null;
    @Setter
    private Integer timestamp;

    private final ArrayList<Song> songs = new ArrayList<>();
    private final ArrayList<Podcast> podcasts = new ArrayList<>();
    private final ArrayList<Playlist> playlists = new ArrayList<>();
    private final ArrayList<Album> albums = new ArrayList<>();

    private final LinkedHashMap<String, User> users = new LinkedHashMap<>();
    private final LinkedHashMap<String, Artist> artists = new LinkedHashMap<>();
    private final LinkedHashMap<String, Host> hosts = new LinkedHashMap<>();

    private final ArrayList<String> onlineUsers = new ArrayList<>();
    private final ArrayList<Artist> endProgramArtists = new ArrayList<>();
    private final HashMap<String, Merch> appMerch = new HashMap<>();

    private Library() {
    }

    /**
     * @return Library instance
     */
    public static Library getLibrary() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    /**
     * Clears all data from library
     */
    public void clearLibrary() {
        songs.clear();
        podcasts.clear();
        playlists.clear();
        albums.clear();

        users.clear();
        artists.clear();
        hosts.clear();
        onlineUsers.clear();
        endProgramArtists.clear();
        appMerch.clear();
    }

    /**
     * Load the input library into the database
     * @param libraryInput list containing all input data
     */
    public void loadLibrary(final LibraryInput libraryInput) {
        for (final SongInput songInput : libraryInput.getSongs()) {
            songs.add(new Song(songInput));
        }

        for (final PodcastInput podcastInput : libraryInput.getPodcasts()) {
            podcasts.add(new Podcast(podcastInput));
        }

        for (final UserInput userInput : libraryInput.getUsers()) {
            users.put(userInput.getUsername(), new User(userInput));
        }
    }

    /**
     * Updates all users' players in the application
     */
    public void updateMusicPlayers() {
        for (User user : Library.getLibrary().getUsers().values()) {
            user.getMusicPlayer().updateMusicPlayer();
        }
    }

    /**
     * Search by name a client from the library
     * @param username the name by which the client is searched
     * @return the client with the searched name; null if it was not found
     */
    public Client getClient(final String username) {
        Client client = users.get(username);
        if (client != null) {
            return client;
        }

        client = artists.get(username);
        if (client != null) {
            return client;
        }

        client = hosts.get(username);
        return client;
    }
}
