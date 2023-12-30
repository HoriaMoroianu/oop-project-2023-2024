package app.clients;

import app.clients.services.MusicPlayer;
import app.clients.services.Page;
import app.clients.services.SearchBar;
import app.audio.collections.Playlist;
import app.audio.files.Song;
import app.management.Library;
import fileio.input.UserInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class User extends Client {
    private final SearchBar searchBar = new SearchBar();
    private final MusicPlayer musicPlayer = new MusicPlayer(this);

    private final ArrayList<Playlist> playlists = new ArrayList<>();
    // TODO can only use song name?
    private final ArrayList<Song> likedSongs = new ArrayList<>();
    private final ArrayList<Playlist> followedPlaylists = new ArrayList<>();

    private boolean onlineStatus;
    private final Page currentPage;

    public User(final UserInput userInput) {
        super(userInput);
        currentPage = new Page(Page.Type.HOME, this);
        switchOnlineStatus();
    }

    /**
     * Deletes this user and all his entries from the application
     */
    @Override
    public void deleteClient() {
        musicPlayer.removeTrack();
        currentPage.switchPage(Page.Type.HOST, this, this);

        followedPlaylists.forEach(Playlist::unfollow);
        likedSongs.forEach(Song::dislike);

        Library.getLibrary().getUsers().values()
                .forEach(user -> playlists.forEach(user.followedPlaylists::remove));

        Library.getLibrary().getPlaylists().removeAll(playlists);
        Library.getLibrary().getUsers().remove(username);
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
