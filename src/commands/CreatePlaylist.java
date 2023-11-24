package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.Playlist;
import entities.User;
import fileio.input.CommandInput;
import lombok.Getter;

public final class CreatePlaylist extends Command {

    private String playlistName;
    @Getter
    private String message;

    public CreatePlaylist(final CommandInput commandInput) {
        super(commandInput);
        playlistName = commandInput.getPlaylistName();
    }

    @Override
    protected ObjectNode executeCommand() {
        User user = Library.getLibrary().getUsers().get(this.getUsername());

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
