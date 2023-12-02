package fileio.input;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter @Setter @NoArgsConstructor
public final class CommandInput {
    private String command;
    private String username;
    private Integer timestamp;
    private String type;
    private FilterInput filters;
    private Integer itemNumber;
    private String playlistName;
    private Integer playlistId;
    private Integer seed;

    private Integer age;
    private String city;
    private String name;
    private Integer releaseYear;
    private String description;
    private ArrayList<SongInput> songs;
    private String date;
    private Integer price;
}
