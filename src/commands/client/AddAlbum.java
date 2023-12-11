package commands.client;

import app.audio.collections.Album;
import app.audio.files.Song;
import app.clients.Artist;
import app.clients.Client;
import app.management.Library;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;
import fileio.input.SongInput;

import java.util.ArrayList;

public class AddAlbum extends Command {
    private final String name;
    private final Integer releaseYear;
    private final String description;
    private final ArrayList<SongInput> songs;

    public AddAlbum(final CommandInput commandInput) {
        super(commandInput);
        name = commandInput.getName();
        releaseYear = commandInput.getReleaseYear();
        description = commandInput.getDescription();
        songs = commandInput.getSongs();
    }

    @Override
    protected ObjectNode executeCommand() {
        Client client = Library.getLibrary().getClient(username);
        if (client == null) {
            message = "The username " + username + " doesn't exist.";
            return new ObjectMapper().valueToTree(this);
        }

        if (client.getClass() != Artist.class) {
            message = username + " is not an artist.";
            return new ObjectMapper().valueToTree(this);
        }

        Artist artist = (Artist) client;

        if (artist.getAlbumsNames().contains(name)) {
            message = username + " has another album with the same name.";
            return new ObjectMapper().valueToTree(this);
        }

        ArrayList<Song> albumSongs = new ArrayList<>();
        ArrayList<String> songNames = new ArrayList<>();

        for (SongInput songInput : songs) {
            Song song = new Song(songInput);

            // TODO: trebuie verificat si in library ?
            if (songNames.contains(song.getName())) {
                message = username + " has the same song at least twice in this album.";
                return new ObjectMapper().valueToTree(this);
            }

            albumSongs.add(song);
            songNames.add(song.getName());
        }

        Album album = new Album(name, artist.getUsername(), releaseYear, description);
        album.getSongs().addAll(albumSongs);
        artist.getAlbums().add(album);

        Library.getLibrary().getAlbums().add(album);
        Library.getLibrary().getSongs().addAll(albumSongs);

        message = username + " has added new album successfully.";
        return new ObjectMapper().valueToTree(this);
    }
}
