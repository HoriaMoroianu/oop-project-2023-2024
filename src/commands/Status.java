package commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.MusicPlayer;
import fileio.input.CommandInput;

public final class Status extends Command {
    @JsonProperty ("stats")
    private MusicPlayer musicPlayer;

    public Status(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public ObjectNode executeCommand() {
        musicPlayer = Library.getLibrary().getUsers().get(this.getUsername()).getMusicPlayer();
        musicPlayer.updateMusicPlayer();
        return new ObjectMapper().valueToTree(this);
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }
}
