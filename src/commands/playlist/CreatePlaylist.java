package commands.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import app.admin.Library;
import app.audio.collections.Playlist;
import app.User;
import commands.Command;
import fileio.input.CommandInput;

public final class CreatePlaylist extends Command {
    private final String playlistName;

    public CreatePlaylist(final CommandInput commandInput) {
        super(commandInput);
        playlistName = commandInput.getPlaylistName();
    }

    @Override
    protected ObjectNode executeCommand() {
        User user = Library.getLibrary().getUsers().get(username);

        for (Playlist playlist : user.getPlaylists()) {
            if (playlist.getName().equals(playlistName)) {
                message = "A playlist with the same name already exists.";
                return new ObjectMapper().valueToTree(this);
            }
        }

        Playlist playlist = new Playlist(playlistName, this.getUsername());
        user.getPlaylists().add(playlist);
        Library.getLibrary().addPlaylist(playlist);

        message = "Playlist created successfully.";
        return new ObjectMapper().valueToTree(this);
    }
}
