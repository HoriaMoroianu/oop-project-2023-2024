package commands.statistics;

import app.audio.collections.Album;
import app.clients.Artist;
import app.management.Library;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@JsonInclude(JsonInclude.Include.NON_NULL)
public final class GetTopArtists extends Command {
    @Getter
    private final ArrayList<String> result = new ArrayList<>();
    private final Integer maxResults = 5;

    public GetTopArtists(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        HashMap<Artist, Integer> artistLikes = new HashMap<>();

        for (Artist artist : Library.getLibrary().getArtists().values()) {
            int likes = artist.getAlbums().stream().mapToInt(Album::likesReceived).sum();
            artistLikes.put(artist, likes);
        }

        ArrayList<Artist> sortedArtist = (ArrayList<Artist>) artistLikes.entrySet().stream()
                .sorted((o1, o2) -> Integer.compare(o2.getValue(), o1.getValue()))
                .limit(maxResults).map(Map.Entry::getKey).collect(Collectors.toList());

        sortedArtist.stream().map(Artist::getUsername).forEach(result::add);
        return new ObjectMapper().valueToTree(this);
    }
}
