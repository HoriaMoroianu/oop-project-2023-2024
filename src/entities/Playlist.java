package entities;

public class Playlist extends AudioPlayable {
    private String name;
    private String owner;

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }
}
