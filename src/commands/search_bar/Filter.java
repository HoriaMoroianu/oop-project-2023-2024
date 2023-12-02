package commands.search_bar;

import app.audio.collections.Playlist;
import app.audio.collections.Podcast;
import app.audio.files.Song;
import fileio.input.FilterInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class Filter {
    private final String name;
    private final String album;
    private final ArrayList<String> tags;
    private final String lyrics;
    private final String genre;
    private final String releaseYear;
    private final String artist;
    private final String owner;

    public Filter(final FilterInput filterInput) {
        name = filterInput.getName();
        album = filterInput.getAlbum();
        tags = filterInput.getTags();
        lyrics = filterInput.getLyrics();
        genre = filterInput.getGenre();
        releaseYear = filterInput.getReleaseYear();
        artist = filterInput.getArtist();
        owner = filterInput.getOwner();
    }

    /**
     * Tests if this song starts with the specified name filter.
     *
     * @param   song the song to be filtered.
     * @return  true if the song name starts with the text specified in the filter;
     *          false otherwise.
     *          Note also that true will be returned if the filter is null.
     */
    public boolean filterByName(final Song song) {
        return name == null || song.getName().startsWith(name);
    }

    /**
     * Tests if this playlist starts with the specified name filter.
     *
     * @param   playlist the playlist to be filtered.
     * @return  true if the playlist name starts with the text specified in the filter;
     *          false otherwise.
     *          Note also that true will be returned if the filter is null.
     */
    public boolean filterByName(final Playlist playlist) {
        return name == null || playlist.getName().startsWith(name);
    }

    /**
     * Tests if this podcast starts with the specified name filter.
     *
     * @param   podcast the podcast to be filtered.
     * @return  true if the podcast name starts with the text specified in the filter;
     *          false otherwise.
     *          Note also that true will be returned if the filter is null.
     */
    public boolean filterByName(final Podcast podcast) {
        return name == null || podcast.getName().startsWith(name);
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
     * Tests if this song is part of specified album filter.
     *
     * @param   song the song to be filtered.
     * @return  true if the song name is part of specified album in the filter;
     *          false otherwise.
     *          Note also that true will be returned if the filter is null.
     */
    public boolean filterByAlbum(final Song song) {
        return album == null || song.getAlbum().equals(album);
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
        return artist == null || song.getArtist().equals(artist);
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
