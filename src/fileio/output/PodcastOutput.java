package fileio.output;

import app.audio.collections.Podcast;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class PodcastOutput {
    private final String name;
    private final ArrayList<String> episodes;

    public PodcastOutput(final Podcast podcast) {
        name = podcast.getName();
        // TODO episode names here?
        episodes = podcast.getEpisodeNames();
    }
}
