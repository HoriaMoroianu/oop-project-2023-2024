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

    public Podcast(final PodcastInput podcastInput) {
        name = podcastInput.getName();
        owner = podcastInput.getOwner();
        for (final EpisodeInput episodeInput : podcastInput.getEpisodes()) {
            episodes.add(new Episode(episodeInput));
        }
    }

    @Override
    public AudioFile findAudioFile() {
        // TODO de unde am ramas
        return episodes.get(0);
    }

    @Override
    public void updateAudioFile(final MusicPlayer musicPlayer, final int timePassed) {

    }
}
