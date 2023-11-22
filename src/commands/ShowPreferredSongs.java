package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.Song;
import fileio.input.CommandInput;

import java.util.ArrayList;

public final class ShowPreferredSongs extends Command {
    private ArrayList<String> result = new ArrayList<>();

    public ShowPreferredSongs(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        ArrayList<Song> songs =
                Library.getLibrary().getUsers().get(this.getUsername()).getLikedSongs();

        songs.forEach(song -> result.add(song.getName()));
        return new ObjectMapper().valueToTree(this);
    }

    public ArrayList<String> getResult() {
        return result;
    }
}
