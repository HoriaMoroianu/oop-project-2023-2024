package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.MusicPlayer;
import fileio.input.CommandInput;

public final class Previous extends Command {
    private String message;
    public Previous(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        MusicPlayer musicPlayer =
                Library.getLibrary().getUsers().get(this.getUsername()).getMusicPlayer();
        musicPlayer.updateMusicPlayer();

        if (musicPlayer.getLoadedTrack() == null) {
            message = "Please load a source before returning to the previous track.";
            return new ObjectMapper().valueToTree(this);
        }

        musicPlayer.previousAudioFile();
        message = (musicPlayer.getLoadedTrack() == null)
                ? "Please load a source before returning to the previous track."
                : "Returned to previous track successfully. "
                + "The current track is " + musicPlayer.getAudioFile().getName() + ".";

        return new ObjectMapper().valueToTree(this);
    }

    public String getMessage() {
        return message;
    }
}
