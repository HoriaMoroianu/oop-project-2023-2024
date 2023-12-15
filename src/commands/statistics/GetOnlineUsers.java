package commands.statistics;

import app.management.Library;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Getter @JsonInclude(JsonInclude.Include.NON_NULL)
public final class GetOnlineUsers extends Command {
    private ArrayList<String> result;
    public GetOnlineUsers(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        result = (ArrayList<String>) Library.getLibrary().getOnlineUsers()
                .stream().sorted(String::compareTo).collect(Collectors.toList());
        return new ObjectMapper().valueToTree(this);
    }
}
