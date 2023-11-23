package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

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
    private Integer lastUpdateTime;
    private final HashMap<String, Integer> podcastHistory = new HashMap<>();

    public void setTrack(final AudioTrack audioTrack) {
        // TODO podcast episode + remained time + scoate melodia curenta
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
        // TODO ajunge la momentul curent din podcast
        if (loadedTrack == null) {
            return; // Nothing to update
        }

        int currentTime = Library.getLibrary().getTimestamp();
        int timePassedPlaying = !paused ? (currentTime - lastUpdateTime) : 0;

        // TODO audioFile + remainedTime
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

    public void updatePlayPause() {
        paused = !paused;
    }

    private void savePodcastHistory() {
        if (loadedTrack == null || loadedTrack.getClass() != Podcast.class) {
            return;
        }
        updateMusicPlayer(); // TODO verifica daca e corect in urmatoarele teste
        Podcast podcast = (Podcast) loadedTrack;

        // Time from the start of the podcast to the end of the current episode
        int elapsedTime = podcast.getElapsedTime()
                .get(podcast.getEpisodes().indexOf(audioFile));

        // Stores viewing time from the beginning of the podcast until now
        podcastHistory.put(podcast.getName(), elapsedTime - remainedTime);
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

    public Integer repeatState() {
        return repeat;
    }

    public void changeRepeat() {
        repeat++;
        if (repeat > 2) {
            repeat = 0;
        }
    }
}
