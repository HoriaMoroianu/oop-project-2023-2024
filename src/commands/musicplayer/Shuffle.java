package commands.musicplayer;

import app.audio.collections.Album;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import app.management.Library;
import app.clients.services.MusicPlayer;
import app.audio.collections.Playlist;
import commands.Command;
import fileio.input.CommandInput;

public final class Shuffle extends Command {
    private final Integer seed;

    public Shuffle(final CommandInput commandInput) {
        super(commandInput);
        seed = commandInput.getSeed();
    }

    @Override
    protected ObjectNode executeCommand() {
        MusicPlayer musicPlayer = Library.getLibrary().getUsers().get(username).getMusicPlayer();
        musicPlayer.updateMusicPlayer();

        if (musicPlayer.getLoadedTrack() == null) {
            message = "Please load a source before using the shuffle function.";
            return new ObjectMapper().valueToTree(this);
        }

        if (musicPlayer.getLoadedTrack().getClass() != Playlist.class
            && musicPlayer.getLoadedTrack().getClass() != Album.class) {
            message = "The loaded source is not a playlist or an album.";
            return new ObjectMapper().valueToTree(this);
        }

        message = musicPlayer.isShuffle()
                ? "Shuffle function deactivated successfully."
                : "Shuffle function activated successfully.";

        musicPlayer.updateShuffle(seed);
        return new ObjectMapper().valueToTree(this);
    }
}
