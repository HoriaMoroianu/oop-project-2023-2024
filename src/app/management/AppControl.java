package app.management;

import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.CommandFactory;
import commands.CommandStrategy;
import fileio.input.CommandInput;
import fileio.input.LibraryInput;

import java.util.List;
import java.util.Objects;

public final class AppControl {
    /**
     * Loads the library, runs the commands and stores the result in the outputs
     * @param library input library database
     * @param commandInputArrayList list of input commands for execution
     * @param outputs commands outputs as ArrayNode
     */
    public void runApp(final LibraryInput library,
                       final List<CommandInput> commandInputArrayList,
                       final ArrayNode outputs) {

        Library.getLibrary().loadLibrary(library);

        CommandInput endCommand = new CommandInput();
        endCommand.setCommand("endProgram");
        commandInputArrayList.add(endCommand);

        commandInputArrayList.stream()
            .map(commandInput -> {
                try {
                    return CommandFactory.createCommand(commandInput);
                } catch (Exception commandException) {
                    System.err.println(commandException.getMessage());
                    return null;
                }
            }).filter(Objects::nonNull)
            .map(CommandStrategy::performCommand)
            .forEach(outputs::add);

        Library.getLibrary().clearLibrary();
    }
}
