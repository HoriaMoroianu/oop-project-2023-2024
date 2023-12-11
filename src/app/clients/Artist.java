package app.clients;

import app.audio.collections.Album;
import fileio.input.UserInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Artist extends Client {
    private final ArrayList<Album> albums = new ArrayList<>();

    public Artist(final UserInput userInput) {
        super(userInput);
    }

    public ArrayList<String> getAlbumsNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Album album : albums) {
            names.add(album.getName());
        }
        return names;
    }
}
