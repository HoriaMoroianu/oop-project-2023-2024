package entities;

public final class MusicPlayer {
    private String name;
    private Integer remainedTime;
    private String repeat;
    private boolean shuffle;
    private boolean paused;
    private AudioPlayable playedTrack;

    public void setTrack(final AudioPlayable audioPlayable) {
        // TODO podcast episode + remained time

        playedTrack = audioPlayable;
        name = audioPlayable.getName();
        repeat = "No Repeat";
        shuffle = false;
        paused = false;
    }
}
