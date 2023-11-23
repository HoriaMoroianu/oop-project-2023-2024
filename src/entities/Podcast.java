package entities;

import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Podcast implements AudioTrack {
    private String name;
    private String owner;
    private ArrayList<Episode> episodes = new ArrayList<>();
    private ArrayList<Integer> elapsedTime = new ArrayList<>();

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

    @Override
    public AudioFile findAudioFile(final Integer watchTime) {
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

    @Override
    public void updateAudioFile(final MusicPlayer musicPlayer, int timePassed) {
        int currentEpisodeIndex = episodes.indexOf((Episode) musicPlayer.getAudioFile());
        int timeUntilNext;

        for (; currentEpisodeIndex < episodes.size(); currentEpisodeIndex++) {
            // Calculate time until next episode starts
            timeUntilNext =
                    currentEpisodeIndex == episodes.indexOf((Episode) musicPlayer.getAudioFile())
                    ? musicPlayer.getRemainedTime()
                    : episodes.get(currentEpisodeIndex).getDuration();

            if (timeUntilNext - timePassed > 0) {
                // Keep current episode
                musicPlayer.setAudioFile(episodes.get(currentEpisodeIndex));
                musicPlayer.setName(episodes.get(currentEpisodeIndex).getName());
                musicPlayer.setRemainedTime(timeUntilNext - timePassed);
                return;
            }
            timePassed = timePassed - timeUntilNext;
        }
        musicPlayer.setRemainedTime(0);
    }
}
