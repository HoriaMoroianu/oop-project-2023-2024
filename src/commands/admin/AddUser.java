package commands.admin;

import app.clients.Artist;
import app.clients.Host;
import app.clients.User;
import app.management.Library;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;
import fileio.input.UserInput;

public final class AddUser extends Command {
    private final String type;
    private final Integer age;
    private final String city;

    public AddUser(final CommandInput commandInput) {
        super(commandInput);
        type = commandInput.getType();
        age = commandInput.getAge();
        city = commandInput.getCity();
    }

    @Override
    protected ObjectNode executeCommand() {
        if (Library.getLibrary().getClient(username) != null) {
            message = "The username " + username + " is already taken.";
            return new ObjectMapper().valueToTree(this);
        }

        UserInput userInput = new UserInput();
        userInput.setUsername(username);
        userInput.setAge(age);
        userInput.setCity(city);

        switch (type) {
            case "user" -> Library.getLibrary().getUsers().put(username, new User(userInput));
            case "artist" -> Library.getLibrary().getArtists().put(username, new Artist(userInput));
            case "host" -> Library.getLibrary().getHosts().put(username, new Host(userInput));
            default -> { }
        }

        message = "The username " + username + " has been added successfully.";
        return new ObjectMapper().valueToTree(this);
    }
}
