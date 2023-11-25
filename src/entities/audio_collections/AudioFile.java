package entities.audio_collections;

public interface AudioFile {
    /**
     * @return name of this audio file
     */
    String getName();

    /**
     * @return duration of this audio file
     */
    Integer getDuration();

    /**
     * @param watchTime the watched time from the beginning of current audio file
     * @return remaining duration of this audio file or null is watchTime is not defined
     */
    default Integer getDuration(Integer watchTime) {
        return watchTime == null ? getDuration() : (getDuration() - watchTime);
    }
}
