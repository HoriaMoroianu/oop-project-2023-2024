package entities;

import fileio.input.UserInput;
import lombok.Getter;

public class User {
    private String username;
    private int age;
    private String city;
    @Getter
    private SearchBar searchBar;
    private MusicPlayer musicPlayer;

    public User(final UserInput userInput) {
        username = userInput.getUsername();
        age = userInput.getAge();
        city = userInput.getCity();
        searchBar = new SearchBar();
        musicPlayer = new MusicPlayer();
    }
}
