package app.audio.files;

import app.audio.AudioFile;
import fileio.input.EpisodeInput;
import lombok.Getter;

public class Episode implements AudioFile {
    @Getter
    private final String name;
    @Getter
    private final Integer duration;
    private String description;

    public Episode(final EpisodeInput episodeInput) {
        name = episodeInput.getName();
        duration = episodeInput.getDuration();
        description = episodeInput.getDescription();
    }
}
