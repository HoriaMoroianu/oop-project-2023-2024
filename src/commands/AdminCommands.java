package commands;

import app.clients.Artist;
import app.clients.Client;
import app.clients.Host;
import app.clients.User;
import app.management.Library;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import fileio.input.UserInput;
import fileio.output.AlbumOutput;
import fileio.output.PlaylistOutput;
import fileio.output.PodcastOutput;

import java.util.ArrayList;
import java.util.stream.Collectors;

public final class AdminCommands extends CommandStrategy {
    private final String type;
    private final Integer age;
    private final String city;

    public AdminCommands(final CommandInput commandInput) {
        super(commandInput);
        type = commandInput.getType();
        age = commandInput.getAge();
        city = commandInput.getCity();
    }

    @Override
    protected ObjectNode executeCommand() {
        return switch (command) {
            case "addUser" -> addUser();
            case "deleteUser" -> deleteUser();
            case "showPlaylists" -> showPlaylists();
            case "showAlbums" -> showAlbums();
            case "showPodcasts" -> showPodcasts();
            case "endProgram" -> endProgram();
            default -> null;
        };
    }

    private ObjectNode addUser() {
        if (Library.getLibrary().getClient(username) != null) {
            message = "The username " + username + " is already taken.";
            outputNode.put("message", message);
            return outputNode;
        }

        UserInput userInput = new UserInput();
        userInput.setUsername(username);
        userInput.setAge(age);
        userInput.setCity(city);

        switch (type) {
            case "user" -> Library.getLibrary().getUsers().put(username, new User(userInput));
            case "artist" -> Library.getLibrary().getArtists().put(username, new Artist(userInput));
            case "host" -> Library.getLibrary().getHosts().put(username, new Host(userInput));
            default -> { }
        }

        message = "The username " + username + " has been added successfully.";
        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode deleteUser() {
        Library.getLibrary().updateMusicPlayers();
        Client client = Library.getLibrary().getClient(username);

        if (client == null) {
            message = "The username " + username + " doesn't exist.";
            outputNode.put("message", message);
            return outputNode;
        }

        if (!client.getContentGuests().isEmpty()) {
            message = username + " can't be deleted.";
            outputNode.put("message", message);
            return outputNode;
        }

        client.deleteClient();
        message = username + " was successfully deleted.";
        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode showPlaylists() {
        ArrayList<PlaylistOutput> result = Library.getLibrary().getUsers().get(username)
                .getPlaylists().stream().map(PlaylistOutput::new)
                .collect(Collectors.toCollection(ArrayList::new));

        outputNode.put("result", objectMapper.valueToTree(result));
        return outputNode;
    }

    private ObjectNode showAlbums() {
        ArrayList<AlbumOutput> result = Library.getLibrary().getArtists().get(username)
                .getAlbums().stream().map(AlbumOutput::new)
                .collect(Collectors.toCollection(ArrayList::new));

        outputNode.put("result", objectMapper.valueToTree(result));
        return outputNode;
    }

    private ObjectNode showPodcasts() {
        ArrayList<PodcastOutput> result = Library.getLibrary().getHosts().get(username)
                .getPodcasts().stream().map(PodcastOutput::new)
                .collect(Collectors.toCollection(ArrayList::new));

        outputNode.put("result", objectMapper.valueToTree(result));
        return outputNode;
    }

    private ObjectNode endProgram() {
        ObjectNode artistsNode = objectMapper.createObjectNode();
        ObjectNode dataNode = objectMapper.createObjectNode();

        ArrayList<String> listenedArtists = Library.getLibrary().getUsers().values().stream()
                .flatMap(user -> user.getClientStats().getArtistsListened().keySet().stream())
                .distinct().sorted(String::compareTo)
                .collect(Collectors.toCollection(ArrayList::new));

        for (String artistName : listenedArtists) {
            // No monetization
            dataNode.put("merchRevenue", 0.0d);
            dataNode.put("songRevenue", 0.0d);
            dataNode.put("ranking", listenedArtists.indexOf(artistName) + 1);
            dataNode.put("mostProfitableSong", "N/A");

            artistsNode.put(artistName, dataNode);
            dataNode = objectMapper.createObjectNode();
        }

        outputNode.remove("timestamp");
        outputNode.remove("user");
        outputNode.put("result", artistsNode);
        return outputNode;
    }
}
