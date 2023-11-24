package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

@Getter
public class Playlist implements AudioTrack {
    private String name;

    @JsonProperty("songs")
    private ArrayList<String> songsNames = new ArrayList<>();

    @Setter
    private String visibility;
    @Setter
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
        songsNames.clear();
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
                if (simulatePlayQueue(musicPlayer, playQueue, timePassed)
                        != musicPlayer.getRemainedTime()) {
                    musicPlayer.setRemainedTime(0);
                }
                break;
            case 1:
                int remainedTime = simulatePlayQueue(musicPlayer, playQueue, timePassed);
                while (remainedTime != musicPlayer.getRemainedTime()) {
                    musicPlayer.setAudioFile(playQueue.get(0));
                    musicPlayer.setRemainedTime(playQueue.get(0).getDuration());
                    remainedTime = simulatePlayQueue(musicPlayer, playQueue, remainedTime);
                }
                break;
            case 2:
                playQueue.add(playQueue.indexOf(musicPlayer.getAudioFile()),
                        musicPlayer.getAudioFile());
                simulatePlayQueue(musicPlayer, playQueue, timePassed);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean atFirstAudioFile(final MusicPlayer musicPlayer) {
        ArrayList<AudioFile> playQueue = new ArrayList<>(songs);
        if (musicPlayer.isShuffle()) {
            Collections.shuffle(playQueue, new Random(musicPlayer.getSeed()));
        }
        return musicPlayer.getAudioFile().equals(playQueue.get(0));
    }

    @Override
    public ArrayList<AudioFile> loadAudioList() {
        return songs;
    }
}
