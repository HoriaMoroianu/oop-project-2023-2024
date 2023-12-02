package commands.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import app.admin.Library;
import app.audio.collections.Playlist;
import app.User;
import commands.Command;
import fileio.input.CommandInput;

public final class SwitchVisibility extends Command {
    private final Integer playlistId;

    public SwitchVisibility(final CommandInput commandInput) {
        super(commandInput);
        playlistId = commandInput.getPlaylistId();
    }

    @Override
    protected ObjectNode executeCommand() {
        User user = Library.getLibrary().getUsers().get(username);

        if (user.getPlaylists().size() < playlistId) {
            message = "The specified playlist ID is too high.";
            return new ObjectMapper().valueToTree(this);
        }

        Playlist playlist = user.getPlaylists().get(playlistId - 1);
        if (playlist.getVisibility().equals("public")) {
            playlist.setVisibility("private");
            message = "Visibility status updated successfully to private.";
        } else {
            playlist.setVisibility("public");
            message = "Visibility status updated successfully to public.";
        }
        return new ObjectMapper().valueToTree(this);
    }
}
