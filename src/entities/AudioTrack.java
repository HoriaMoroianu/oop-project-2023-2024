package entities;

import java.util.ArrayList;

public interface AudioTrack {
    String getName();
    AudioFile loadAudioFile(Integer watchTime);
    void updateAudioFile(MusicPlayer musicPlayer, int timePassed);
    boolean atFirstAudioFile(MusicPlayer musicPlayer);
    ArrayList<AudioFile> loadAudioList();

    default int simulatePlayQueue(MusicPlayer musicPlayer, ArrayList<AudioFile> audioFiles,
                                  int timePassed) {
        AudioFile currentPlayedFile = musicPlayer.getAudioFile();
        int startIndex = audioFiles.indexOf(currentPlayedFile);
        int timeUntilNextFile = -1;

        for (AudioFile file : audioFiles.subList(startIndex, audioFiles.size())) {
            timeUntilNextFile = (timeUntilNextFile == -1)
                    ? musicPlayer.getRemainedTime()
                    : file.getDuration();

            if (timeUntilNextFile - timePassed > 0) {
                musicPlayer.setAudioFile(file);
                musicPlayer.setName(file.getName());
                musicPlayer.setRemainedTime(timeUntilNextFile - timePassed);
                return timeUntilNextFile - timePassed;
            }
            timePassed -= timeUntilNextFile;
        }
        return timePassed;
    }
}
