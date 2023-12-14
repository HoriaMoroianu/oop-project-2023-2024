package commands.admin;

import app.audio.collections.Podcast;
import app.management.Library;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter @JsonInclude(JsonInclude.Include.NON_NULL)
public final class ShowPodcasts extends Command {
    private final ArrayList<Podcast> result = new ArrayList<>();

    public ShowPodcasts(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    protected ObjectNode executeCommand() {
        result.addAll(Library.getLibrary().getHosts().get(username).getPodcasts());
        return new ObjectMapper().valueToTree(this);
    }
}
