package entities.audio_collections;

import entities.MusicPlayer;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Song implements AudioFile, AudioTrack {
    private final String name;
    private final Integer duration;
    private final String album;
    private final ArrayList<String> tags;
    private final String lyrics;
    private final String genre;
    private final Integer releaseYear;
    private final String artist;
    @Setter
    private Integer likesReceived;

    public Song(final SongInput songInput) {
        name = songInput.getName();
        duration = songInput.getDuration();
        album = songInput.getAlbum();
        tags = songInput.getTags();
        lyrics = songInput.getLyrics();
        genre = songInput.getGenre();
        releaseYear = songInput.getReleaseYear();
        artist = songInput.getArtist();
        likesReceived = 0;
    }

    @Override
    public AudioFile loadAudioFile(final Integer watchTime) {
        return this;
    }

    @Override
    public ArrayList<AudioFile> loadAudioList() {
        return new ArrayList<>(List.of(this));
    }

    @Override
    public void updateAudioFile(final MusicPlayer musicPlayer, final int timePassed) {
        ArrayList<AudioFile> playQueue = new ArrayList<>();
        playQueue.add(this);

        switch (musicPlayer.repeatState()) {
            case 0:
                if (musicPlayer.simulatePlayQueue(playQueue, timePassed)
                        != musicPlayer.getRemainedTime()) {
                    musicPlayer.setRemainedTime(0);
                }
                break;
            case 1:
                if (timePassed > musicPlayer.getRemainedTime()) {
                    musicPlayer.setRepeat(0);
                    playQueue.add(musicPlayer.getAudioFile()); // extra song for repeat once
                }
                musicPlayer.simulatePlayQueue(playQueue, timePassed);
                break;
            case 2:
                int remainedTime = musicPlayer.simulatePlayQueue(playQueue, timePassed);
                if (remainedTime != musicPlayer.getRemainedTime()) {
                    musicPlayer.setRemainedTime(duration - remainedTime % duration);
                }
                break;
            default:
                break;
        }
    }
}
