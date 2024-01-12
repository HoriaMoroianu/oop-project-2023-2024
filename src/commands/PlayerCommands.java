package commands;

import app.audio.collections.Album;
import app.audio.collections.Playlist;
import app.audio.collections.Podcast;
import app.audio.files.AudioFile;
import app.audio.files.Song;
import app.clients.User;
import app.clients.services.MusicPlayer;
import app.clients.services.SearchBar;
import app.management.Library;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

public final class PlayerCommands extends CommandStrategy {
    private User user;
    private MusicPlayer musicPlayer;
    private final Integer seed;
    private final Integer playlistId;

    public PlayerCommands(final CommandInput commandInput) {
        super(commandInput);
        seed = commandInput.getSeed();
        playlistId = commandInput.getPlaylistId();
    }

    @Override
    protected ObjectNode executeCommand() {
        user = Library.getLibrary().getUsers().get(username);
        musicPlayer = user.getMusicPlayer();
        musicPlayer.updateMusicPlayer();

        return switch (command) {
            case "load" -> load();
            case "playPause" -> playPause();
            case "repeat" -> repeat();
            case "shuffle" -> shuffle();
            case "forward" -> forward();
            case "backward" -> backward();
            case "next" -> next();
            case "prev" -> previous();
            case "like" -> like();
            case "addRemoveInPlaylist" -> addRemoveInPlaylist();
            case "status" -> status();
            default -> null;
        };
    }

    private ObjectNode load() {
        SearchBar searchBar = user.getSearchBar();

        if (searchBar.getSelectedTrack() == null) {
            message = "Please select a source before attempting to load.";
            outputNode.put("message", message);
            return outputNode;
        }

        if (searchBar.getSelectedTrack().loadedAudioFiles().isEmpty()) {
            message = "You can't load an empty audio collection!";
            outputNode.put("message", message);
            return outputNode;
        }

        musicPlayer.loadTrack(searchBar.getSelectedTrack());
        searchBar.setSelectedTrack(null);

        message = "Playback loaded successfully.";
        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode playPause() {
        if (musicPlayer.getLoadedTrack() == null) {
            message = "Please load a source before attempting to pause or resume playback.";
            outputNode.put("message", message);
            return outputNode;
        }

        musicPlayer.updatePlayPause();
        message = musicPlayer.isPaused() ? "Playback paused successfully."
                                         : "Playback resumed successfully.";
        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode repeat() {
        if (musicPlayer.getLoadedTrack() == null) {
            message = "Please load a source before setting the repeat status.";
            outputNode.put("message", message);
            return outputNode;
        }

        musicPlayer.changeRepeat();
        message = "Repeat mode changed to " + musicPlayer.getRepeat().toLowerCase() + ".";
        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode shuffle() {
        if (musicPlayer.getLoadedTrack() == null) {
            message = "Please load a source before using the shuffle function.";
            outputNode.put("message", message);
            return outputNode;
        }

        if (musicPlayer.getLoadedTrack().getClass() != Playlist.class
                && musicPlayer.getLoadedTrack().getClass() != Album.class) {
            message = "The loaded source is not a playlist or an album.";
            outputNode.put("message", message);
            return outputNode;
        }

        musicPlayer.updateShuffle(seed);
        message = musicPlayer.isShuffle() ? "Shuffle function activated successfully."
                                          : "Shuffle function deactivated successfully.";
        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode forward() {
        if (musicPlayer.getLoadedTrack() == null) {
            message = "Please load a source before attempting to forward.";
            outputNode.put("message", message);
            return outputNode;
        }

        if (musicPlayer.getLoadedTrack().getClass() != Podcast.class) {
            message = "The loaded source is not a podcast.";
            outputNode.put("message", message);
            return outputNode;
        }

        musicPlayer.skipForward();
        message = "Skipped forward successfully.";
        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode backward() {
        if (musicPlayer.getLoadedTrack() == null) {
            message = "Please select a source before rewinding.";
            outputNode.put("message", message);
            return outputNode;
        }

        if (musicPlayer.getLoadedTrack().getClass() != Podcast.class) {
            message = "The loaded source is not a podcast.";
            outputNode.put("message", message);
            return outputNode;
        }

        musicPlayer.skipBackward();
        message = "Rewound successfully.";
        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode next() {
        if (musicPlayer.getLoadedTrack() == null) {
            message = "Please load a source before skipping to the next track.";
            outputNode.put("message", message);
            return outputNode;
        }

        musicPlayer.nextAudioFile();
        message = (musicPlayer.getLoadedTrack() == null)
                ? "Please load a source before skipping to the next track."
                : "Skipped to next track successfully. "
                + "The current track is " + musicPlayer.getAudioFile().getName() + ".";

        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode previous() {
        if (musicPlayer.getLoadedTrack() == null) {
            message = "Please load a source before returning to the previous track.";
            outputNode.put("message", message);
            return outputNode;
        }

        musicPlayer.previousAudioFile();
        message = (musicPlayer.getLoadedTrack() == null)
                ? "Please load a source before returning to the previous track."
                : "Returned to previous track successfully. "
                + "The current track is " + musicPlayer.getAudioFile().getName() + ".";

        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode like() {
        AudioFile loadedAudio = user.getMusicPlayer().getAudioFile();

        if (user.getMusicPlayer().getLoadedTrack() == null) {
            message = "Please load a source before liking or unliking.";
            outputNode.put("message", message);
            return outputNode;
        }

        if (loadedAudio.getClass() != Song.class) {
            message = "Loaded source is not a song.";
            outputNode.put("message", message);
            return outputNode;
        }

        Song song = (Song) loadedAudio;
        if (user.getLikedSongs().contains(song)) {
            song.dislike();
            user.getLikedSongs().remove(song);
            message = "Unlike registered successfully.";
        } else {
            song.like();
            user.getLikedSongs().add(song);
            message = "Like registered successfully.";
        }

        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode addRemoveInPlaylist() {
        AudioFile loadedAudio = user.getMusicPlayer().getAudioFile();

        if (user.getMusicPlayer().getLoadedTrack() == null) {
            message = "Please load a source before adding to or removing from the playlist.";
            outputNode.put("message", message);
            return outputNode;
        }

        if (loadedAudio.getClass() != Song.class) {
            message = "The loaded source is not a song.";
            outputNode.put("message", message);
            return outputNode;
        }

        if (user.getPlaylists().size() < playlistId) {
            message = "The specified playlist does not exist.";
            outputNode.put("message", message);
            return outputNode;
        }

        Playlist playlist = user.getPlaylists().get(playlistId - 1);
        if (playlist.getSongs().contains(loadedAudio)) {
            playlist.getSongs().remove(loadedAudio);
            message = "Successfully removed from playlist.";
        } else {
            playlist.getSongs().add(loadedAudio);
            message = "Successfully added to playlist.";
        }

        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode status() {
        outputNode.put("stats", objectMapper.valueToTree(musicPlayer));
        return outputNode;
    }
}
