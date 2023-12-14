package commands.client;

import app.audio.collections.Album;
import app.audio.collections.Playlist;
import app.audio.files.AudioFile;
import app.audio.files.Song;
import app.clients.Artist;
import app.clients.Client;
import app.management.Library;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;

public final class RemoveAlbum extends Command {
    private final String name;
    public RemoveAlbum(final CommandInput commandInput) {
        super(commandInput);
        name = commandInput.getName();
    }

    @Override
    protected ObjectNode executeCommand() {
        Library.getLibrary().updateMusicPlayers();
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

        if (!artist.getAlbumsNames().contains(name)) {
            message = username + " doesn't have an album with the given name.";
            return new ObjectMapper().valueToTree(this);
        }

        Album albumToDelete = artist.getAlbums().stream()
                .filter(album -> album.getName().equals(name))
                .findFirst().orElse(null);

        if (!safeDelete(albumToDelete)) {
            message = username + " can't delete this album.";
            return new ObjectMapper().valueToTree(this);
        }

        Library.getLibrary().getAlbums().remove(albumToDelete);
        artist.getAlbums().remove(albumToDelete);

        message = username + " deleted the album successfully.";
        return new ObjectMapper().valueToTree(this);
    }

    private boolean safeDelete(final Album album) {
        if (!album.getListeners().isEmpty()) {
            return false;
        }

        for (AudioFile audioFile : album.getSongs()) {
            Song song = (Song) audioFile;
            if (!song.getListeners().isEmpty()) {
                return false;
            }

            for (Playlist playlist : Library.getLibrary().getPlaylists()) {
                if (playlist.getSongs().contains(song)) {
                    return false;
                }
            }
        }

        return true;
    }
}
