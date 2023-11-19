package entities;

import fileio.input.FiltersInput;

import java.util.ArrayList;

public class SearchFilters {
    private String name;
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private String releaseYear;
    private String artist;
    private String owner;

    public SearchFilters(FiltersInput filtersInput) {
        this.name= filtersInput.getName();
        this.album = filtersInput.getAlbum();
        this.tags = filtersInput.getTags();
        this.lyrics = filtersInput.getLyrics();
        this.genre = filtersInput.getGenre();
        this.releaseYear = filtersInput.getReleaseYear();
        this.artist = filtersInput.getArtist();
        this.owner = filtersInput.getOwner();
    }
}
