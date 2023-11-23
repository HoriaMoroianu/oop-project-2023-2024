package entities;

public interface AudioTrack {
    String getName();
    AudioFile findAudioFile(Integer watchTime);
    void updateAudioFile(MusicPlayer musicPlayer, int timePassed);
}
