package app.clients;

import app.clients.services.Event;
import app.clients.services.Merch;
import app.audio.collections.Album;
import app.management.Library;
import fileio.input.UserInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Artist extends Client {
    private final ArrayList<Album> albums = new ArrayList<>();
    private final ArrayList<Event> events = new ArrayList<>();
    private final ArrayList<Merch> merches = new ArrayList<>();

    public Artist(final UserInput userInput) {
        super(userInput);
    }

    /**
     * Deletes this artist and all his entries from the application
     */
    @Override
    public void deleteClient() {
        for (Album album : albums) {
            Library.getLibrary().getUsers().values()
                    .forEach(user -> user.getLikedSongs().removeAll(album.getSongs()));

            Library.getLibrary().getSongs().removeAll(album.getSongs());
        }

        Library.getLibrary().getAlbums().removeAll(albums);
        Library.getLibrary().getArtists().remove(username);
    }

    /**
     * Get a list with the names of all the albums of this artist
     * @return the list with album's names
     */
    public ArrayList<String> getAlbumsNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Album album : albums) {
            names.add(album.getName());
        }
        return names;
    }

    /**
     * Get a list with the names of all the events of this artis
     * @return the list with event's names
     */
    public ArrayList<String> getEventsNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Event event : events) {
            names.add(event.getName());
        }
        return names;
    }

    /**
     * Get a list with the names of all the merchandises of this artist
     * @return the list with merchandise's names
     */
    public ArrayList<String> getMerchesNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Merch merch : merches) {
            names.add(merch.getName());
        }
        return names;
    }
}
