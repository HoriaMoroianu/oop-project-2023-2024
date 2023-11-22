package entities;

public interface AudioTrack {
    String getName();
    AudioFile findAudioFile();
    void updateAudioFile(MusicPlayer musicPlayer, int timePassed);
}
