package commands;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.AudioTrack;
import entities.Filter;
import entities.Library;
import entities.Playlist;
import entities.Podcast;
import entities.SearchBar;
import entities.Song;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Getter @JsonIgnoreProperties({ "type", "filter", "maxSearchResults" })
public final class Search extends Command {
    private String type;
    private Filter filter;
    private String message;
    private ArrayList<String> results = new ArrayList<>();
    private final int maxSearchResults = 5;

    public Search(final CommandInput commandInput) {
        super(commandInput);
        type = commandInput.getType();
        filter = new Filter(commandInput.getFilters());
    }

    @Override
    public ObjectNode executeCommand() {

        // TODO scoate sursa din player

        SearchBar searchBar = Library.getLibrary()
                .getUsers()
                .get(this.getUsername())
                .getSearchBar();

        searchBar.emptyBar();
        ArrayList<AudioTrack> audioTracks = new ArrayList<>();

        switch (type) {
            case "song":
                audioTracks = searchSongs(Library.getLibrary().getSongs());
                break;
            case "podcast":
                audioTracks = searchPodcast(Library.getLibrary().getPodcasts());
                break;
            case "playlist":
                // TODO playlist al userului sau public
                audioTracks = searchPlaylist(Library.getLibrary().getPlaylists());
                break;
            default:
                break;
        }
        message = "Search returned " + audioTracks.size() + " results";
        audioTracks.forEach(audioPlayable -> results.add(audioPlayable.getName()));
        searchBar.setSearchResults(audioTracks);

        return new ObjectMapper().valueToTree(this);
    }

    private ArrayList<AudioTrack> searchSongs(final ArrayList<Song> songs) {
        return songs.stream()
            .filter(filter::filterByName)
            .filter(filter::filterByAlbum)
            .filter(filter::filterByTags)
            .filter(filter::filterByLyrics)
            .filter(filter::filterByGenre)
            .filter(filter::filterByReleaseYear)
            .filter(filter::filterByArtist)
            .limit(maxSearchResults)
            .collect(Collectors.toCollection(ArrayList::new));
    }
    private ArrayList<AudioTrack> searchPodcast(final ArrayList<Podcast> podcasts) {
        return podcasts.stream()
            .filter(filter::filterByName)
            .filter(filter::filterByOwner)
            .limit(maxSearchResults)
            .collect(Collectors.toCollection(ArrayList::new));
    }
    private ArrayList<AudioTrack> searchPlaylist(final ArrayList<Playlist> playlists) {
        return playlists.stream()
            .filter(filter::filterByName)
            .filter(filter::filterByOwner)
            .limit(maxSearchResults)
            .collect(Collectors.toCollection(ArrayList::new));
    }
}
