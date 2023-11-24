package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.MusicPlayer;
import fileio.input.CommandInput;

public final class Repeat extends Command {
    public Repeat(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        MusicPlayer musicPlayer = Library.getLibrary().getUsers().get(username).getMusicPlayer();
        musicPlayer.updateMusicPlayer();

        if (musicPlayer.getLoadedTrack() == null) {
            message = "Please load a source before setting the repeat status.";
            return new ObjectMapper().valueToTree(this);
        }

        musicPlayer.changeRepeat();
        message = "Repeat mode changed to " + musicPlayer.getRepeat().toLowerCase() + ".";
        return new ObjectMapper().valueToTree(this);
    }
}
