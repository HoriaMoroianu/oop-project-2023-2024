package fileio.output;

import app.audio.collections.Playlist;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class PlaylistOutput {
    private final String name;
    private final ArrayList<String> songs;
    private final String visibility;
    private final Integer followers;

    public PlaylistOutput(final Playlist playlist) {
        name = playlist.getName();
        songs = playlist.getSongsNames();
        visibility = playlist.getVisibility();
        followers = playlist.getFollowers();
    }
}
