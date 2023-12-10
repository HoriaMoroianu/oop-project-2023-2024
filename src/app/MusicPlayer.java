package app;

import app.management.Library;
import com.fasterxml.jackson.annotation.JsonIgnore;
import app.audio.files.AudioFile;
import app.audio.collections.AudioTrack;
import app.audio.collections.Playlist;
import app.audio.collections.Podcast;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public final class MusicPlayer {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private Integer remainedTime;
    @Setter
    private Integer repeat;
    @Getter
    private boolean shuffle;
    @Getter
    private boolean paused;

    @Getter @JsonIgnore
    private AudioTrack loadedTrack;
    @Getter @Setter @JsonIgnore
    private AudioFile audioFile;
    @Getter @JsonIgnore
    private Integer seed;
    @Setter
    private boolean online;
    private Integer lastUpdateTime;
    private final int skippTime = 90;
    private final HashMap<String, Integer> podcastHistory = new HashMap<>();

    /**
     * Loads audioTrack into this musicPlayer
     * @param audioTrack the track that is loaded
     */
    public void loadTrack(final AudioTrack audioTrack) {
        savePodcastHistory();

        name = audioTrack.getName();
        loadedTrack = audioTrack;
        audioFile = loadedTrack.loadAudioFile(podcastHistory.get(name));
        remainedTime = audioFile.getDuration(podcastHistory.get(name));

        repeat = 0;
        shuffle = false;
        paused = false;
        lastUpdateTime = Library.getLibrary().getTimestamp();
    }

    /**
     * Removes the current loaded track
     */
    public void removeTrack() {
        savePodcastHistory();

        name = "";
        loadedTrack = null;
        audioFile = null;
        remainedTime = 0;

        repeat = 0;
        shuffle = false;
        paused = true;

        lastUpdateTime = Library.getLibrary().getTimestamp();
    }

    /**
     * Updates the loaded track in this musicPlayer
     */
    public void updateMusicPlayer() {
        if (loadedTrack == null) {
            return;
        }
        int currentTime = Library.getLibrary().getTimestamp();
        int timePassedPlaying = (!paused && online) ? (currentTime - lastUpdateTime) : 0;

        loadedTrack.updateAudioFile(this, timePassedPlaying);

        if (remainedTime == 0) {
            removeTrack();
        }
        lastUpdateTime = currentTime;
    }


    /**
     * Saves in history the view-time of the current loaded track if it's a podcast
     */
    private void savePodcastHistory() {
        if (loadedTrack == null || loadedTrack.getClass() != Podcast.class) {
            return;
        }
        updateMusicPlayer();
        Podcast podcast = (Podcast) loadedTrack;

        // Time from the start of the podcast to the end of the current episode
        int elapsedTime = podcast.getElapsedTime()
                .get(podcast.getEpisodes().indexOf(audioFile));

        // Stores viewing time from the beginning of the podcast until now
        podcastHistory.put(podcast.getName(), elapsedTime - remainedTime);
    }

    /**
     * Simulates the passage of time through the current track
     * @param audioFiles the audio files contained in the current track
     * @param timeSinceUpdate the time passed since the last update
     * @return the remaining time of the current file if we
     *         have simulated up to the current moment;
     *         the time left after reaching the end of the track
     */
    public int simulatePlayQueue(final ArrayList<AudioFile> audioFiles, final int timeSinceUpdate) {
        int startIndex = audioFiles.indexOf(audioFile);
        int timePassed = timeSinceUpdate;
        int timeUntilNextFile = -1;

        for (AudioFile file : audioFiles.subList(startIndex, audioFiles.size())) {
            timeUntilNextFile = (timeUntilNextFile == -1) ? remainedTime : file.getDuration();

            if (timeUntilNextFile - timePassed > 0) {
                audioFile = file;
                name = audioFile.getName();
                remainedTime = timeUntilNextFile - timePassed;
                return timeUntilNextFile - timePassed;
            }
            timePassed -= timeUntilNextFile;
        }
        return timePassed;
    }

    /**
     * Skip to the next audio file considering
     * that all its remaining time has passed.
     */
    public void nextAudioFile() {
        loadedTrack.updateAudioFile(this, remainedTime);
        paused = false;

        if (remainedTime == 0) {
            removeTrack();
        }
        lastUpdateTime = Library.getLibrary().getTimestamp();
    }

    /**
     * Skip at the beginning of this audio
     * file or to the previous one.
     */
    public void previousAudioFile() {
        if (atFirstAudioFile() || audioFile.getDuration() > remainedTime) {
            remainedTime = audioFile.getDuration(); //restart the current file
        } else {
            loadPreviousAudioFile();
        }
        paused = false;
        lastUpdateTime = Library.getLibrary().getTimestamp();
    }

    /**
     * Advances in time with a set skipTime
     */
    public void skipForward() {
        if (remainedTime < skippTime) {
            loadedTrack.updateAudioFile(this, remainedTime); // next file
        } else {
            loadedTrack.updateAudioFile(this, skippTime);
        }
        lastUpdateTime = Library.getLibrary().getTimestamp();
    }

    /**
     * Goes back in time with a set skipTime
     */
    public void skipBackward() {
        if (audioFile.getDuration() - remainedTime < skippTime) {
            remainedTime = audioFile.getDuration(); //restart the current file
        } else {
            remainedTime += skippTime;
        }
        lastUpdateTime = Library.getLibrary().getTimestamp();
    }

    /**
     * Switch the current play/pause state of this music player
     */
    public void updatePlayPause() {
        paused = !paused;
    }

    /**
     * Switch to the next repeat state
     */
    public void changeRepeat() {
        repeat = (repeat != 2) ? repeat + 1 : 0;
    }

    /**
     * Switch the current shuffle state of this music player
     * @param shuffleSeed new seed for shuffling
     */
    public void updateShuffle(final Integer shuffleSeed) {
        shuffle = !shuffle;
        seed = shuffleSeed;
    }

    /**
     * @return coded repeat state
     */
    public String getRepeat() {
        if (repeat == 0) {
            return "No Repeat";
        }
        if (loadedTrack.getClass() == Playlist.class) {
            return (repeat == 1) ? "Repeat All" : "Repeat Current Song";
        } else {
            return (repeat == 1) ? "Repeat Once" : "Repeat Infinite";
        }
    }

    private void loadPreviousAudioFile() {
        ArrayList<AudioFile> playQueue = new ArrayList<>(loadedTrack.loadAudioList());
        if (shuffle) {
            Collections.shuffle(playQueue, new Random(seed));
        }
        audioFile = playQueue.get(playQueue.indexOf(audioFile) - 1);
        remainedTime = audioFile.getDuration();
        name = audioFile.getName();
    }

    private boolean atFirstAudioFile() {
        ArrayList<AudioFile> playQueue = new ArrayList<>(loadedTrack.loadAudioList());
        if (shuffle) {
            Collections.shuffle(playQueue, new Random(seed));
        }
        return audioFile.equals(playQueue.get(0));
    }
}
