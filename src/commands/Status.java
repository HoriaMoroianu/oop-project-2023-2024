package commands;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.MusicPlayer;
import fileio.input.CommandInput;
import lombok.Getter;

@Getter @JsonInclude(JsonInclude.Include.NON_NULL)
public final class Status extends Command {
    @JsonProperty ("stats")
    private MusicPlayer musicPlayer;

    public Status(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public ObjectNode executeCommand() {
        musicPlayer = Library.getLibrary().getUsers().get(username).getMusicPlayer();
        musicPlayer.updateMusicPlayer();
        return new ObjectMapper().valueToTree(this);
    }
}
