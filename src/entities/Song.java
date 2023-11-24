package entities;

import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Song implements AudioFile, AudioTrack {
    private String name;
    private Integer duration;
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private Integer releaseYear;
    private String artist;
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
    public void updateAudioFile(final MusicPlayer musicPlayer, final int timePassed) {
        ArrayList<AudioFile> playQueue = new ArrayList<>();
        playQueue.add(this);

        if (musicPlayer.repeatState() == 1 && timePassed > musicPlayer.getRemainedTime()) {
            musicPlayer.setRepeat(0);
            playQueue.add(musicPlayer.getAudioFile());
        }

        switch (musicPlayer.repeatState()) {
            case 0:
                if (simulatePlayQueue(musicPlayer, playQueue, timePassed)
                        != musicPlayer.getRemainedTime()) {
                    musicPlayer.setRemainedTime(0);
                }
                break;
            case 1:
                playQueue.add(this);
                simulatePlayQueue(musicPlayer, playQueue, timePassed);
                break;
            case 2:
                int remainedTime = simulatePlayQueue(musicPlayer, playQueue, timePassed);
                if (remainedTime != musicPlayer.getRemainedTime()) {
                    musicPlayer.setRemainedTime(duration - remainedTime % duration);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean atFirstAudioFile(final MusicPlayer musicPlayer) {
        return true;
    }

    @Override
    public ArrayList<AudioFile> loadAudioList() {
        return new ArrayList<>(List.of(this));
    }
}
