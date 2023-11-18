package entities;

import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;

import java.util.ArrayList;

public final class Database {
    private static Database instance = null;
    private final ArrayList<Song> library = new ArrayList<>();
    private final ArrayList<Podcast> podcasts = new ArrayList<>();
    private final ArrayList<User> users = new ArrayList<>();

    private Database() {
    }

    /**
     * Lazy Singleton Pattern
     */
    public static Database getDatabase() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /**
     * Load the input library into the database
     * @param libraryInput list containing all input data
     */
    public void loadDatabase(final LibraryInput libraryInput) {
        for (final SongInput songInput : libraryInput.getSongs()) {
            this.library.add(new Song(songInput));
        }

        for (final PodcastInput podcastInput : libraryInput.getPodcasts()) {
            this.podcasts.add(new Podcast(podcastInput));
        }

        for (final UserInput userInput : libraryInput.getUsers()) {
            this.users.add(new User(userInput));
        }
    }
}
