package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.MusicPlayer;
import entities.Playlist;
import fileio.input.CommandInput;
import lombok.Getter;

public final class Shuffle extends Command {
    @Getter
    private String message;
    private Integer seed;

    public Shuffle(final CommandInput commandInput) {
        super(commandInput);
        seed = commandInput.getSeed();
    }

    @Override
    protected ObjectNode executeCommand() {
        MusicPlayer musicPlayer =
                Library.getLibrary().getUsers().get(this.getUsername()).getMusicPlayer();

        musicPlayer.updateMusicPlayer();

        if (musicPlayer.getLoadedTrack() == null) {
            message = "Please load a source before using the shuffle function.";
            return new ObjectMapper().valueToTree(this);
        }

        if (musicPlayer.getLoadedTrack().getClass() != Playlist.class) {
            message = "The loaded source is not a playlist.";
            return new ObjectMapper().valueToTree(this);
        }

        message = musicPlayer.isShuffle()
                ? "Shuffle function deactivated successfully."
                : "Shuffle function activated successfully.";

        musicPlayer.updateShuffle(seed);
        return new ObjectMapper().valueToTree(this);
    }
}
