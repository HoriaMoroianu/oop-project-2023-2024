package commands.page;

import app.Page;
import app.clients.User;
import app.management.Library;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;

public final class PrintCurrentPage extends Command {
    public PrintCurrentPage(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        User user = Library.getLibrary().getUsers().get(username);
        Page page = user.getCurrentPage();

        // TODO: restul de cazuri

        switch (page.getType()) {
            case HOME:
                message = page.printHomePage();
                break;
            case LIKED_CONTENT:
                message = null;
                break;
            case ARTIST:
                message = page.printArtistPage();
                break;
            case HOST:
                message = null;
                break;
            default:
                break;
        }

        return new ObjectMapper().valueToTree(this);
    }
}
