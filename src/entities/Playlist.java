package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.*;

@Getter
public class Playlist implements AudioTrack {
    private String name;

    @JsonProperty("songs")
    private ArrayList<String> songsNames = new ArrayList<>();

    private String visibility;
    private Integer followers;

    @JsonIgnore
    private String owner;
    @JsonIgnore
    private ArrayList<AudioFile> songs = new ArrayList<>();

    public Playlist(final String name, final String owner) {
        this.name = name;
        this.owner = owner;
        visibility = "public";
        followers = 0;
    }

    @Override
    public AudioFile loadAudioFile(final Integer watchTime) {
        return songs.get(0);
    }

    public ArrayList<String> getSongsNames() {
        songs.forEach(song -> songsNames.add(song.getName()));
        return songsNames;
    }

    @Override
    public void updateAudioFile(final MusicPlayer musicPlayer, final int timePassed) {
        ArrayList<AudioFile> playQueue = new ArrayList<>(songs);

        if (musicPlayer.isShuffle()) {
            Collections.shuffle(playQueue, new Random(musicPlayer.getSeed()));
        }

        switch (musicPlayer.repeatState()) {
            case 0:
                simulatePlayQueue(musicPlayer, playQueue, timePassed);
                break;
            case 1:
                int remainedTime = simulatePlayQueue(musicPlayer, playQueue, timePassed);
                while (remainedTime != 0) {
                    musicPlayer.setAudioFile(songs.get(0));
                    musicPlayer.setRemainedTime(songs.get(0).getDuration());
                    remainedTime = simulatePlayQueue(musicPlayer, songs, remainedTime);
                }
                break;
            case 2:
                playQueue.add(songs.indexOf(musicPlayer.getAudioFile()),
                        musicPlayer.getAudioFile());
                simulatePlayQueue(musicPlayer, playQueue, timePassed);
                break;
            default:
                break;
        }
    }
}
