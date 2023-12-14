package commands.statistics;

import app.clients.Client;
import app.management.Library;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter @JsonInclude(JsonInclude.Include.NON_NULL)
public final class GetAllUsers extends Command {
    private final ArrayList<String> result = new ArrayList<>();

    public GetAllUsers(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        Library.getLibrary().getUsers().values()
                .stream().map(Client::getUsername).forEach(result::add);

        Library.getLibrary().getArtists().values()
                .stream().map(Client::getUsername).forEach(result::add);

        Library.getLibrary().getHosts().values()
                .stream().map(Client::getUsername).forEach(result::add);

        return new ObjectMapper().valueToTree(this);
    }
}
