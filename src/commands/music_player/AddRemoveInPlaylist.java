package commands.music_player;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import app.audio.AudioFile;
import app.admin.Library;
import app.audio.collections.Playlist;
import app.audio.files.Song;
import app.User;
import commands.Command;
import fileio.input.CommandInput;

public final class AddRemoveInPlaylist extends Command {
    private final int playlistId;

    public AddRemoveInPlaylist(final CommandInput commandInput) {
        super(commandInput);
        playlistId = commandInput.getPlaylistId();
    }

    @Override
    protected ObjectNode executeCommand() {
        User user = Library.getLibrary().getUsers().get(username);
        user.getMusicPlayer().updateMusicPlayer();
        AudioFile loadedAudio = user.getMusicPlayer().getAudioFile();

        if (user.getMusicPlayer().getLoadedTrack() == null) {
            message = "Please load a source before adding to or removing from the playlist.";
            return new ObjectMapper().valueToTree(this);
        }
        if (loadedAudio.getClass() != Song.class) {
            message = "The loaded source is not a song.";
            return new ObjectMapper().valueToTree(this);
        }
        if (user.getPlaylists().size() < playlistId) {
            message = "The specified playlist does not exist.";
            return new ObjectMapper().valueToTree(this);
        }

        Playlist playlist = user.getPlaylists().get(playlistId - 1);

        if (playlist.getSongs().contains(loadedAudio)) {
            playlist.getSongs().remove(loadedAudio);
            message = "Successfully removed from playlist.";
        } else {
            playlist.getSongs().add(loadedAudio);
            message = "Successfully added to playlist.";
        }
        return new ObjectMapper().valueToTree(this);
    }
}
