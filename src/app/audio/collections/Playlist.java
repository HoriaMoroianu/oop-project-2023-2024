package app.audio.collections;

import app.audio.files.AudioFile;
import app.audio.AudioTrack;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import app.MusicPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

@Getter
public class Playlist implements AudioTrack {
    private final String name;
    @JsonProperty("songs")
    private ArrayList<String> songsNames = new ArrayList<>();
    @Setter
    private String visibility;
    @Setter
    private Integer followers;

    @JsonIgnore
    private final String owner;
    @JsonIgnore
    private final ArrayList<AudioFile> songs = new ArrayList<>();

    public Playlist(final String name, final String owner) {
        this.name = name;
        this.owner = owner;
        visibility = "public";
        followers = 0;
    }

    /**
     * @return a list with the names of the songs in this playlist
     */
    public ArrayList<String> getSongsNames() {
        songsNames.clear();
        songs.forEach(song -> songsNames.add(song.getName()));
        return songsNames;
    }

    /**
     * @param watchTime the watched time from the beginning of current audio track
     * @return the first song as the first audio file for current track
     */
    @Override
    public AudioFile loadAudioFile(final Integer watchTime) {
        return songs.get(0);
    }

    /**
     * @return song list as the audio file list of current track
     */
    @Override
    public ArrayList<AudioFile> loadAudioList() {
        return songs;
    }

    /**
     * Updates the audio file in the music player
     * taking into account the repeat and shuffle status.
     * @param musicPlayer for updating the audio file
     * @param timePassed  since last update
     */
    @Override
    public void updateAudioFile(final MusicPlayer musicPlayer, final int timePassed) {
        ArrayList<AudioFile> playQueue = new ArrayList<>(songs);

        if (musicPlayer.isShuffle()) {
            Collections.shuffle(playQueue, new Random(musicPlayer.getSeed()));
        }

        switch (musicPlayer.getRepeat()) {
            case "No Repeat":
                if (musicPlayer.simulatePlayQueue(playQueue, timePassed)
                        != musicPlayer.getRemainedTime()) {
                    // Reached the end of playQueue after the simulation
                    musicPlayer.setRemainedTime(0);
                }
                break;
            case "Repeat All":
                int remainedTime = musicPlayer.simulatePlayQueue(playQueue, timePassed);
                while (remainedTime != musicPlayer.getRemainedTime()) {
                    // Reached the end of playQueue after the simulation -> simulate again
                    musicPlayer.setAudioFile(playQueue.get(0));
                    musicPlayer.setRemainedTime(playQueue.get(0).getDuration());
                    remainedTime = musicPlayer.simulatePlayQueue(playQueue, remainedTime);
                }
                break;
            case "Repeat Current Song":
                // Current song is added to the playQueue once more
                playQueue.add(playQueue.indexOf(musicPlayer.getAudioFile()),
                        musicPlayer.getAudioFile());
                musicPlayer.simulatePlayQueue(playQueue, timePassed);
                break;
            default:
                break;
        }
    }
}
