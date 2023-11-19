package entities;

import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;

import java.util.ArrayList;

public class Podcast {
    private String name;
    private String owner;
    private ArrayList<Episode> episodes = new ArrayList<>();

    public Podcast() {
    }

    public Podcast(final PodcastInput podcastInput) {
        name = podcastInput.getName();
        owner = podcastInput.getOwner();
        for (final EpisodeInput episodeInput : podcastInput.getEpisodes()) {
            episodes.add(new Episode(episodeInput));
        }
    }
}
