package commands.music_player;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import app.management.Library;
import app.MusicPlayer;
import commands.Command;
import fileio.input.CommandInput;

public final class PlayPause extends Command {
    public PlayPause(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public ObjectNode executeCommand() {
        MusicPlayer musicPlayer = Library.getLibrary().getUsers().get(username).getMusicPlayer();
        musicPlayer.updateMusicPlayer();

        if (musicPlayer.getLoadedTrack() == null) {
            message = "Please load a source before attempting to pause or resume playback.";
            return new ObjectMapper().valueToTree(this);
        }

        message = musicPlayer.isPaused()
                ? "Playback resumed successfully."
                : "Playback paused successfully.";

        musicPlayer.updatePlayPause();
        return new ObjectMapper().valueToTree(this);
    }
}
