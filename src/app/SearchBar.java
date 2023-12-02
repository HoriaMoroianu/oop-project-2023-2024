package app;

import app.audio.AudioTrack;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
public final class SearchBar {
    private ArrayList<AudioTrack> searchResults = new ArrayList<>();
    private boolean searchConducted;
    @Setter
    private AudioTrack selectedTrack;

    /**
     * Updates this searchBar with the new audioTracks
     * @param audioTracks new-found search results
     */
    public void setSearchResults(final ArrayList<AudioTrack> audioTracks) {
        this.searchResults = audioTracks;
        searchConducted = true;
    }

    /**
     * Clears this searchBar
     */
    public void clearSearchResults() {
        searchResults.clear();
        searchConducted = false;
    }
}
