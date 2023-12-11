package commands.page;

import app.Page;
import app.clients.User;
import app.management.Library;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;

public class PrintCurrentPage extends Command {
    public PrintCurrentPage(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        User user = Library.getLibrary().getUsers().get(username);
        Page page = user.getPage();

        // TODO: restul de cazuri

        switch (page.getType()) {
            case HOME:
                message = page.printHomePage(user);
                break;
            case LIKED_CONTENT:
                message = null;
                break;
            case ARTIST:
                message = null;
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
