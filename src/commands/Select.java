package commands;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.AudioPlayable;
import entities.Library;
import fileio.input.CommandInput;
import lombok.Getter;

import java.util.ArrayList;

public final class Select extends Command {
    private Integer itemNumber;
    @Getter
    private String message;

    public Select(final CommandInput commandInput) {
        super(commandInput);
        itemNumber = commandInput.getItemNumber();
    }

    @Override
    public ObjectNode executeCommand() {
        ArrayList<AudioPlayable> audioPlayables =
                Library.getLibrary().getUsers()
                        .get(this.getUsername())
                        .getSearchBar()
                        .getAudioPlayables();

        if (audioPlayables.isEmpty()) {
            message = "Please conduct a search before making a selection.";
            return new ObjectMapper().valueToTree(this);
        }

        if (itemNumber > audioPlayables.size()) {
            message = "The selected ID is too high.";
            return new ObjectMapper().valueToTree(this);
        }

        message = "Successfully selected " + audioPlayables.get(itemNumber - 1).getName() + ".";
        return new ObjectMapper().valueToTree(this);
    }
}
