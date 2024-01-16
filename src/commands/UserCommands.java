package commands;

import app.audio.collections.Playlist;
import app.audio.files.Song;
import app.clients.Artist;
import app.clients.Client;
import app.clients.Host;
import app.clients.User;
import app.clients.services.Merch;
import app.clients.services.Page;
import app.clients.services.SearchBar;
import app.management.Library;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.HashMap;

public final class UserCommands extends CommandStrategy {
    private User user;
    private final String playlistName;
    private final Integer playlistId;
    private final String nextPage;
    private final String name;

    public UserCommands(final CommandInput commandInput) {
        super(commandInput);
        playlistName = commandInput.getPlaylistName();
        playlistId = commandInput.getPlaylistId();
        nextPage = commandInput.getNextPage();
        name = commandInput.getName();
    }

    @Override
    protected ObjectNode executeCommand() {
        user = Library.getLibrary().getUsers().get(username);

        return switch (command) {
            case "createPlaylist" -> createPlaylist();
            case "follow" -> followPlaylist();
            case "showPreferredSongs" -> showPreferredSongs();
            case "switchVisibility" -> switchVisibility();
            case "switchConnectionStatus" -> switchConnectionStatus();
            case "changePage" -> changePage();
            case "printCurrentPage" -> printCurrentPage();
            case "buyMerch" -> buyMerch();
            case "seeMerch" -> seeMerch();
            case "subscribe" -> subscribeToCreator();
            case "getNotifications" -> seeNotifications();
            default -> null;
        };
    }

    private ObjectNode createPlaylist() {
        for (Playlist playlist : user.getPlaylists()) {
            if (playlist.getName().equals(playlistName)) {
                message = "A playlist with the same name already exists.";
                outputNode.put("message", message);
                return outputNode;
            }
        }

        Playlist playlist = new Playlist(playlistName, username);
        user.getPlaylists().add(playlist);
        Library.getLibrary().getPlaylists().add(playlist);

        message = "Playlist created successfully.";
        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode followPlaylist() {
        SearchBar searchBar = user.getSearchBar();

        if (searchBar.getSelectedTrack() == null) {
            message = "Please select a source before following or unfollowing.";
            outputNode.put("message", message);
            return outputNode;
        }

        if (!searchBar.getSelectedTrack().getType().equals("playlist")) {
            message = "The selected source is not a playlist.";
            outputNode.put("message", message);
            return outputNode;
        }

        Playlist playlist = (Playlist) searchBar.getSelectedTrack();

        if (playlist.getOwner().equals(username)) {
            message = "You cannot follow or unfollow your own playlist.";
            outputNode.put("message", message);
            return outputNode;
        }

        if (user.getFollowedPlaylists().contains(playlist)) {
            user.getFollowedPlaylists().remove(playlist);
            playlist.unfollow();
            message = "Playlist unfollowed successfully.";
        } else {
            user.getFollowedPlaylists().add(playlist);
            playlist.follow();
            message = "Playlist followed successfully.";
        }

        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode showPreferredSongs() {
        ArrayList<String> result = new ArrayList<>();
        ArrayList<Song> songs = Library.getLibrary().getUsers().get(username).getLikedSongs();
        songs.forEach(song -> result.add(song.getName()));

        outputNode.put("result", objectMapper.valueToTree(result));
        return outputNode;
    }

    private ObjectNode switchVisibility() {
        if (user.getPlaylists().size() < playlistId) {
            message = "The specified playlist ID is too high.";
            outputNode.put("message", message);
            return outputNode;
        }

        Playlist playlist = user.getPlaylists().get(playlistId - 1);

        if (playlist.getVisibility().equals("public")) {
            playlist.setVisibility("private");
            message = "Visibility status updated successfully to private.";
        } else {
            playlist.setVisibility("public");
            message = "Visibility status updated successfully to public.";
        }

        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode switchConnectionStatus() {
        Client client = Library.getLibrary().getClient(username);

        if (client == null) {
            message = "The username " + username + " doesn't exist.";
            outputNode.put("message", message);
            return outputNode;
        }

        if (!client.getType().equals("user")) {
            message = username + " is not a normal user.";
            outputNode.put("message", message);
            return outputNode;
        }

        ((User) client).switchOnlineStatus();
        message = username + " has changed status successfully.";
        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode changePage() {
        switch (nextPage) {
            case "Home":
                user.getCurrentPage().switchPage(Page.Type.HOME, user, user);
                message = username + " accessed " + nextPage + " successfully.";
                break;
            case "LikedContent":
                user.getCurrentPage().switchPage(Page.Type.LIKED_CONTENT, user, user);
                message = username + " accessed " + nextPage + " successfully.";
                break;
            default:
                message = username + " is trying to access a non-existent page.";
                break;
        }
        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode printCurrentPage() {
        Page page = user.getCurrentPage();

        switch (page.getType()) {
            case HOME:
                message = page.printHomePage();
                break;
            case LIKED_CONTENT:
                message = page.printLikedContentPage();
                break;
            case ARTIST:
                message = page.printArtistPage();
                break;
            case HOST:
                message = page.printHostPage();
                break;
            default:
                break;
        }
        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode buyMerch() {
        if (user == null) {
            message = "The username " + username + " doesn't exist.";
            outputNode.put("message", message);
            return outputNode;
        }

        if (!user.getCurrentPage().getType().equals(Page.Type.ARTIST)) {
            message = "Cannot buy merch from this page.";
            outputNode.put("message", message);
            return outputNode;
        }

        Merch merch = Library.getLibrary().getAppMerch().get(name);
        if (merch == null) {
            message = "The merch " + name + " doesn't exist.";
            outputNode.put("message", message);
            return outputNode;
        }

        Artist artist = merch.getOwner();
        artist.setMerchRevenue(artist.getMerchRevenue() + merch.getPrice());
        Library.getLibrary().getEndProgramArtists().add(artist);

        user.getOwnedMerch().add(merch.getName());

        message = username + " has added new merch successfully.";
        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode seeMerch() {
        if (user == null) {
            message = "The username " + username + " doesn't exist.";
            outputNode.put("message", message);
            return outputNode;
        }

        outputNode.put("result", objectMapper.valueToTree(user.getOwnedMerch()));
        return outputNode;
    }

    private ObjectNode subscribeToCreator() {
        if (user == null) {
            message = "The username " + username + " doesn't exist.";
            outputNode.put("message", message);
            return outputNode;
        }

        Page userPage = user.getCurrentPage();

        if (!userPage.getType().equals(Page.Type.ARTIST)
                && !userPage.getType().equals(Page.Type.HOST)) {
            message = "To subscribe you need to be on the page of an artist or host.";
            outputNode.put("message", message);
            return outputNode;
        }

        Client client = userPage.getPageOwner();
        if (user.getFollowedCreators().contains(client)) {
            // Unsubscribe
            if (client.getType().equals("artist")) {
                ((Artist) client).unsubscribe(user);
            } else {
                ((Host) client).unsubscribe(user);
            }

            user.getFollowedCreators().remove(client);
            message = username + " unsubscribed from " + client.getUsername() + " successfully.";
            outputNode.put("message", message);
            return outputNode;
        }

        // Subscribe
        if (client.getType().equals("artist")) {
            ((Artist) client).subscribe(user);
        } else {
            ((Host) client).subscribe(user);
        }

        user.getFollowedCreators().add(client);
        message = username + " subscribed to " + client.getUsername() + " successfully.";
        outputNode.put("message", message);
        return outputNode;
    }

    private ObjectNode seeNotifications() {
        ArrayList<ObjectNode> dataNodes = new ArrayList<>();

        for (HashMap<String, String> notification : user.getNotifications()) {
            ObjectNode node = objectMapper.createObjectNode();

            node.put("name", notification.keySet().stream().findFirst().orElse(null));
            node.put("description", notification.values().stream().findFirst().orElse(null));

            dataNodes.add(node);
        }

        user.getNotifications().clear();
        outputNode.put("notifications", objectMapper.valueToTree(dataNodes));
        return outputNode;
    }
}
