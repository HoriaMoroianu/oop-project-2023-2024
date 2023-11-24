package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.Playlist;
import entities.SearchBar;
import entities.User;
import fileio.input.CommandInput;

public final class FollowPlaylist extends Command {
    private String message;
    public FollowPlaylist(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        User user = Library.getLibrary().getUsers().get(this.getUsername());
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
            playlist.setFollowers(playlist.getFollowers() - 1);
            message = "Playlist unfollowed successfully.";
        } else {
            user.getFollowedPlaylists().add(playlist);
            playlist.setFollowers(playlist.getFollowers() + 1);
            message = "Playlist followed successfully.";
        }
        return new ObjectMapper().valueToTree(this);
    }

    public String getMessage() {
        return message;
    }
}
