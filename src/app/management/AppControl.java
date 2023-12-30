package app.management;

import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.AdminCommands;
import commands.ArtistCommands;
import commands.Command;
import commands.HostCommands;
import commands.PlayerCommands;
import commands.SearchbarCommands;
import commands.StatisticsCommands;
import commands.UserCommands;
import fileio.input.CommandInput;
import fileio.input.LibraryInput;

import java.util.ArrayList;
import java.util.List;

public final class AppControl {
    private final ArrayList<Command> commands = new ArrayList<>();

    /**
     * Loads the library, runs the commands and stores the result in the outputs
     * @param library input library database
     * @param commandInputArrayList list of input commands for execution
     * @param outputs commands outputs as ArrayNode
     */
    public void runApp(final LibraryInput library, final List<CommandInput> commandInputArrayList,
                        final ArrayNode outputs) {
        Library.getLibrary().loadLibrary(library);
        parseCommandInput(commandInputArrayList);
        commands.stream().map(Command::performCommand).forEach(outputs::add);
        commands.clear();
        Library.getLibrary().clearLibrary();
    }

    private void parseCommandInput(final List<CommandInput> commandInputArrayList) {
        // TODO factory
        for (CommandInput commandInput : commandInputArrayList) {
            switch (commandInput.getCommand()) {
                case "search", "select" -> commands.add(new SearchbarCommands(commandInput));

                case "load", "playPause", "repeat", "shuffle", "forward", "backward", "next",
                     "prev", "like", "addRemoveInPlaylist", "status"
                        -> commands.add(new PlayerCommands(commandInput));

                case "addUser", "deleteUser", "showPlaylists", "showAlbums", "showPodcasts"
                        -> commands.add(new AdminCommands(commandInput));

                case "getAllUsers", "getOnlineUsers", "getTop5Songs", "getTop5Albums",
                     "getTop5Playlists", "getTop5Artists"
                        -> commands.add(new StatisticsCommands(commandInput));

                case "createPlaylist", "follow", "showPreferredSongs", "switchVisibility",
                     "switchConnectionStatus", "changePage", "printCurrentPage"
                        -> commands.add(new UserCommands(commandInput));

                case "addAlbum", "removeAlbum", "addEvent", "removeEvent", "addMerch"
                        -> commands.add(new ArtistCommands(commandInput));

                case "addPodcast", "removePodcast", "addAnnouncement", "removeAnnouncement"
                        -> commands.add(new HostCommands(commandInput));

                default -> System.err.println(commandInput.getCommand() + " command is unknown!");
            }
        }
    }
}
