package entities;

import fileio.input.LibraryInput;
import fileio.input.SongInput;

import java.util.ArrayList;

public final class Database {
    private static Database instance = null;
    private final ArrayList<Song> library = new ArrayList<>();
    private final ArrayList<Podcast> podcasts = new ArrayList<>();
    private final ArrayList<User> users = new ArrayList<>();

    private Database() {
    }

    public static Database getDatabase() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void loadDatabase(LibraryInput input) {
        for (final SongInput songInput : input.getSongs()) {
            this.library.add(new Song(songInput));
        }
    }
}
