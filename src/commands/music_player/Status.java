package commands.music_player;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import app.management.Library;
import app.MusicPlayer;
import commands.Command;
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
