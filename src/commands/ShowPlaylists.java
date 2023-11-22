package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.Playlist;
import fileio.input.CommandInput;

import java.util.ArrayList;

public final class ShowPlaylists extends Command {
    private ArrayList<Playlist> result;

    public ShowPlaylists(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        result = Library.getLibrary().getUsers().get(this.getUsername()).getPlaylists();
        return new ObjectMapper().valueToTree(this);
    }

    public ArrayList<Playlist> getResult() {
        return result;
    }
}
