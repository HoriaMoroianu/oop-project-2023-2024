package commands;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
        switch (type) {
            case "song":
                ArrayList<Song> songs = searchSongs(Library.getLibrary().getSongs());
                message = "Search returned " + songs.size() + " results";
                songs.forEach(song -> results.add(song.getName()));
                break;
            case "podcast":
                ArrayList<Podcast> podcasts = searchPodcast(Library.getLibrary().getPodcasts());
                message = "Search returned " + podcasts.size() + " results";
                podcasts.forEach(podcast -> results.add(podcast.getName()));
                break;
            case "playlist":
                // TODO playlist al userului sau public
                ArrayList<Playlist> playlists = searchPlaylist(Library.getLibrary().getPlaylists());
                break;
            default:
                break;
        }
        // TODO output
        return new ObjectMapper().valueToTree(this);
    }

    private ArrayList<Song> searchSongs(final ArrayList<Song> songs) {
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
    private ArrayList<Podcast> searchPodcast(final ArrayList<Podcast> podcasts) {
        return podcasts.stream()
                       .filter(filter::filterByName)
                       .filter(filter::filterByOwner)
                       .limit(maxSearchResults)
                       .collect(Collectors.toCollection(ArrayList::new));
    }
    private ArrayList<Playlist> searchPlaylist(final ArrayList<Playlist> playlists) {
        return playlists.stream()
                        .filter(filter::filterByName)
                        .filter(filter::filterByOwner)
                        .limit(maxSearchResults)
                        .collect(Collectors.toCollection(ArrayList::new));
    }
}
