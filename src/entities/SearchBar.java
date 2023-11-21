package entities;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class SearchBar {
    private ArrayList<AudioPlayable> searchResults = new ArrayList<>();
    private AudioPlayable selectedAudio;

    public void setSearchResults(final ArrayList<AudioPlayable> audioPlayables) {
        this.searchResults = audioPlayables;
    }
    public void setSelectedAudio(final AudioPlayable selectedAudio) {
        this.selectedAudio = selectedAudio;
    }
}
