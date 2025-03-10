package app.clients;

import app.clients.services.Event;
import app.clients.services.Merch;
import app.audio.collections.Album;
import app.clients.services.NotificationsObserver;
import app.management.Library;
import fileio.input.UserInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class Artist extends Client {
    private final ArrayList<Album> albums = new ArrayList<>();
    private final ArrayList<Event> events = new ArrayList<>();
    private final ArrayList<Merch> merches = new ArrayList<>();

    private final HashMap<String, NotificationsObserver> subscribedUsers = new HashMap<>();

    @Setter
    private Double merchRevenue = 0.0d;

    public Artist(final UserInput userInput) {
        super(userInput);
    }

    /**
     * @return type of this artist
     */
    @Override
    public String getType() {
        return "artist";
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

    /**
     * Adds a user to the subscriber list of this artist
     * @param user the user who will receive the notifications
     */
    public void subscribe(final User user) {
        subscribedUsers.put(user.getUsername(), new NotificationsObserver(user));
    }

    /**
     * Removes a user from the subscriber list of this artist
     * @param user the user who receives the notifications
     */
    public void unsubscribe(final User user) {
        subscribedUsers.remove(user.getUsername());
    }

    /**
     * Notify all subscribers about new content
     * @param type the type of notification
     */
    public void notifySubscribers(final String type) {
        subscribedUsers.values().forEach(observer -> observer.updateNotifications(type, username));
    }
}
