package commands.statistics;

import app.management.Library;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter @JsonInclude(JsonInclude.Include.NON_NULL)
public final class GetOnlineUsers extends Command {
    private ArrayList<String> result;
    public GetOnlineUsers(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        result = Library.getLibrary().getOnlineUsers();
        return new ObjectMapper().valueToTree(this);
    }
}
