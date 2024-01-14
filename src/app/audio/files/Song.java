package app.audio.files;

import app.clients.services.MusicPlayer;
import app.audio.collections.AudioTrack;
import app.clients.Client;
import app.management.Library;
import fileio.input.SongInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Song extends AudioFile implements AudioTrack {
    private final String album;
    private final ArrayList<String> tags;
    private final String lyrics;
    private final String genre;
    private final Integer releaseYear;
    private final String artist;
    private Integer likesReceived;

    private final ArrayList<Client> listeners = new ArrayList<>();

    public Song(final SongInput songInput) {
        super(songInput.getName(), songInput.getDuration());
        album = songInput.getAlbum();
        tags = songInput.getTags();
        lyrics = songInput.getLyrics();
        genre = songInput.getGenre();
        releaseYear = songInput.getReleaseYear();
        artist = songInput.getArtist();
        likesReceived = 0;
    }

    /**
     * @return type of this audio track
     */
    @Override
    public String getType() {
        return "song";
    }

    /**
     * Increments the number of likes received
     */
    public void like() {
        likesReceived++;
    }

    /**
     * Decrements the number of likes received
     */
    public void dislike() {
        likesReceived--;
    }

    /**
     * Updates the guest list of the artist of this song and its listeners
     * @param mode  for setting the list update mode - add/remove guest
     * @param guest that interacts with the content
     */
    @Override
    public void updateClientGuests(final Client.GuestMode mode, final Client guest) {
        Client songArtist = Library.getLibrary().getArtists().get(artist);
        if (songArtist != null) {
            songArtist.updateGuests(mode, guest);

            switch (mode) {
                case ADD_GUEST -> listeners.add(guest);
                case REMOVE_GUEST -> listeners.remove(guest);
                default -> { }
            }
        }
    }

    /**
     * @param watchTime the watched time from the beginning of current audio track
     * @return this song as the first audio file for current track
     */
    @Override
    public AudioFile loadAudioFile(final Integer watchTime) {
        return this;
    }

    /**
     * @return this song as the audio file list of current track
     */
    @Override
    public ArrayList<AudioFile> loadedAudioFiles() {
        return new ArrayList<>(List.of(this));
    }

    /**
     * Updates the audio file in the music player
     * taking into account the repeat status.
     * @param musicPlayer for updating the audio file
     * @param timePassed  since last update
     */
    @Override
    public void updateAudioFile(final MusicPlayer musicPlayer, final int timePassed) {
        ArrayList<AudioFile> playQueue = new ArrayList<>();
        playQueue.add(this);

        switch (musicPlayer.getRepeat()) {
            case "No Repeat":
                if (musicPlayer.simulatePlayQueue(playQueue, timePassed)
                        != musicPlayer.getRemainedTime()) {
                    // Reached the end of playQueue after the simulation
                    musicPlayer.setRemainedTime(0);
                }
                break;
            case "Repeat Once":
                if (timePassed > musicPlayer.getRemainedTime()) {
                    musicPlayer.setRepeat(0);
                    // Add extra song for repeat once
                    playQueue.add(musicPlayer.getAudioFile());
                }
                musicPlayer.simulatePlayQueue(playQueue, timePassed);
                break;
            case "Repeat Infinite":
                int remainedTime = musicPlayer.simulatePlayQueue(playQueue, timePassed);
                if (remainedTime != musicPlayer.getRemainedTime()) {
                    // Reached the end of playQueue after the simulation
                    musicPlayer.setRemainedTime(duration - (remainedTime % duration));
                }
                break;
            default:
                break;
        }
    }
}
