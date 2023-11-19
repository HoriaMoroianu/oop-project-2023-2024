package commands;

import entities.SearchFilters;
import fileio.input.CommandInput;

public class Search extends Command {
    private String type;
    private SearchFilters filters;

    public Search(final CommandInput commandInput) {
        super(commandInput);
        this.type = commandInput.getType();
        this.filters = new SearchFilters(commandInput.getFilters());
    }

    @Override
    public void executeCommand() {

    }
}
