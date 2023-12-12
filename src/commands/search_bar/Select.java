package commands.search_bar;

import app.Page;
import app.clients.Artist;
import app.clients.Host;
import app.clients.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import app.audio.collections.AudioTrack;
import app.management.Library;
import app.SearchBar;
import commands.Command;
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
        User user = Library.getLibrary().getUsers().get(username);
        SearchBar searchBar = user.getSearchBar();
        Page currentPage = user.getCurrentPage();

        searchBar.setSelectedTrack(null);

        if (!searchBar.isSearchConducted()) {
            message = "Please conduct a search before making a selection.";
            return new ObjectMapper().valueToTree(this);
        }

        if (itemNumber > searchBar.getFoundResults()) {
            message = "The selected ID is too high.";
            return new ObjectMapper().valueToTree(this);
        }

        switch (searchBar.getSearchType()) {
            case "song", "playlist", "podcast", "album":
                ArrayList<AudioTrack> searchedTracks = searchBar.getSearchedTracks();
                searchBar.setSelectedTrack(searchedTracks.get(itemNumber - 1));
                message = "Successfully selected " + searchBar.getSelectedTrack().getName() + ".";
                break;

            case "artist":
                Artist artist = (Artist) searchBar.getSearchedClients().get((itemNumber - 1));
                currentPage.setType(Page.Type.ARTIST);
                currentPage.setPageOwner(artist);
                message = "Successfully selected " + artist.getUsername() + "'s page.";
                break;

            case "host":
                Host host = (Host) searchBar.getSearchedClients().get((itemNumber - 1));
                currentPage.setType(Page.Type.HOST);
                currentPage.setPageOwner(host);
                message = "Successfully selected " + host.getUsername() + "'s page.";
                break;

            default:
                break;
        }

        searchBar.clearSearchResults();
        return new ObjectMapper().valueToTree(this);
    }
}
