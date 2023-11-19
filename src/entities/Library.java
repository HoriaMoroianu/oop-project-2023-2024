package entities;

import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;

import java.util.ArrayList;

public final class Library {
    private static Library instance = null;
    private final ArrayList<Song> songs = new ArrayList<>();
    private final ArrayList<Podcast> podcasts = new ArrayList<>();
    private final ArrayList<User> users = new ArrayList<>();

    private Library() {
    }

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
            users.add(new User(userInput));
        }
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public ArrayList<Podcast> getPodcasts() {
        return podcasts;
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
