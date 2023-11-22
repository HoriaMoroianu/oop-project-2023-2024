package entities;

public class Playlist implements AudioTrack {
    private String name;
    private String owner;

    public String getName() {
        return name;
    }

    @Override
    public AudioFile getAudioFile() {

        // TODO afla melodia curenta

        return null;
    }

    public String getOwner() {
        return owner;
    }


}
