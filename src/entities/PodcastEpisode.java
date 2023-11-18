package entities;

import fileio.input.EpisodeInput;

public class PodcastEpisode {
    private String name;
    private Integer duration;
    private String description;

    public PodcastEpisode() {
    }

    public PodcastEpisode(final EpisodeInput episodeInput) {
        this.name = episodeInput.getName();
        this.duration = episodeInput.getDuration();
        this.description = episodeInput.getDescription();
    }
}
