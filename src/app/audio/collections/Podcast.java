package app.audio.collections;

import app.clients.services.MusicPlayer;
import app.audio.files.AudioFile;
import app.audio.files.Episode;
import app.clients.Client;
import app.management.Library;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import fileio.input.PodcastInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter @JsonIgnoreProperties({"owner", "elapsedTime", "listeners"})
public class Podcast implements AudioTrack {
    private final String name;
    private final String owner;

    @JsonIgnore
    private final ArrayList<AudioFile> episodes = new ArrayList<>();
    @JsonProperty("episodes")
    private final ArrayList<String> episodeNames = new ArrayList<>();

    private final ArrayList<Integer> elapsedTime = new ArrayList<>();
    private final ArrayList<Client> listeners = new ArrayList<>();

    public Podcast(final PodcastInput podcastInput) {
        name = podcastInput.getName();
        owner = podcastInput.getOwner();
        podcastInput.getEpisodes().stream().map(Episode::new).forEach(episodes::add);
        calculateElapsedTime();
    }

    public Podcast(final String name, final String owner, final ArrayList<Episode> episodeInput) {
        this.name = name;
        this.owner = owner;
        episodes.addAll(episodeInput);
        calculateElapsedTime();
    }

    @Override
    public String getType() {
        return "podcast";
    }

    /**
     * @return array list containing the names of episodes of this podcast
     */
    public ArrayList<String> getEpisodeNames() {
        ArrayList<String> names = new ArrayList<>();
        for (AudioFile episode : episodes) {
            names.add(episode.getName());
        }
        return names;
    }

    /**
     * Calculates an array of partial sums with the
     * viewing time required to reach a certain episode
     */
    private void calculateElapsedTime() {
        int playtime = 0;
        for (AudioFile episode : episodes) {
            playtime += episode.getDuration();
            elapsedTime.add(playtime);
        }
    }

    /**
     * Updates the guest list of the host of this podcast and its listeners
     * @param mode  for setting the list update mode - add/remove guest
     * @param guest that interacts with the content
     */
    @Override
    public void updateClientGuests(final Client.GuestMode mode, final Client guest) {
        Client podcastOwner = Library.getLibrary().getHosts().get(owner);
        if (podcastOwner != null) {
            podcastOwner.updateGuests(mode, guest);

            switch (mode) {
                case ADD_GUEST -> listeners.add(guest);
                case REMOVE_GUEST -> listeners.remove(guest);
                default -> { }
            }
        }
    }

    /**
     * @return podcast episodes as the audio file list of current track
     */
    @Override
    public ArrayList<AudioFile> loadedAudioFiles() {
        return episodes;
    }

    /**
     * @param watchTime the watched time from the beginning of current audio track
     * @return episode from the current podcast after the specified watchTime passes
     */
    @Override
    public AudioFile loadAudioFile(final Integer watchTime) {
        if (watchTime == null) {
            return episodes.get(0);
        }
        for (int i = 0; i < elapsedTime.size(); i++) {
            if (watchTime < elapsedTime.get(i)) {
                return episodes.get(i);
            }
        }
        return episodes.get(0);
    }

    /**
     * Updates the audio file in the music player
     * taking into account the repeat status.
     * @param musicPlayer for updating the audio file
     * @param timeSinceUpdate since last update
     */
    @Override
    public void updateAudioFile(final MusicPlayer musicPlayer, final int timeSinceUpdate) {
        ArrayList<AudioFile> playQueue = new ArrayList<>(episodes);
        int timePassed = timeSinceUpdate;

        switch (musicPlayer.getRepeat()) {
            case "No Repeat":
                if (musicPlayer.simulatePlayQueue(playQueue, timePassed)
                        != musicPlayer.getRemainedTime()) {
                    // Reached the end of playQueue after the simulation
                    musicPlayer.setRemainedTime(0);
                }
                break;
            case "Repeat Once":
                // Current episode is added to the playQueue once more
                playQueue.add(playQueue.indexOf(musicPlayer.getAudioFile()),
                            musicPlayer.getAudioFile());
                musicPlayer.simulatePlayQueue(playQueue, timePassed);
                musicPlayer.setRepeat(0);
                break;
            case "Repeat Infinite":
                if (timePassed <= musicPlayer.getRemainedTime()) {
                    musicPlayer.setRemainedTime(musicPlayer.getRemainedTime() - timePassed);
                    return;
                }
                timePassed -= musicPlayer.getRemainedTime();
                int audioFileDuration = musicPlayer.getAudioFile().getDuration();
                musicPlayer.setRemainedTime(audioFileDuration - (timePassed % audioFileDuration));
                break;
            default:
                break;
        }
    }
}
