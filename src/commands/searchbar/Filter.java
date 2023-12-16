package commands.searchbar;

import app.audio.collections.Album;
import app.audio.collections.AudioTrack;
import app.audio.collections.Playlist;
import app.audio.collections.Podcast;
import app.audio.files.Song;
import app.clients.Artist;
import app.clients.Host;
import fileio.input.FilterInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class Filter {
    private final String name;
    private final String albumName;
    private final ArrayList<String> tags;
    private final String lyrics;
    private final String genre;
    private final String releaseYear;
    private final String artistName;
    private final String owner;
    private final String description;

    public Filter(final FilterInput filterInput) {
        name = filterInput.getName();
        albumName = filterInput.getAlbum();
        tags = filterInput.getTags();
        lyrics = filterInput.getLyrics();
        genre = filterInput.getGenre();
        releaseYear = filterInput.getReleaseYear();
        artistName = filterInput.getArtist();
        owner = filterInput.getOwner();
        description = filterInput.getDescription();
    }

    /**
     * Tests if this audioTrack starts with the specified name filter.
     *
     * @param   audioTrack the audioTrack to be filtered.
     * @return  true if the audioTrack name starts with the text specified in the filter;
     *          false otherwise.
     *          Note also that true will be returned if the filter is null.
     */
    public boolean filterByName(final AudioTrack audioTrack) {
        return name == null || audioTrack.getName().startsWith(name);
    }

    /**
     * Tests if this artist starts with the specified name filter.
     *
     * @param   artist the artist to be filtered.
     * @return  true if the artist name starts with the text specified in the filter;
     *          false otherwise.
     *          Note also that true will be returned if the filter is null.
     */
    public boolean filterByName(final Artist artist) {
        return name == null || artist.getUsername().startsWith(name);
    }

    /**
     * Tests if this host starts with the specified name filter.
     *
     * @param   host the host to be filtered.
     * @return  true if the host name starts with the text specified in the filter;
     *          false otherwise.
     *          Note also that true will be returned if the filter is null.
     */
    public boolean filterByName(final Host host) {
        return name == null || host.getUsername().startsWith(name);
    }

    /**
     * Tests if this playlist was created by the specified owner filter.
     *
     * @param   playlist the playlist to be filtered.
     * @return  true if the playlist was created by the specified owner in the filter;
     *          false otherwise.
     *          Note also that true will be returned if the filter is null.
     */
    public boolean filterByOwner(final Playlist playlist) {
        return owner == null || playlist.getOwner().equals(owner);
    }

    /**
     * Tests if this podcast was created by the specified owner filter.
     *
     * @param   podcast the podcast to be filtered.
     * @return  true if the podcast was created by the specified owner in the filter;
     *          false otherwise.
     *          Note also that true will be returned if the filter is null.
     */
    public boolean filterByOwner(final Podcast podcast) {
        return owner == null || podcast.getOwner().equals(owner);
    }

    /**
     * Tests if this album was created by the specified owner filter.
     *
     * @param   album the album to be filtered.
     * @return  true if the album was created by the specified owner in the filter;
     *          false otherwise.
     *          Note also that true will be returned if the filter is null.
     */
    public boolean filterByOwner(final Album album) {
        return owner == null || album.getOwner().equals(owner);
    }

    /**
     * Tests if this album description starts with the specified description text.
     *
     * @param   album the audioTrack to be filtered.
     * @return  true if the album description starts with the text specified in the filter;
     *          false otherwise.
     *          Note also that true will be returned if the filter is null.
     */
    public boolean filterByDescription(final Album album) {
        return description == null || album.getDescription().startsWith(description);
    }

    /**
     * Tests if this song is part of specified album filter.
     *
     * @param   song the song to be filtered.
     * @return  true if the song name is part of specified album in the filter;
     *          false otherwise.
     *          Note also that true will be returned if the filter is null.
     */
    public boolean filterByAlbum(final Song song) {
        return albumName == null || song.getAlbum().equals(albumName);
    }

    /**
     * Tests if this song have all the specified tags.
     *
     * @param   song the song to be filtered.
     * @return  true if the song have all the specified tags in the filter;
     *          false otherwise.
     *          Note also that true will be returned if the filter is null.
     */
    public boolean filterByTags(final Song song) {
        return tags == null || song.getTags().containsAll(tags);
    }

    /**
     * Tests if this song contain the specified string in lyrics filter.
     *
     * @param   song the song to be filtered.
     * @return  true if the song contain the specified string in lyrics filter;
     *          false otherwise.
     *          Note also that true will be returned if the filter is null.
     */
    public boolean filterByLyrics(final Song song) {
        return lyrics == null || song.getLyrics().toLowerCase().contains(lyrics.toLowerCase());
    }

    /**
     * Tests if this song is part of the music genre filter.
     *
     * @param   song the song to be filtered.
     * @return  true if the song is part of the music genre specified in filter;
     *          false otherwise.
     *          Note also that true will be returned if the filter is null.
     */
    public boolean filterByGenre(final Song song) {
        return genre == null || song.getGenre().equalsIgnoreCase(genre);
    }

    /**
     * Tests if this song is created by the artist filter.
     *
     * @param   song the song to be filtered.
     * @return  true if the song is created by the artist specified in filter;
     *          false otherwise.
     *          Note also that true will be returned if the filter is null.
     */
    public boolean filterByArtist(final Song song) {
        return artistName == null || song.getArtist().equals(artistName);
    }

    /**
     * Tests if this song release year is lower or higher
     * than the specified year in filter.
     *
     * @param   song the song to be filtered.
     * @return  true if the song release year is lower
     *          or higher than the specified year in filter; false otherwise.
     *          Note also that true will be returned if the filter is null.
     */
    public boolean filterByReleaseYear(final Song song) {
        return releaseYear == null || (releaseYear.startsWith("<")
                ? song.getReleaseYear() < Integer.parseInt(releaseYear.substring(1))
                : song.getReleaseYear() > Integer.parseInt(releaseYear.substring(1)));
    }
}
