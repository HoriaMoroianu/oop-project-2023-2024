package commands;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.Playlist;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.Comparator;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class GetTopPlaylists extends Command {
    private final ArrayList<String> result = new ArrayList<>();
    private final int maxResults = 5;
    public GetTopPlaylists(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        ArrayList<Playlist> playlists = new ArrayList<>();

        for (Playlist playlist : Library.getLibrary().getPlaylists()) {
            if (playlist.getVisibility().equals("public")) {
                playlists.add(playlist);
            }
        }
        playlists.sort(Comparator.comparingInt(Playlist::getFollowers).reversed());
        playlists.stream().limit(maxResults).forEach(playlist -> result.add(playlist.getName()));

        return new ObjectMapper().valueToTree(this);
    }

    public ArrayList<String> getResult() {
        return result;
    }
}
