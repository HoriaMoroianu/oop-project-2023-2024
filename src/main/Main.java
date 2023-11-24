package main;

import checker.Checker;
import checker.CheckerConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.*;
import entities.Library;
import fileio.input.CommandInput;
import fileio.input.LibraryInput;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {
    static final String LIBRARY_PATH = CheckerConstants.TESTS_PATH + "library/library.json";

    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.getName().startsWith("library")) {
                continue;
            }

            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePathInput for input file
     * @param filePathOutput for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePathInput,
                              final String filePathOutput) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        LibraryInput library = objectMapper.readValue(new File(LIBRARY_PATH), LibraryInput.class);

        ArrayNode outputs = objectMapper.createArrayNode();

        // TODO add your implementation
        File inputFile = new File(CheckerConstants.TESTS_PATH + filePathInput);
        List<CommandInput> commands = objectMapper.readValue(inputFile, new TypeReference<>() { });

        ArrayList<Command> executableCommands = new ArrayList<>();

        for (CommandInput commandInput : commands) {
            switch (commandInput.getCommand()) {
                case "search" -> executableCommands.add(new Search(commandInput));
                case "select" -> executableCommands.add(new Select(commandInput));
                case "load" -> executableCommands.add(new Load(commandInput));
                case "status" -> executableCommands.add(new Status(commandInput));
                case "playPause" -> executableCommands.add(new PlayPause(commandInput));
                case "createPlaylist" -> executableCommands.add(new CreatePlaylist(commandInput));
                case "like" -> executableCommands.add(new Like(commandInput));
                case "repeat" -> executableCommands.add(new Repeat(commandInput));
                case "shuffle" -> executableCommands.add(new Shuffle(commandInput));
                case "showPlaylists" -> executableCommands.add(new ShowPlaylists(commandInput));
                case "follow" -> executableCommands.add(new FollowPlaylist(commandInput));
                case "switchVisibility" -> executableCommands
                        .add(new SwitchVisibility(commandInput));
                case "showPreferredSongs" -> executableCommands
                        .add(new ShowPreferredSongs(commandInput));
                case "addRemoveInPlaylist" -> executableCommands
                        .add(new AddRemoveInPlaylist(commandInput));
                default -> { }
            }
        }

        Library.getLibrary().loadLibrary(library);
        executableCommands.stream().map(Command::performCommand).forEach(outputs::add);
        Library.getLibrary().clearLibrary();

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePathOutput), outputs);
    }
}
