package commands.statistics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import app.management.Library;
import app.audio.files.Song;
import commands.Command;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class GetTopSongs extends Command {
    @Getter
    private final ArrayList<String> result = new ArrayList<>();
    private final int maxResults = 5;
    public GetTopSongs(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        ArrayList<Song> songs = new ArrayList<>(Library.getLibrary().getSongs());
        songs.sort(Comparator.comparingInt(Song::getLikesReceived).reversed());
        songs.stream().limit(maxResults).forEach(song -> result.add(song.getName()));

        return new ObjectMapper().valueToTree(this);
    }
}
