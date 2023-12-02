package app.audio.files;

import fileio.input.EpisodeInput;

public class Episode extends AudioFile {
    private final String description;

    public Episode(final EpisodeInput episodeInput) {
        super(episodeInput.getName(), episodeInput.getDuration());
        description = episodeInput.getDescription();
    }
}
