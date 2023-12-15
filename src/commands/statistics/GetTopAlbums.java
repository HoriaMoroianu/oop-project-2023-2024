package commands.statistics;

import app.audio.collections.Album;
import app.audio.collections.Playlist;
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
public final class GetTopAlbums extends Command {
    @Getter
    private final ArrayList<String> result = new ArrayList<>();
    private final Integer maxResults = 5;

    public GetTopAlbums(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        HashMap<Album, Integer> albumLikes = new HashMap<>();
        for (Album album : Library.getLibrary().getAlbums()) {
            albumLikes.put(album, album.likesReceived());
        }

        ArrayList<Album> sortedAlbums =
            (ArrayList<Album>) albumLikes.entrySet().stream().sorted((o1, o2) -> {
                if (o1.getValue() != o2.getValue()) {
                    return Integer.compare(o2.getValue(), o1.getValue());
                } else {
                    return o1.getKey().getName().compareTo(o2.getKey().getName());
                }
        }).limit(maxResults).map(Map.Entry::getKey).collect(Collectors.toList());

        sortedAlbums.stream().map(Playlist::getName).forEach(result::add);
        return new ObjectMapper().valueToTree(this);
    }
}
