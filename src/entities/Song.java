package entities;

import fileio.input.SongInput;
import lombok.Getter;

import java.util.ArrayList;

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

    public Song(final SongInput songInput) {
        name = songInput.getName();
        duration = songInput.getDuration();
        album = songInput.getAlbum();
        tags = songInput.getTags();
        lyrics = songInput.getLyrics();
        genre = songInput.getGenre();
        releaseYear = songInput.getReleaseYear();
        artist = songInput.getArtist();
    }

    @Override
    public AudioFile findAudioFile(final Integer watchTime) {
        return this;
    }

    @Override
    public void updateAudioFile(final MusicPlayer musicPlayer, final int timePassed) {
        // TODO repeat + shuffle
        int remainedTime = Math.max(musicPlayer.getRemainedTime() - timePassed, 0);

        musicPlayer.setAudioFile(this);
        musicPlayer.setName(this.name);
        musicPlayer.setRemainedTime(remainedTime);
    }

    @Override
    public Integer getDuration(final Integer watchTime) {
        return getDuration();
    }
}
