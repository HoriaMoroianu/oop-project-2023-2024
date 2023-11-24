package commands;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.Song;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;
@Getter @JsonInclude(JsonInclude.Include.NON_NULL)
public final class ShowPreferredSongs extends Command {

    private final ArrayList<String> result = new ArrayList<>();

    public ShowPreferredSongs(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        ArrayList<Song> songs = Library.getLibrary().getUsers().get(username).getLikedSongs();
        songs.forEach(song -> result.add(song.getName()));
        return new ObjectMapper().valueToTree(this);
    }
}
