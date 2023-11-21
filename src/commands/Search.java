package commands;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.AudioPlayable;
import entities.Filter;
import entities.Library;
import entities.Playlist;
import entities.Podcast;
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
        ArrayList<AudioPlayable> audioPlayables = new ArrayList<>();

        switch (type) {
            case "song":
                audioPlayables = searchSongs(Library.getLibrary().getSongs());
                break;
            case "podcast":
                audioPlayables = searchPodcast(Library.getLibrary().getPodcasts());
                break;
            case "playlist":
                // TODO playlist al userului sau public
                audioPlayables = searchPlaylist(Library.getLibrary().getPlaylists());
                break;
            default:
                break;
        }
        message = "Search returned " + audioPlayables.size() + " results";
        audioPlayables.forEach(audioPlayable -> results.add(audioPlayable.getName()));

        Library.getLibrary()
            .getUsers()
            .get(this.getUsername())
            .getSearchBar()
            .setSearchResults(audioPlayables);

        return new ObjectMapper().valueToTree(this);
    }

    private ArrayList<AudioPlayable> searchSongs(final ArrayList<Song> songs) {
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
    private ArrayList<AudioPlayable> searchPodcast(final ArrayList<Podcast> podcasts) {
        return podcasts.stream()
            .filter(filter::filterByName)
            .filter(filter::filterByOwner)
            .limit(maxSearchResults)
            .collect(Collectors.toCollection(ArrayList::new));
    }
    private ArrayList<AudioPlayable> searchPlaylist(final ArrayList<Playlist> playlists) {
        return playlists.stream()
            .filter(filter::filterByName)
            .filter(filter::filterByOwner)
            .limit(maxSearchResults)
            .collect(Collectors.toCollection(ArrayList::new));
    }
}
