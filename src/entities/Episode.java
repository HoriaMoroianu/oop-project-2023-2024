package entities;

import fileio.input.EpisodeInput;

public class Episode implements AudioFile {
    private String name;
    private Integer duration;
    private String description;

    public Episode(final EpisodeInput episodeInput) {
        name = episodeInput.getName();
        duration = episodeInput.getDuration();
        description = episodeInput.getDescription();
    }

    @Override
    public Integer getDuration() {
        return duration;
    }

    @Override
    public Integer getDuration(final Integer watchTime) {
        return watchTime == null ? getDuration() : (getDuration() - watchTime);
    }

    public String getName() {
        return name;
    }
}
