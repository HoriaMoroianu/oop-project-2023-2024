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
    @Getter
    private String repeat;
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
        audioFile = loadedTrack.findAudioFile(podcastHistory.get(name));
        remainedTime = audioFile.getDuration(podcastHistory.get(name));

        repeat = "No Repeat";
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

        repeat = "No Repeat";
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
                .get(podcast.getEpisodes().indexOf((Episode) audioFile));

        // Stores viewing time from the beginning of the podcast until now
        podcastHistory.put(podcast.getName(), elapsedTime - remainedTime);
    }
}
