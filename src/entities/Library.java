package entities;

import entities.audio_collections.Playlist;
import entities.audio_collections.Podcast;
import entities.audio_collections.Song;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public final class Library {
    private static Library instance = null;
    @Setter
    private Integer timestamp;

    private final ArrayList<Song> songs = new ArrayList<>();
    private final ArrayList<Podcast> podcasts = new ArrayList<>();
    private final ArrayList<Playlist> playlists = new ArrayList<>();
    private final HashMap<String, User> users = new HashMap<>();

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
     * Adds this playlist into the database
     * @param playlist user created playlist
     */
    public void addPlaylist(final Playlist playlist) {
        playlists.add(playlist);
    }

    /**
     * Clears all data from library
     */
    public void clearLibrary() {
        songs.clear();
        podcasts.clear();
        playlists.clear();
        users.clear();
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
}
