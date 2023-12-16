package commands.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import app.management.Library;
import app.audio.collections.Playlist;
import app.clients.services.SearchBar;
import app.clients.User;
import commands.Command;
import fileio.input.CommandInput;

public final class FollowPlaylist extends Command {
    public FollowPlaylist(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        User user = Library.getLibrary().getUsers().get(username);
        SearchBar searchBar = user.getSearchBar();

        if (searchBar.getSelectedTrack() == null) {
            message = "Please select a source before following or unfollowing.";
            return new ObjectMapper().valueToTree(this);
        }
        if (searchBar.getSelectedTrack().getClass() != Playlist.class) {
            message = "The selected source is not a playlist.";
            return new ObjectMapper().valueToTree(this);
        }

        Playlist playlist = (Playlist) searchBar.getSelectedTrack();

        if (playlist.getOwner().equals(this.getUsername())) {
            message = "You cannot follow or unfollow your own playlist.";
            return new ObjectMapper().valueToTree(this);
        }

        if (user.getFollowedPlaylists().contains(playlist)) {
            user.getFollowedPlaylists().remove(playlist);
            playlist.unfollow();
            message = "Playlist unfollowed successfully.";
        } else {
            user.getFollowedPlaylists().add(playlist);
            playlist.follow();
            message = "Playlist followed successfully.";
        }
        return new ObjectMapper().valueToTree(this);
    }
}
