package entities;

import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;

import java.util.ArrayList;

public class Podcast {
    private String name;
    private String owner;
    private ArrayList<PodcastEpisode> episodes = new ArrayList<>();

    public Podcast() {
    }

    public Podcast(final PodcastInput podcastInput) {
        this.name = podcastInput.getName();
        this.owner = podcastInput.getOwner();
        for (final EpisodeInput episodeInput : podcastInput.getEpisodes()) {
            this.episodes.add(new PodcastEpisode(episodeInput));
        }
    }
}
