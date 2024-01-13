package app.clients.services;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class ClientStats {
    public enum ListenType {
        ARTIST,
        GENRE,
        SONG,
        ALBUM,
        EPISODE,
        LISTENER
    }

    private final HashMap<String, Integer> artistsListened = new HashMap<>();
    private final HashMap<String, Integer> genresListened = new HashMap<>();
    private final HashMap<String, Integer> songsListened = new HashMap<>();
    private final HashMap<String, Integer> albumsListened = new HashMap<>();
    private final HashMap<String, Integer> episodesListened = new HashMap<>();
    private final HashMap<String, Integer> listeners = new HashMap<>();

    public void addListen(final ListenType listenType, final String name) {
        switch (listenType) {
            case ARTIST ->
                artistsListened.put(name, artistsListened.getOrDefault(name, 0) + 1);
            case GENRE ->
                genresListened.put(name, genresListened.getOrDefault(name, 0) + 1);
            case SONG ->
                songsListened.put(name, songsListened.getOrDefault(name, 0) + 1);
            case ALBUM ->
                albumsListened.put(name, albumsListened.getOrDefault(name, 0) + 1);
            case EPISODE ->
                episodesListened.put(name, episodesListened.getOrDefault(name, 0) + 1);
            case LISTENER ->
                listeners.put(name, listeners.getOrDefault(name, 0) + 1);
        }
    }
}
