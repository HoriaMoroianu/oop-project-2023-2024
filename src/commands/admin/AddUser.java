package commands.admin;

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
        if (Library.getLibrary().getUsers().containsKey(username)) {
            message = "The username " + username + " is already taken.";
            return new ObjectMapper().valueToTree(this);
        }

        UserInput userInput = new UserInput();
        userInput.setUsername(username);
        userInput.setAge(age);
        userInput.setCity(city);

        // TODO: other users

        switch (type) {
            case "user":
                Library.getLibrary().getUsers().put(username, new User(userInput));
                break;
            default:
        }
        message = "The username " + username + " has been added successfully.";
        return new ObjectMapper().valueToTree(this);
    }
}
