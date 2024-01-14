package app.audio.files;

import fileio.input.EpisodeInput;
import lombok.Getter;

@Getter
public class Episode extends AudioFile {
    private final String description;

    public Episode(final EpisodeInput episodeInput) {
        super(episodeInput.getName(), episodeInput.getDuration());
        description = episodeInput.getDescription();
    }

    /**
     * @return type of this audio track
     */
    @Override
    public String getType() {
        return "episode";
    }
}
