package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.AudioFile;
import entities.Library;
import entities.Song;
import entities.User;
import fileio.input.CommandInput;
import lombok.Getter;

public final class Like extends Command {
    @Getter
    private String message;

    public Like(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        User user = Library.getLibrary().getUsers().get(this.getUsername());
        user.getMusicPlayer().updateMusicPlayer();
        AudioFile loadedAudio = user.getMusicPlayer().getAudioFile();

        if (user.getMusicPlayer().getLoadedTrack() == null) {
            message = "Please load a source before liking or unliking.";
            return new ObjectMapper().valueToTree(this);
        }
        if (loadedAudio.getClass() != Song.class) {
            message = "Loaded source is not a song.";
            return new ObjectMapper().valueToTree(this);
        }

        if (user.getLikedSongs().contains(loadedAudio)) {
            user.getLikedSongs().remove(loadedAudio);
            message = "Unlike registered successfully.";
        } else {
            user.getLikedSongs().add((Song) loadedAudio);
            message = "Like registered successfully.";
        }
        return new ObjectMapper().valueToTree(this);
    }
}
