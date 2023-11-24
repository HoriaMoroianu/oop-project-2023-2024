package entities.audio_collections;

import entities.MusicPlayer;

import java.util.ArrayList;

public interface AudioTrack {
    String getName();
    AudioFile loadAudioFile(Integer watchTime);
    void updateAudioFile(MusicPlayer musicPlayer, int timePassed);
    ArrayList<AudioFile> loadAudioList();
}
