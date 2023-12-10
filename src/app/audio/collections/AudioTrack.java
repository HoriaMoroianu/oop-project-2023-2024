package app.audio.collections;

import app.MusicPlayer;
import app.audio.files.AudioFile;

import java.util.ArrayList;

public interface AudioTrack {
    /**
     * @return name of this audio track
     */
    String getName();

    /**
     * @param watchTime the watched time from the beginning of current audio track
     * @return the first audio file to be played in musicPlayer
     */
    AudioFile loadAudioFile(Integer watchTime);

    /**
     * @return list of audio files contained in the current track
     */
    ArrayList<AudioFile> loadAudioList();

    /**
     * Loads in the music player the audio file played at this moment
     * @param musicPlayer for updating the audio file
     * @param timePassed since last update
     */
    void updateAudioFile(MusicPlayer musicPlayer, int timePassed);
}
