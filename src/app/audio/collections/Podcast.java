package app.audio.collections;

import app.MusicPlayer;
import app.audio.AudioFile;
import app.audio.AudioTrack;
import app.audio.files.Episode;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Podcast implements AudioTrack {
    private final String name;
    private final String owner;
    private final ArrayList<AudioFile> episodes = new ArrayList<>();
    private final ArrayList<Integer> elapsedTime = new ArrayList<>();

    public Podcast(final PodcastInput podcastInput) {
        name = podcastInput.getName();
        owner = podcastInput.getOwner();

        int playtime = 0;
        for (final EpisodeInput episodeInput : podcastInput.getEpisodes()) {
            episodes.add(new Episode(episodeInput));
            playtime += episodeInput.getDuration();
            elapsedTime.add(playtime);
        }
    }

    /**
     * @return podcast episodes as the audio file list of current track
     */
    @Override
    public ArrayList<AudioFile> loadAudioList() {
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
