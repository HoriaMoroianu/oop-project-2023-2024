package entities;

import fileio.input.UserInput;
import lombok.Getter;

import java.util.ArrayList;

public class User {
    private String username;
    private int age;
    private String city;

    @Getter
    private SearchBar searchBar = new SearchBar();
    @Getter
    private MusicPlayer musicPlayer = new MusicPlayer();
    @Getter
    private ArrayList<Playlist> playlists = new ArrayList<>();
    @Getter
    private ArrayList<Song> likedSongs = new ArrayList<>();
    @Getter
    private ArrayList<Playlist> followedPlaylists = new ArrayList<>();

    public User(final UserInput userInput) {
        username = userInput.getUsername();
        age = userInput.getAge();
        city = userInput.getCity();
    }
}
