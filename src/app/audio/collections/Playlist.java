package app.audio.collections;

import app.audio.files.AudioFile;
import app.audio.files.Song;
import app.clients.Client;
import app.management.Library;
import app.clients.services.MusicPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

@Getter
public class Playlist implements AudioTrack {
    protected final String name;
    protected ArrayList<String> songsNames = new ArrayList<>();

    @Setter
    private String visibility;
    private Integer followers;

    protected final String owner;
    protected final ArrayList<AudioFile> songs = new ArrayList<>();

    public Playlist(final String name, final String owner) {
        this.name = name;
        this.owner = owner;
        visibility = "public";
        followers = 0;
    }

    /**
     * @return type of this audio track
     */
    @Override
    public String getType() {
        return "playlist";
    }

    /**
     * Increments the number of followers of this playlist
     */
    public void follow() {
        followers++;
    }

    /**
     * Decrements the number of followers of this playlist
     */
    public void unfollow() {
        followers--;
    }

    /**
     * @return the sum of all the likes received for this playlist
     */
    public Integer likesReceived() {
        return songs.stream().map(audioFile -> (Song) audioFile)
                .mapToInt(Song::getLikesReceived).sum();
    }

    /**
     * Updates the guest list of the owner of this playlist and
     * the guest list of the artists of the songs in it
     * @param mode  for setting the list update mode - add/remove guest
     * @param guest that interacts with the content
     */
    @Override
    public void updateClientGuests(final Client.GuestMode mode, final Client guest) {
        Client playlistOwner = Library.getLibrary().getUsers().get(owner);
        if (playlistOwner != null) {
            playlistOwner.updateGuests(mode, guest);

            songs.stream().map(audioFile -> (Song) audioFile)
                    .forEach(song -> song.updateClientGuests(mode, guest));
        }
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
    public ArrayList<AudioFile> loadedAudioFiles() {
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
        int remainedTime = 0;

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
                remainedTime = musicPlayer.simulatePlayQueue(playQueue, timePassed);
                while (remainedTime != musicPlayer.getRemainedTime()) {
                    // Reached the end of playQueue after the simulation -> simulate again
                    musicPlayer.setAudioFile(playQueue.get(0));
                    musicPlayer.setRemainedTime(playQueue.get(0).getDuration());
                    remainedTime = musicPlayer.simulatePlayQueue(playQueue, remainedTime);
                }
                break;
            case "Repeat Current Song":
                remainedTime = musicPlayer.getRemainedTime();
                int loadedFileDuration = musicPlayer.getAudioFile().getDuration();

                if (timePassed == remainedTime) {
                    musicPlayer.setRemainedTime(loadedFileDuration);
                    break;
                }

                if (timePassed < remainedTime) {
                    musicPlayer.setRemainedTime(remainedTime - timePassed);
                    break;
                }

                remainedTime = (timePassed - remainedTime) % loadedFileDuration;
                musicPlayer.setRemainedTime(loadedFileDuration - remainedTime);
                break;
            default:
                break;
        }
    }
}
