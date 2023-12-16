package app.audio.collections;

import app.clients.Client;
import app.management.Library;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@JsonIgnoreProperties({"followers", "visibility", "description", "releaseYear", "listeners"})
public class Album extends Playlist {
    private final Integer releaseYear;
    private final String description;

    private final ArrayList<Client> listeners = new ArrayList<>();

    public Album(final String name, final String owner,
                 final Integer releaseYear, final String description) {
        super(name, owner);
        this.releaseYear = releaseYear;
        this.description = description;
    }

    /**
     * Updates the guest list of the artist of this album and its listeners
     * @param mode  for setting the list update mode - add/remove guest
     * @param guest that interacts with the content
     */
    @Override
    public void updateClientGuests(final Client.GuestMode mode, final Client guest) {
        Client albumOwner = Library.getLibrary().getArtists().get(owner);
        if (albumOwner != null) {
            albumOwner.updateGuests(mode, guest);

            switch (mode) {
                case ADD_GUEST -> listeners.add(guest);
                case REMOVE_GUEST -> listeners.remove(guest);
                default -> { }
            }
        }
    }
}
