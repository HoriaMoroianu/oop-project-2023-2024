package commands.page;

import app.clients.services.Page;
import app.clients.User;
import app.management.Library;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;

public final class ChangePage extends Command {
    private final String nextPage;
    public ChangePage(final CommandInput commandInput) {
        super(commandInput);
        nextPage = commandInput.getNextPage();
    }

    @Override
    protected ObjectNode executeCommand() {
        User user = Library.getLibrary().getUsers().get(username);

        switch (nextPage) {
            case "Home":
                user.getCurrentPage().switchPage(Page.Type.HOME, user, user);
                message = username + " accessed " + nextPage + " successfully.";
                break;
            case "LikedContent":
                user.getCurrentPage().switchPage(Page.Type.LIKED_CONTENT, user, user);
                message = username + " accessed " + nextPage + " successfully.";
                break;
            default:
                message = username + " is trying to access a non-existent page.";
                break;
        }

        return new ObjectMapper().valueToTree(this);
    }
}
