package app.audio.collections;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties({"followers", "visibility", "description", "releaseYear"})
public class Album extends Playlist {
    private final Integer releaseYear;
    private final String description;

    public Album(final String name, final String owner,
                 final Integer releaseYear, final String description) {
        super(name, owner);
        this.releaseYear = releaseYear;
        this.description = description;
    }
}
