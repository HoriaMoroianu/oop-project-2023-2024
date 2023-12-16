package commands.client;

import app.clients.services.Merch;
import app.clients.Artist;
import app.clients.Client;
import app.management.Library;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;

public final class AddMerch extends Command {
    private final String name;
    private final String description;
    private final Integer price;

    public AddMerch(final CommandInput commandInput) {
        super(commandInput);
        name = commandInput.getName();
        description = commandInput.getDescription();
        price = commandInput.getPrice();
    }

    @Override
    protected ObjectNode executeCommand() {
        Client client = Library.getLibrary().getClient(username);
        if (client == null) {
            message = "The username " + username + " doesn't exist.";
            return new ObjectMapper().valueToTree(this);
        }

        if (client.getClass() != Artist.class) {
            message = username + " is not an artist.";
            return new ObjectMapper().valueToTree(this);
        }
        Artist artist = (Artist) client;

        if (artist.getMerchesNames().contains(name)) {
            message = username + " has merchandise with the same name.";
            return new ObjectMapper().valueToTree(this);
        }

        if (price < 0) {
            message = "Price for merchandise can not be negative.";
            return new ObjectMapper().valueToTree(this);
        }

        artist.getMerches().add(new Merch(name, description, price));
        message = username + " has added new merchandise successfully.";
        return new ObjectMapper().valueToTree(this);
    }
}
