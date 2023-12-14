package app;

import app.audio.collections.Playlist;
import app.audio.files.Song;
import app.clients.Artist;
import app.clients.Client;
import app.clients.User;
import lombok.Getter;

import java.util.ArrayList;

public class Page {
    public enum Type {
        HOME,
        LIKED_CONTENT,
        ARTIST,
        HOST
    }

    @Getter
    private Type type;
    @Getter
    private Client pageOwner;

    public Page(final Type type, final Client pageOwner) {
        this.type = type;
        this.pageOwner = pageOwner;
    }

    public void switchPage(final Type newType, final Client newOwner, final Client guest) {
        pageOwner.updateGuests(Client.UpdateMode.REMOVE_GUEST, guest);
        newOwner.updateGuests(Client.UpdateMode.ADD_GUEST, guest);

        type = newType;
        pageOwner = newOwner;
    }

    public String printHomePage() {
        User user = (User) pageOwner;

        ArrayList<String> likedSongsNames = new ArrayList<>();
        for (Song song : user.getLikedSongs()) {
            likedSongsNames.add(song.getName());
        }

        ArrayList<String> followedPlaylistsNames = new ArrayList<>();
        for (Playlist playlist : user.getFollowedPlaylists()) {
            followedPlaylistsNames.add(playlist.getName());
        }

        return "Liked songs:\n\t" + likedSongsNames
                + "\n\nFollowed playlists:\n\t" + followedPlaylistsNames;
    }

    public String printArtistPage() {
        Artist artist = (Artist) pageOwner;

        ArrayList<String> merchFormatting = new ArrayList<>();
        for (Merch merch : artist.getMerches()) {
            merchFormatting.add(merch.getName() + " - "
                    + merch.getPrice() + ":\n\t" + merch.getDescription());
        }

        ArrayList<String> eventFormatting = new ArrayList<>();
        for (Event event : artist.getEvents()) {
            eventFormatting.add(event.getName() + " - "
                    + event.getDate() + ":\n\t" + event.getDescription());
        }

        return "Albums:\n\t" + artist.getAlbumsNames()
                + "\n\nMerch:\n\t" + merchFormatting
                + "\n\nEvents:\n\t" + eventFormatting;
    }
}
