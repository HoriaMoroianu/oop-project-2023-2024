package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.SearchBar;
import entities.User;
import fileio.input.CommandInput;
import lombok.Getter;

@Getter
public final class Load extends Command {

    private String message;

    public Load(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public ObjectNode executeCommand() {
        User user = Library.getLibrary().getUsers().get(this.getUsername());
        SearchBar searchBar = user.getSearchBar();

        // TODO empty audio collection

        if (searchBar.getSelectedTrack() == null) {
            message = "Please select a source before attempting to load.";
            return new ObjectMapper().valueToTree(this);
        }

        user.getMusicPlayer().setTrack(searchBar.getSelectedTrack());
        searchBar.setSelectedTrack(null);
        message = "Playback loaded successfully.";

        return new ObjectMapper().valueToTree(this);
    }
}
