package commands;

import app.audio.collections.Album;
import app.audio.collections.Playlist;
import app.audio.files.AudioFile;
import app.audio.files.Song;
import app.clients.Artist;
import app.clients.Client;
import app.clients.User;
import app.clients.services.Event;
import app.clients.services.Merch;
import app.management.Library;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import fileio.input.SongInput;

import java.util.ArrayList;

public final class ArtistCommands extends CommandStrategy {
    private Client client;
    private Artist artist;
    private final String name;
    private final Integer releaseYear;
    private final String description;
    private final ArrayList<SongInput> songs;
    private final String date;
    private final Integer price;

    // Constants for validating the date of an event:
    private static final int FEBRUARY = 2;
    private static final int MAX_FEBRUARY_DAYS = 28;
    private static final int MAX_MONTH_DAYS = 31;
    private static final int MAX_MONTHS = 12;
    private static final int MIN_YEAR = 1900;
    private static final int MAX_YEAR = 2023;

    public ArtistCommands(final CommandInput commandInput) {
        super(commandInput);
        name = commandInput.getName();
        releaseYear = commandInput.getReleaseYear();
        description = commandInput.getDescription();
        songs = commandInput.getSongs();
        date = commandInput.getDate();
        price = commandInput.getPrice();
    }

    @Override
    protected ObjectNode executeCommand() {
        client = Library.getLibrary().getClient(username);

        return switch (command) {
            case "addAlbum" -> addAlbum();
            case "removeAlbum" -> removeAlbum();
            case "addEvent" -> addEvent();
            case "removeEvent" -> removeEvent();
            case "addMerch" -> addMerch();
            default -> null;
        };
    }

    private ObjectNode addAlbum() {
        if (clientNotArtist()) {
            return outputNode;
        }

        if (artist.getAlbumsNames().contains(name)) {
            message = username + " has another album with the same name.";
            outputNode.put("message", message);
            return outputNode;
        }

        ArrayList<Song> albumSongs = new ArrayList<>();
        ArrayList<String> songNames = new ArrayList<>();

        for (SongInput songInput : songs) {
            Song song = new Song(songInput);

            if (songNames.contains(song.getName())) {
                message = username + " has the same song at least twice in this album.";
                outputNode.put("message", message);
                return outputNode;
            }

            albumSongs.add(song);
            songNames.add(song.getName());
        }

        Album album = new Album(name, username, releaseYear, description);
        album.getSongs().addAll(albumSongs);
        artist.getAlbums().add(album);

        Library.getLibrary().getAlbums().add(album);
        Library.getLibrary().getSongs().addAll(albumSongs);

        message = username + " has added new album successfully.";
        artist.getSubscribedUsers().forEach(user ->
                user.updateNotifications("New Album", username));

        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode removeAlbum() {
        if (clientNotArtist()) {
            return outputNode;
        }

        Library.getLibrary().updateMusicPlayers();

        if (!artist.getAlbumsNames().contains(name)) {
            message = username + " doesn't have an album with the given name.";
            outputNode.put("message", message);
            return outputNode;
        }

        Album albumToDelete = artist.getAlbums().stream()
                .filter(album -> album.getName().equals(name))
                .findFirst().orElse(null);

        if (!safeAlbumDelete(albumToDelete)) {
            message = username + " can't delete this album.";
            outputNode.put("message", message);
            return outputNode;
        }

        Library.getLibrary().getAlbums().remove(albumToDelete);
        artist.getAlbums().remove(albumToDelete);

        message = username + " deleted the album successfully.";
        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode addEvent() {
        if (clientNotArtist()) {
            return outputNode;
        }

        if (artist.getEventsNames().contains(name)) {
            message = username + " has another event with the same name.";
            outputNode.put("message", message);
            return outputNode;
        }

        if (!validDate(date)) {
            message = "Event for " + username + " does not have a valid date.";
            outputNode.put("message", message);
            return outputNode;
        }

        artist.getEvents().add(new Event(name, description, date));
        artist.getSubscribedUsers().forEach(user ->
                user.updateNotifications("New Event", username));

        message = username + " has added new event successfully.";
        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode removeEvent() {
        if (clientNotArtist()) {
            return outputNode;
        }

        if (!artist.getEventsNames().contains(name)) {
            message = username + " doesn't have an event with the given name.";
            outputNode.put("message", message);
            return outputNode;
        }

        Event eventToDelete = artist.getEvents().stream()
                .filter(event -> event.getName().equals(name))
                .findFirst().orElse(null);

        artist.getEvents().remove(eventToDelete);
        message = username + " deleted the event successfully.";
        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode addMerch() {
        if (clientNotArtist()) {
            return outputNode;
        }

        if (artist.getMerchesNames().contains(name)) {
            message = username + " has merchandise with the same name.";
            outputNode.put("message", message);
            return outputNode;
        }

        if (price < 0) {
            message = "Price for merchandise can not be negative.";
            outputNode.put("message", message);
            return outputNode;
        }

        Merch merch = new Merch(name, description, price, artist);
        Library.getLibrary().getAppMerch().put(merch.getName(), merch);

        artist.getMerches().add(merch);
        artist.getSubscribedUsers().forEach(user ->
                user.updateNotifications("New Merchandise", username));

        message = username + " has added new merchandise successfully.";
        outputNode.put("message", message);
        return outputNode;
    }

    private boolean clientNotArtist() {
        if (client == null) {
            message = "The username " + username + " doesn't exist.";
            outputNode.put("message", message);
            return true;
        }

        if (client.getClass() != Artist.class) {
            message = username + " is not an artist.";
            outputNode.put("message", message);
            return true;
        }

        artist = (Artist) client;
        return false;
    }

    private boolean safeAlbumDelete(final Album album) {
        if (!album.getListeners().isEmpty()) {
            return false;
        }

        for (AudioFile audioFile : album.getSongs()) {
            Song song = (Song) audioFile;
            if (!song.getListeners().isEmpty()) {
                return false;
            }

            // Check if the songs of the album are in a playlist in the library
            for (Playlist playlist : Library.getLibrary().getPlaylists()) {
                if (playlist.getSongs().contains(song)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validDate(final String stringDate) {
        String[] splitDate = stringDate.split("-");

        int day = Integer.parseInt(splitDate[0]);
        int month = Integer.parseInt(splitDate[1]);
        int year = Integer.parseInt(splitDate[2]);

        return (month != FEBRUARY || day <= MAX_FEBRUARY_DAYS)
                && day <= MAX_MONTH_DAYS
                && month <= MAX_MONTHS
                && year >= MIN_YEAR && year <= MAX_YEAR;
    }
}
