package commands.musicplayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import app.management.Library;
import app.clients.services.MusicPlayer;
import commands.Command;
import fileio.input.CommandInput;

public final class Next extends Command {
    public Next(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        MusicPlayer musicPlayer = Library.getLibrary().getUsers().get(username).getMusicPlayer();
        musicPlayer.updateMusicPlayer();

        if (musicPlayer.getLoadedTrack() == null) {
            message = "Please load a source before skipping to the next track.";
            return new ObjectMapper().valueToTree(this);
        }

        musicPlayer.nextAudioFile();

        message = (musicPlayer.getLoadedTrack() == null)
                ? "Please load a source before skipping to the next track."
                : "Skipped to next track successfully. "
                    + "The current track is " + musicPlayer.getAudioFile().getName() + ".";

        return new ObjectMapper().valueToTree(this);
    }
}
