package entities;

import fileio.input.FilterInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Filter {
    private String name;
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private String releaseYear;
    private String artist;
    private String owner;

    public Filter(final FilterInput filterInput) {
        name= filterInput.getName();
        album = filterInput.getAlbum();
        tags = filterInput.getTags();
        lyrics = filterInput.getLyrics();
        genre = filterInput.getGenre();
        releaseYear = filterInput.getReleaseYear();
        artist = filterInput.getArtist();
        owner = filterInput.getOwner();
    }

    public boolean filterByName(Song song) {
        return name == null || song.getName().startsWith(name);
    }
    public boolean filterByName(Playlist playlist) {
        return name == null || playlist.getName().startsWith(name);
    }
    public boolean filterByName(Podcast podcast) {
        return name == null || podcast.getName().startsWith(name);
    }

    public boolean filterByOwner(Playlist playlist) {
        return owner == null || playlist.getOwner().equals(owner);
    }
    public boolean filterByOwner(Podcast podcast) {
        return owner == null || podcast.getOwner().equals(owner);
    }

    public boolean filterByAlbum(Song song) {
        return album == null || song.getAlbum().equals(album);
    }
    public boolean filterByTags(Song song) {
        return tags == null || song.getTags().containsAll(tags);
    }
    public boolean filterByLyrics(Song song) {
        return lyrics == null || song.getLyrics().contains(lyrics);
    }
    public boolean filterByGenre(Song song) {
        return genre == null || song.getGenre().equals(genre);
    }
    public boolean filterByArtist(Song song) {
        return artist == null || song.getArtist().equals(artist);
    }
    public boolean filterByReleaseYear(Song song) {
        return releaseYear == null || (releaseYear.startsWith("<")
                ? song.getReleaseYear() < Integer.parseInt(releaseYear)
                : song.getReleaseYear() > Integer.parseInt(releaseYear));
    }
}
