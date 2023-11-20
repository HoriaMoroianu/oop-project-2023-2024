package entities;

import fileio.input.FilterInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class Filter {
    private String name;
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private String releaseYear;
    private String artist;
    private String owner;

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

    public boolean filterByName(final Song song) {
        return name == null || song.getName().startsWith(name);
    }
    public boolean filterByName(final Playlist playlist) {
        return name == null || playlist.getName().startsWith(name);
    }
    public boolean filterByName(final Podcast podcast) {
        return name == null || podcast.getName().startsWith(name);
    }
    public boolean filterByOwner(final Playlist playlist) {
        return owner == null || playlist.getOwner().equals(owner);
    }
    public boolean filterByOwner(final Podcast podcast) {
        return owner == null || podcast.getOwner().equals(owner);
    }
    public boolean filterByAlbum(final Song song) {
        return album == null || song.getAlbum().equals(album);
    }
    public boolean filterByTags(final Song song) {
        return tags == null || song.getTags().containsAll(tags);
    }
    public boolean filterByLyrics(final Song song) {
        return lyrics == null || song.getLyrics().contains(lyrics);
    }
    public boolean filterByGenre(final Song song) {
        return genre == null || song.getGenre().equalsIgnoreCase(genre);
    }
    public boolean filterByArtist(final Song song) {
        return artist == null || song.getArtist().equals(artist);
    }
    public boolean filterByReleaseYear(final Song song) {
        return releaseYear == null || (releaseYear.startsWith("<")
                ? song.getReleaseYear() < Integer.parseInt(releaseYear.substring(1))
                : song.getReleaseYear() > Integer.parseInt(releaseYear.substring(1)));
    }
}
