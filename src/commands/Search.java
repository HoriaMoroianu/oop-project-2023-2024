package commands;

import entities.Filter;
import fileio.input.CommandInput;

public class Search extends Command {
    private String type;
    private Filter filter;

    public Search(final CommandInput commandInput) {
        super(commandInput);
        type = commandInput.getType();
        filter = new Filter(commandInput.getFilter());
    }

    @Override
    public void executeCommand() {
        switch (type) {
            case "song":
                // Array-Song
                // aplica filtre pe fiecare melodie
                break;
            case "playlist":
                // Array-Song
                // aplica filtre pe fiecare melodie
                break;
            case "podcast":
                // Array-podcasturi
                // aplica filtre pe fiecare podcast
                break;
        }
    }
}
