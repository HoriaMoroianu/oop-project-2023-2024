package entities;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class SearchBar {
    private ArrayList<AudioTrack> searchResults = new ArrayList<>();
    private AudioTrack selectedTrack;

    public void setSearchResults(final ArrayList<AudioTrack> audioTracks) {
        this.searchResults = audioTracks;
    }
    public void setSelectedTrack(final AudioTrack selectedTrack) {
        this.selectedTrack = selectedTrack;
    }
}
