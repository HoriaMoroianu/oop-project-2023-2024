package entities;

public interface AudioFile {
    // TODO clasa abstracta?
    String getName();
    Integer getDuration();
    default Integer getDuration(Integer watchTime) {
        return watchTime == null ? getDuration() : (getDuration() - watchTime);
    }
}
