package commands;

import entities.AudioPlayable;
import entities.Filter;
import entities.Library;
import entities.Song;
import fileio.input.CommandInput;

import java.util.ArrayList;

public class Search extends Command {
    private String type;
    private Filter filter;

    public Search(final CommandInput commandInput) {
        super(commandInput);
        type = commandInput.getType();
        filter = new Filter(commandInput.getFilter());
    }

    @Override
    public void executeCommand() {
        switch (type) {
            case "song":
                // Array-Song
                // aplica filtre pe fiecare melodie

                break;
            case "playlist":
                // Array-Song
                // aplica filtre pe fiecare melodie
                break;
            case "podcast":
                // Array-podcasturi
                // aplica filtre pe fiecare podcast
                break;
        }
    }

    private ArrayList<AudioPlayable> searchSongs(ArrayList<Song> songs) {
        ArrayList<AudioPlayable> foundSongs = new ArrayList<>();
        songs.stream()
                .filter(song -> filter.filterByName(song))
                .filter(song -> filter.filterByAlbum(song))
                .filter(song -> filter.filterByGenre(song));
        return null;
    }
}
