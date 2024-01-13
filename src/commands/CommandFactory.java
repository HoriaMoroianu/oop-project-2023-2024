package commands;

import fileio.input.CommandInput;

public final class CommandFactory {
    private CommandFactory() {
        throw new UnsupportedOperationException("This utility class and cannot be instantiated");
    }

    /**
     * It receives the data for a command and creates, based on its type, a new executable one
     * @param commandInput the command data read from the input file
     * @return the parsed command ready for execution
     * @throws Exception if the command type is unknown
     */
    public static CommandStrategy createCommand(final CommandInput commandInput) throws Exception {
        return switch (commandInput.getCommand()) {
            case "search", "select" -> new SearchbarCommands(commandInput);
            case "wrapped" -> new WrappedCommands(commandInput);

            case "load", "playPause", "repeat", "shuffle", "forward", "backward", "next", "prev",
                 "like", "addRemoveInPlaylist", "status"
                -> new PlayerCommands(commandInput);

            case "addUser", "deleteUser", "showPlaylists", "showAlbums", "showPodcasts"
                -> new AdminCommands(commandInput);

            case "getAllUsers", "getOnlineUsers", "getTop5Songs", "getTop5Albums",
                 "getTop5Playlists", "getTop5Artists"
                -> new StatisticsCommands(commandInput);

            case "createPlaylist", "follow", "showPreferredSongs", "switchVisibility",
                 "switchConnectionStatus", "changePage", "printCurrentPage"
                -> new UserCommands(commandInput);

            case "addAlbum", "removeAlbum", "addEvent", "removeEvent", "addMerch"
                -> new ArtistCommands(commandInput);

            case "addPodcast", "removePodcast", "addAnnouncement", "removeAnnouncement"
                -> new HostCommands(commandInput);

            default
                -> throw new Exception("Command " + commandInput.getCommand() + " is unknown!");
        };
    }
}
