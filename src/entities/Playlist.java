package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;

public class Playlist implements AudioTrack {
    private String name;
    @JsonProperty("songs")
    private ArrayList<String> songsNames = new ArrayList<>();
    @Getter
    private String visibility;
    @Getter
    private Integer followers;

    @Getter @JsonIgnore
    private String owner;
    @Getter @JsonIgnore
    private ArrayList<Song> songs = new ArrayList<>();

    public Playlist(final String name, final String owner) {
        this.name = name;
        this.owner = owner;
        visibility = "public";
        followers = 0;
    }

    @Override
    public AudioFile findAudioFile(final Integer watchTime) {
        return songs.get(0);
    }

    public ArrayList<String> getSongsNames() {
        songs.forEach(song -> songsNames.add(song.getName()));
        return songsNames;
    }

    @Override
    public void updateAudioFile(final MusicPlayer musicPlayer, int timePassed) {
        int currentSongIndex = songs.indexOf((Song) musicPlayer.getAudioFile());
        int timeUntilNext;

        for (; currentSongIndex < songs.size(); currentSongIndex++, timePassed -= timeUntilNext) {
            // Calculate time until next song starts
            timeUntilNext = currentSongIndex == songs.indexOf((Song) musicPlayer.getAudioFile())
                    ? musicPlayer.getRemainedTime() : songs.get(currentSongIndex).getDuration();

            if (timeUntilNext - timePassed > 0) {
                // Keep current song
                musicPlayer.setAudioFile(songs.get(currentSongIndex));
                musicPlayer.setName(songs.get(currentSongIndex).getName());
                musicPlayer.setRemainedTime(timeUntilNext - timePassed);
                return;
            }
        }
        musicPlayer.setRemainedTime(0);
    }

    public String getName() {
        return name;
    }
}
