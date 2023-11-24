package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.MusicPlayer;
import entities.audio_collections.Podcast;
import fileio.input.CommandInput;

public final class ForwardBackward extends Command {
    public ForwardBackward(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        MusicPlayer musicPlayer = Library.getLibrary().getUsers().get(username).getMusicPlayer();
        musicPlayer.updateMusicPlayer();

        if (musicPlayer.getLoadedTrack() == null) {
            message = command.equals("forward")
                    ? "Please load a source before attempting to forward."
                    : "Please select a source before rewinding.";
            return new ObjectMapper().valueToTree(this);
        }

        if (musicPlayer.getLoadedTrack().getClass() != Podcast.class) {
            message = "The loaded source is not a podcast.";
            return new ObjectMapper().valueToTree(this);
        }

        switch (command) {
            case "forward":
                musicPlayer.skippForward();
                message = "Skipped forward successfully.";
                break;
            case "backward":
                musicPlayer.skippBackward();
                message = "Rewound successfully.";
                break;
            default:
                break;
        }
        return new ObjectMapper().valueToTree(this);
    }
}
