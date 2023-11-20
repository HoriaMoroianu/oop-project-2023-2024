package entities;

import lombok.Getter;

import java.util.ArrayList;

public final class SearchBar {
    @Getter
    private ArrayList<AudioPlayable> audioPlayables = new ArrayList<>();

    public void updateSearchBar(final ArrayList<AudioPlayable> audioPlayables) {
        this.audioPlayables = audioPlayables;
    }
}
