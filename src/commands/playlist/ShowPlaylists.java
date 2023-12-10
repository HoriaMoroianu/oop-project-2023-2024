package commands.playlist;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import app.management.Library;
import app.audio.collections.Playlist;
import commands.Command;
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
