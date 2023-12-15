package app;

import app.audio.collections.AudioTrack;
import app.audio.collections.Playlist;
import app.audio.collections.Podcast;
import app.audio.files.AudioFile;
import app.audio.files.Episode;
import app.audio.files.Song;
import app.clients.Artist;
import app.clients.Client;
import app.clients.Host;
import app.clients.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;

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
    private final Integer maxResults = 5;

    public Page(final Type type, final Client pageOwner) {
        this.type = type;
        this.pageOwner = pageOwner;
    }

    public void switchPage(final Type newType, final Client newOwner, final Client guest) {
        pageOwner.updateGuests(Client.GuestMode.REMOVE_GUEST, guest);
        newOwner.updateGuests(Client.GuestMode.ADD_GUEST, guest);

        type = newType;
        pageOwner = newOwner;
    }

    /**
     * Formats the content that will be displayed on the home page
     * @return string in the desired page format
     */
    public String printHomePage() {
        User user = (User) pageOwner;
        ArrayList<String> likedSongsNames = new ArrayList<>();
        ArrayList<String> followedPlaylistsNames = new ArrayList<>();

        user.getLikedSongs().stream()
                .sorted(Comparator.comparingInt(Song::getLikesReceived).reversed())
                .limit(maxResults).map(AudioFile::getName).forEach(likedSongsNames::add);

        user.getFollowedPlaylists().stream()
                .sorted(Comparator.comparingInt(Playlist::likesReceived).reversed())
                .limit(maxResults).map(AudioTrack::getName).forEach(followedPlaylistsNames::add);

        return "Liked songs:\n\t" + likedSongsNames
                + "\n\nFollowed playlists:\n\t" + followedPlaylistsNames;
    }

    /**
     * Formats the content that will be displayed on the liked content page
     * @return string in the desired page format
     */
    public String printLikedContentPage() {
        User user = (User) pageOwner;

        ArrayList<String> songFormatting = new ArrayList<>();
        for (Song song : user.getLikedSongs()) {
            songFormatting.add(song.getName() + " - " + song.getArtist());
        }

        ArrayList<String> playlistFormatting = new ArrayList<>();
        for (Playlist playlist : user.getFollowedPlaylists()) {
            playlistFormatting.add(playlist.getName() + " - " + playlist.getOwner());
        }

        return "Liked songs:\n\t" + songFormatting
                + "\n\nFollowed playlists:\n\t" + playlistFormatting;
    }

    /**
     * Formats the content that will be displayed on the artist page
     * @return string in the desired page format
     */
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

    /**
     * Formats the content that will be displayed on the artist page
     * @return string in the desired page format
     */
    public String printHostPage() {
        Host host = (Host) pageOwner;

        ArrayList<String> announcementFormatting = new ArrayList<>();
        for (Announcement announcement : host.getAnnouncements()) {
            announcementFormatting.add(announcement.getName() + ":\n\t"
                    + announcement.getDescription() + "\n");
        }

        ArrayList<String> podcastFormatting = new ArrayList<>();
        for (Podcast podcast : host.getPodcasts()) {
            podcastFormatting.add(podcast.getName() + ":\n\t"
                    + getEpisodeFormatting(podcast) + "\n");
        }

        return "Podcasts:\n\t" + podcastFormatting
                + "\n\nAnnouncements:\n\t" + announcementFormatting;
    }

    private ArrayList<String> getEpisodeFormatting(final Podcast podcast) {
        ArrayList<String> episodeFormatting = new ArrayList<>();
        for (AudioFile audioFile : podcast.getEpisodes()) {
            Episode episode = (Episode) audioFile;
            episodeFormatting.add(episode.getName() + " - " + episode.getDescription());
        }
        return episodeFormatting;
    }
}
