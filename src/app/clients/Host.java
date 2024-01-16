package app.clients;

import app.clients.services.Announcement;
import app.audio.collections.Podcast;
import app.management.Library;
import fileio.input.UserInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Host extends Client {
    private final ArrayList<Podcast> podcasts = new ArrayList<>();
    private final ArrayList<Announcement> announcements = new ArrayList<>();
    private final ArrayList<User> subscribedUsers = new ArrayList<>();

    public Host(final UserInput userInput) {
        super(userInput);
    }

    /**
     * @return type of this host
     */
    @Override
    public String getType() {
        return "host";
    }

    /**
     * Deletes this host and all his entries from the application
     */
    @Override
    public void deleteClient() {
        Library.getLibrary().getHosts().remove(username);
    }

    /**
     * Get a list with the names of all the podcasts of this host
     * @return the list with podcast's names
     */
    public ArrayList<String> getPodcastsNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            names.add(podcast.getName());
        }
        return names;
    }

    /**
     * Get a list with the names of all the announcements of this host
     * @return the list with announcement's names
     */
    public ArrayList<String> getAnnouncementsNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Announcement announcement : announcements) {
            names.add(announcement.getName());
        }
        return names;
    }
}
