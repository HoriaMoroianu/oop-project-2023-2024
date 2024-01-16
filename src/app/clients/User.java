package app.clients;

import app.audio.collections.AudioTrack;
import app.audio.collections.Podcast;
import app.audio.files.AudioFile;
import app.clients.services.*;
import app.audio.collections.Playlist;
import app.audio.files.Song;
import app.management.Library;
import fileio.input.UserInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Getter
public class User extends Client {
    private final SearchBar searchBar = new SearchBar();
    private final MusicPlayer musicPlayer = new MusicPlayer(this);

    private final ArrayList<Song> likedSongs = new ArrayList<>();
    private final ArrayList<Playlist> playlists = new ArrayList<>();

    private final ArrayList<Playlist> followedPlaylists = new ArrayList<>();
    private final ArrayList<Client> followedCreators = new ArrayList<>();

    private final ArrayList<String> ownedMerch = new ArrayList<>();
    private final ArrayList<HashMap<String, String>> notifications = new ArrayList<>();

    private boolean onlineStatus;
    private final Page currentPage;

    public User(final UserInput userInput) {
        super(userInput);
        currentPage = new Page(Page.Type.HOME, this);
        switchOnlineStatus();
    }

    @Override
    public String getType() {
        return "user";
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

    public void listenAudioFile(final AudioTrack audioTrack, final AudioFile audioFile) {
        // TODO host + artist functions

        if (audioFile.getType().equals("episode")) {
            clientStats.addListen(ClientStats.ListenType.EPISODE, audioFile.getName());

            Host host = Library.getLibrary().getHosts().get(((Podcast) audioTrack).getOwner());
            if (host != null) {
                host.getClientStats().addListen(ClientStats.ListenType.EPISODE, audioFile.getName());
                host.getClientStats().addListen(ClientStats.ListenType.LISTENER, username);
            }
            return;
        }

        Song song = (Song) audioFile;
        clientStats.addListen(ClientStats.ListenType.ARTIST, song.getArtist());
        clientStats.addListen(ClientStats.ListenType.GENRE, song.getGenre());
        clientStats.addListen(ClientStats.ListenType.SONG, song.getName());
        clientStats.addListen(ClientStats.ListenType.ALBUM, song.getAlbum());

        Artist artist = Library.getLibrary().getArtists().get(song.getArtist());
        Library.getLibrary().getEndProgramArtists().add(artist);

        artist.getClientStats().addListen(ClientStats.ListenType.ALBUM, song.getAlbum());
        artist.getClientStats().addListen(ClientStats.ListenType.SONG, song.getName());
        artist.getClientStats().addListen(ClientStats.ListenType.LISTENER, username);
    }

    public void updateNotifications(final String type, final String creatorName) {
        HashMap<String, String> notification = new HashMap<>();
        notification.put(type, type + " from " + creatorName + ".");
        notifications.add(notification);
    }
}
