package fileio.input;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter @Setter @NoArgsConstructor
public final class CommandInput {
    private String command;
    private String username;
    private Integer timestamp;
    private String type;
    private FiltersInput filters;
    private Integer itemNumber;
    private String playlistName;
    private Integer playlistId;
    private Integer seed;
}
