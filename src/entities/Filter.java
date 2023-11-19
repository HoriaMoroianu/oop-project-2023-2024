package entities;

import fileio.input.FilterInput;

import java.util.ArrayList;

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
}
