package commands.search_bar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import app.audio.AudioTrack;
import app.admin.Library;
import app.audio.collections.Playlist;
import app.audio.collections.Podcast;
import app.audio.files.Song;
import app.User;
import commands.Command;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.stream.Collectors;

@JsonIgnoreProperties({ "type", "filter", "maxSearchResults" })
public final class Search extends Command {
    private final String type;
    private final Filter filter;
    private final int maxSearchResults = 5;

    @Getter
    private final ArrayList<String> results = new ArrayList<>();

    public Search(final CommandInput commandInput) {
        super(commandInput);
        type = commandInput.getType();
        filter = new Filter(commandInput.getFilters());
    }

    @Override
    public ObjectNode executeCommand() {
        User user = Library.getLibrary().getUsers().get(username);
        user.getMusicPlayer().removeTrack();

        ArrayList<AudioTrack> audioTracks = new ArrayList<>();
        switch (type) {
            case "song":
                audioTracks = searchSongs(Library.getLibrary().getSongs());
                break;
            case "podcast":
                audioTracks = searchPodcast(Library.getLibrary().getPodcasts());
                break;
            case "playlist":
                audioTracks = searchPlaylist(userAccessiblePlaylists());
                break;
            default:
                break;
        }
        message = "Search returned " + audioTracks.size() + " results";
        audioTracks.forEach(audioPlayable -> results.add(audioPlayable.getName()));
        user.getSearchBar().setSearchResults(audioTracks);

        return new ObjectMapper().valueToTree(this);
    }

    private ArrayList<Playlist> userAccessiblePlaylists() {
        ArrayList<Playlist> userAccessible = new ArrayList<>();

        for (Playlist playlist : Library.getLibrary().getPlaylists()) {
            if (playlist.getOwner().equals(username)
                    || playlist.getVisibility().equals("public")) {
                userAccessible.add(playlist);
            }
        }
        return userAccessible;
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
