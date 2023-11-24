package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Integer lastUpdateTime;
    private final int skippTime = 90;
    private final HashMap<String, Integer> podcastHistory = new HashMap<>();

    public void setTrack(final AudioTrack audioTrack) {
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

    public void updateMusicPlayer() {
        if (loadedTrack == null) {
            return; // Nothing to update
        }
        int currentTime = Library.getLibrary().getTimestamp();
        int timePassedPlaying = !paused ? (currentTime - lastUpdateTime) : 0;

        loadedTrack.updateAudioFile(this, timePassedPlaying);

        if (remainedTime == 0) {
            removeTrack();
        }
        lastUpdateTime = currentTime;
    }

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

    public void updatePlayPause() {
        paused = !paused;
    }

    public void updateShuffle(final Integer shuffleSeed) {
        shuffle = !shuffle;
        seed = shuffleSeed;
    }

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

    // TODO se presupune ca s-a dat update inainte
    // TODO verifica paused
    public void nextAudioFile() {
        loadedTrack.updateAudioFile(this, remainedTime);
        paused = false;

        if (remainedTime == 0) {
            removeTrack();
        }

        lastUpdateTime = Library.getLibrary().getTimestamp();
    }

    public void previousAudioFile() {
        if (loadedTrack.atFirstAudioFile(this)
                || audioFile.getDuration() > remainedTime) {
            remainedTime = audioFile.getDuration();
        } else {
            loadPreviousAudioFile();
        }
        paused = false;
        lastUpdateTime = Library.getLibrary().getTimestamp();
    }

    public Integer repeatState() {
        return repeat;
    }

    public void changeRepeat() {
        repeat = (repeat != 2) ? repeat + 1 : 0;
    }

    public void skippForward() {
        if (remainedTime < skippTime) {
            loadedTrack.updateAudioFile(this, remainedTime);
        } else {
            loadedTrack.updateAudioFile(this, skippTime);
        }
        lastUpdateTime = Library.getLibrary().getTimestamp();
    }

    public void skippBackward() {
        if (audioFile.getDuration() - remainedTime < skippTime) {
            remainedTime = audioFile.getDuration();
        } else {
            remainedTime += skippTime;
        }
        lastUpdateTime = Library.getLibrary().getTimestamp();
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
}
