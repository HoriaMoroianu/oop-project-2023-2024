package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.audio_collections.AudioFile;
import entities.Library;
import entities.audio_collections.Song;
import entities.User;
import fileio.input.CommandInput;

public final class Like extends Command {
    public Like(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        User user = Library.getLibrary().getUsers().get(username);
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

        Song song = (Song) loadedAudio;

        if (user.getLikedSongs().contains(song)) {
            song.setLikesReceived(song.getLikesReceived() - 1);
            user.getLikedSongs().remove(song);
            message = "Unlike registered successfully.";
        } else {
            song.setLikesReceived(song.getLikesReceived() + 1);
            user.getLikedSongs().add(song);
            message = "Like registered successfully.";
        }
        return new ObjectMapper().valueToTree(this);
    }
}
