package app.audio.collections;

import app.clients.Client;
import app.management.Library;
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

    @Override
    public void updateClientGuests(final Client.UpdateMode mode, final Client guest) {
        Client albumOwner = Library.getLibrary().getArtists().get(owner);
        if (albumOwner != null) {
            albumOwner.updateGuests(mode, guest);
        }
    }
}
