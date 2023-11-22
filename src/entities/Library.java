package entities;

import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public final class Library {
    private static Library instance = null;
    private Integer timestamp;

    private final ArrayList<Song> songs = new ArrayList<>();
    private final ArrayList<Podcast> podcasts = new ArrayList<>();
    private final ArrayList<Playlist> playlists = new ArrayList<>();
    private final HashMap<String, User> users = new HashMap<>();

    /**
     * Lazy Singleton Pattern
     */
    public static Library getLibrary() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
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

    public void clearLibrary() {
        songs.clear();
        podcasts.clear();
        playlists.clear();
        users.clear();
    }

    public void setTimestamp(final Integer timestamp) {
        this.timestamp = timestamp;
    }
}
