package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.SearchBar;
import entities.User;
import fileio.input.CommandInput;

public final class Load extends Command {
    public Load(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public ObjectNode executeCommand() {
        User user = Library.getLibrary().getUsers().get(username);
        SearchBar searchBar = user.getSearchBar();

        if (searchBar.getSelectedTrack() == null) {
            message = "Please select a source before attempting to load.";
            return new ObjectMapper().valueToTree(this);
        }

        if (searchBar.getSelectedTrack().loadAudioList().isEmpty()) {
            message = "You can't load an empty audio collection!";
            return new ObjectMapper().valueToTree(this);
        }

        user.getMusicPlayer().setTrack(searchBar.getSelectedTrack());
        searchBar.setSelectedTrack(null);
        message = "Playback loaded successfully.";

        return new ObjectMapper().valueToTree(this);
    }
}
