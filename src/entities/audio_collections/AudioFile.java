package entities.audio_collections;

public interface AudioFile {
    String getName();
    Integer getDuration();
    default Integer getDuration(Integer watchTime) {
        return watchTime == null ? getDuration() : (getDuration() - watchTime);
    }
}
