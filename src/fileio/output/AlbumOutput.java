package fileio.output;

import app.audio.collections.Album;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class AlbumOutput {
    private final String name;
    private final ArrayList<String> songs;

    public AlbumOutput(final Album album) {
        name = album.getName();
        songs = album.getSongsNames();
    }
}
