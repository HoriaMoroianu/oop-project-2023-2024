package commands;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.audio_collections.Playlist;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter @JsonInclude(JsonInclude.Include.NON_NULL)
public final class ShowPlaylists extends Command {

    private ArrayList<Playlist> result;

    public ShowPlaylists(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        result = Library.getLibrary().getUsers().get(username).getPlaylists();
        return new ObjectMapper().valueToTree(this);
    }
}
