package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.audio_collections.AudioTrack;
import entities.Library;
import entities.SearchBar;
import fileio.input.CommandInput;

import java.util.ArrayList;

public final class Select extends Command {
    private final Integer itemNumber;

    public Select(final CommandInput commandInput) {
        super(commandInput);
        itemNumber = commandInput.getItemNumber();
    }

    @Override
    public ObjectNode executeCommand() {
        SearchBar searchBar = Library.getLibrary().getUsers().get(username).getSearchBar();

        ArrayList<AudioTrack> searchResults = searchBar.getSearchResults();
        searchBar.setSelectedTrack(null);

        if (!searchBar.isSearchConducted()) {
            message = "Please conduct a search before making a selection.";
            return new ObjectMapper().valueToTree(this);
        }

        if (itemNumber > searchResults.size()) {
            message = "The selected ID is too high.";
            return new ObjectMapper().valueToTree(this);
        }

        searchBar.setSelectedTrack(searchResults.get(itemNumber - 1));
        searchBar.clearSearchResults();

        message = "Successfully selected " + searchBar.getSelectedTrack().getName() + ".";
        return new ObjectMapper().valueToTree(this);
    }
}
