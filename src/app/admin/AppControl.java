package app.admin;

import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.music_player.AddRemoveInPlaylist;
import commands.Command;
import commands.playlist.CreatePlaylist;
import commands.playlist.FollowPlaylist;
import commands.music_player.ForwardBackward;
import commands.statistics.GetTopPlaylists;
import commands.statistics.GetTopSongs;
import commands.music_player.Like;
import commands.music_player.Load;
import commands.music_player.Next;
import commands.music_player.PlayPause;
import commands.music_player.Previous;
import commands.music_player.Repeat;
import commands.search_bar.Search;
import commands.search_bar.Select;
import commands.playlist.ShowPlaylists;
import commands.statistics.ShowPreferredSongs;
import commands.music_player.Shuffle;
import commands.music_player.Status;
import commands.playlist.SwitchVisibility;
import fileio.input.CommandInput;
import fileio.input.LibraryInput;

import java.util.ArrayList;
import java.util.List;

public final class AppControl {
    private static AppControl instance = null;
    private final ArrayList<Command> commands = new ArrayList<>();

    /**
     * @return AppControl instance
     */
    public static AppControl getAppControl() {
        if (instance == null) {
            instance = new AppControl();
        }
        return instance;
    }

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
        for (CommandInput commandInput : commandInputArrayList) {
            switch (commandInput.getCommand()) {
                case "addRemoveInPlaylist" -> commands.add(new AddRemoveInPlaylist(commandInput));
                case "createPlaylist" -> commands.add(new CreatePlaylist(commandInput));
                case "follow" -> commands.add(new FollowPlaylist(commandInput));
                case "forward", "backward" -> commands.add(new ForwardBackward(commandInput));
                case "getTop5Playlists" -> commands.add(new GetTopPlaylists(commandInput));
                case "getTop5Songs" -> commands.add(new GetTopSongs(commandInput));
                case "like" -> commands.add(new Like(commandInput));
                case "load" -> commands.add(new Load(commandInput));
                case "next" -> commands.add(new Next(commandInput));
                case "playPause" -> commands.add(new PlayPause(commandInput));
                case "prev" -> commands.add(new Previous(commandInput));
                case "repeat" -> commands.add(new Repeat(commandInput));
                case "search" -> commands.add(new Search(commandInput));
                case "select" -> commands.add(new Select(commandInput));
                case "showPlaylists" -> commands.add(new ShowPlaylists(commandInput));
                case "showPreferredSongs" -> commands.add(new ShowPreferredSongs(commandInput));
                case "shuffle" -> commands.add(new Shuffle(commandInput));
                case "status" -> commands.add(new Status(commandInput));
                case "switchVisibility" -> commands.add(new SwitchVisibility(commandInput));
                default -> { }
            }
        }
    }
}
