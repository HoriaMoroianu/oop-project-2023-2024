package app.clients;

import app.MusicPlayer;
import app.Page;
import app.SearchBar;
import app.audio.collections.Playlist;
import app.audio.files.Song;
import app.management.Library;
import fileio.input.UserInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class User extends Client {
    private final SearchBar searchBar = new SearchBar();
    private final MusicPlayer musicPlayer = new MusicPlayer();

    private final ArrayList<Playlist> playlists = new ArrayList<>();
    private final ArrayList<Song> likedSongs = new ArrayList<>();
    private final ArrayList<Playlist> followedPlaylists = new ArrayList<>();

    private boolean onlineStatus;
    private Page currentPage;

    public User(final UserInput userInput) {
        super(userInput);
        currentPage = new Page(Page.Type.HOME, this);
        switchOnlineStatus();
    }


    /**
     * Changes the online status of this user;
     * if it is online it becomes offline and vice versa
     */
    public void switchOnlineStatus() {
        if (onlineStatus) {
            onlineStatus = false;
            Library.getLibrary().getOnlineUsers().remove(username);
        } else {
            onlineStatus = true;
            Library.getLibrary().getOnlineUsers().add(username);
        }
        musicPlayer.updateMusicPlayer();
        musicPlayer.setOnline(onlineStatus);
    }
}
