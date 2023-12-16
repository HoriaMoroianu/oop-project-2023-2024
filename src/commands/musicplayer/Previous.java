package commands.musicplayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import app.management.Library;
import app.clients.services.MusicPlayer;
import commands.Command;
import fileio.input.CommandInput;

public final class Previous extends Command {
    public Previous(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        MusicPlayer musicPlayer = Library.getLibrary().getUsers().get(username).getMusicPlayer();
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
}
