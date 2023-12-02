package app.audio.files;

import lombok.Getter;

@Getter
public abstract class AudioFile {
    protected final String name;
    protected final Integer duration;

    public AudioFile(final String name, final Integer duration) {
        this.name = name;
        this.duration = duration;
    }

    /**
     * @param watchTime the watched time from the beginning of current audio file
     * @return remaining duration of this audio file or null is watchTime is not defined
     */
    public Integer getDuration(final Integer watchTime) {
        return watchTime == null ? getDuration() : (getDuration() - watchTime);
    }
}
