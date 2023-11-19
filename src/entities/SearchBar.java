package entities;

import fileio.input.SongInput;

import java.util.ArrayList;

public class SearchBar {
    private ArrayList<AudioPlayable> audioPlayables = new ArrayList<>();

    public void updateSearchBar(ArrayList<AudioPlayable> audioPlayables) {
        this.audioPlayables = audioPlayables;
    }

}
