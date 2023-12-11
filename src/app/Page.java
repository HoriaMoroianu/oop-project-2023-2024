package app;

import app.audio.collections.Playlist;
import app.audio.files.Song;
import app.clients.User;

import java.util.ArrayList;

public class Page {
    public enum Type {
        HOME,
        LIKED_CONTENT,
        ARTIST,
        HOST
    }

    private Type type;

    public Page(final Type type) {
        this.type = type;
    }

    public String printHomePage(final User user) {
        ArrayList<String> likedSongsNames = new ArrayList<>();
        for (Song song : user.getLikedSongs()) {
            likedSongsNames.add(song.getName());
        }

        ArrayList<String> followedPlaylistsNames = new ArrayList<>();
        for (Playlist playlist : user.getFollowedPlaylists()) {
            followedPlaylistsNames.add(playlist.getName());
        }

        return "Liked songs:\n\t" + likedSongsNames
                + "\n\nFollowed playlists:\n\t" + followedPlaylistsNames;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
