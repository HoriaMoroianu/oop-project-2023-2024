package entities;

import entities.audio_collections.Playlist;
import entities.audio_collections.Song;
import fileio.input.UserInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class User {
    private final String username;
    private final int age;
    private final String city;

    private final SearchBar searchBar = new SearchBar();
    private final MusicPlayer musicPlayer = new MusicPlayer();

    private final ArrayList<Playlist> playlists = new ArrayList<>();
    private final ArrayList<Song> likedSongs = new ArrayList<>();
    private final ArrayList<Playlist> followedPlaylists = new ArrayList<>();

    public User(final UserInput userInput) {
        username = userInput.getUsername();
        age = userInput.getAge();
        city = userInput.getCity();
    }
}
