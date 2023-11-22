package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

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

    public void setTrack(final AudioTrack audioTrack) {
        // TODO podcast episode + remained time + scoate melodia curenta

        name = audioTrack.getName();
        loadedTrack = audioTrack;
        audioFile = loadedTrack.findAudioFile();
        remainedTime = audioFile.getDuration();

        repeat = "No Repeat";
        shuffle = false;
        paused = false;

        lastUpdateTime = Library.getLibrary().getTimestamp();
    }

    public void updateMusicPlayer() {
        // TODO ajunge la momentul curent din playlist / podcast
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
        name = "";
        loadedTrack = null;
        audioFile = null;
        remainedTime = 0;

        repeat = "No Repeat";
        shuffle = false;
        paused = true;
    }

    public void updatePlayPause() {
        paused = !paused;
    }
}
