package entities;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class SearchBar {
    private ArrayList<AudioTrack> searchResults = new ArrayList<>();
    private AudioTrack selectedTrack;
    private boolean searchConducted;

    public void setSearchResults(final ArrayList<AudioTrack> audioTracks) {
        this.searchResults = audioTracks;
        searchConducted = true;
    }
    public void clearSearchResults() {
        searchResults.clear();
        searchConducted = false;
    }
    public void setSelectedTrack(final AudioTrack selectedTrack) {
        this.selectedTrack = selectedTrack;
    }
}
