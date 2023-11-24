package entities.audio_collections;

import fileio.input.EpisodeInput;

public class Episode implements AudioFile {
    private final String name;
    private final Integer duration;
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

    public String getName() {
        return name;
    }
}
