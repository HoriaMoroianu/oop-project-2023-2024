package commands.admin;

import app.audio.collections.Album;
import app.management.Library;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter @JsonInclude(JsonInclude.Include.NON_NULL)
public final class ShowAlbums extends Command {
    private ArrayList<Album> result;

    public ShowAlbums(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        result = Library.getLibrary().getArtists().get(username).getAlbums();
        return new ObjectMapper().valueToTree(this);
    }
}
