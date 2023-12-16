package app.clients.services;

import app.audio.collections.AudioTrack;
import app.clients.Client;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
public final class SearchBar {
    private String searchType;
    private boolean searchConducted;

    private Integer foundResults;
    private ArrayList<AudioTrack> searchedTracks;
    private ArrayList<Client> searchedClients;

    @Setter
    private AudioTrack selectedTrack;

    /**
     * Updates this searchBar with the new audioTracks
     * @param audioTracks new-found search results
     */
    public void setSearchedTracks(final ArrayList<AudioTrack> audioTracks, final String type) {
        searchType = type;
        searchedTracks = audioTracks;
        foundResults = audioTracks.size();
        searchConducted = true;
    }

    /**
     * Updates this searchBar with the new clients
     * @param clients new-found search results
     */
    public void setSearchedClients(final ArrayList<Client> clients, final String type) {
        searchType = type;
        searchedClients = clients;
        foundResults = clients.size();
        searchConducted = true;
    }

    /**
     * Clears this searchBar
     */
    public void clearSearchResults() {
        searchType = null;
        searchedTracks = null;
        searchedClients = null;
        foundResults = 0;
        searchConducted = false;
    }
}
