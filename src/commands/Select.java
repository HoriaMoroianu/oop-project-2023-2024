package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.AudioPlayable;
import entities.Library;
import entities.SearchBar;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;

public final class Select extends Command {
    private Integer itemNumber;
    @Getter
    private String message;

    public Select(final CommandInput commandInput) {
        super(commandInput);
        itemNumber = commandInput.getItemNumber();
    }

    @Override
    public ObjectNode executeCommand() {
        SearchBar searchBar = Library.getLibrary().getUsers()
                                    .get(this.getUsername())
                                    .getSearchBar();

        ArrayList<AudioPlayable> searchResults = searchBar.getSearchResults();

        if (searchResults.isEmpty()) {
            message = "Please conduct a search before making a selection.";
            return new ObjectMapper().valueToTree(this);
        }

        if (itemNumber > searchResults.size()) {
            message = "The selected ID is too high.";
            return new ObjectMapper().valueToTree(this);
        }

        searchBar.setSelectedAudio(searchResults.get(itemNumber - 1));
        searchResults.clear();

        message = "Successfully selected " + searchBar.getSelectedAudio().getName() + ".";
        return new ObjectMapper().valueToTree(this);
    }
}
