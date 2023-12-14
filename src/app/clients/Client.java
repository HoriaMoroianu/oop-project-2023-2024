package app.clients;

import fileio.input.UserInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public abstract class Client {
    public enum UpdateMode {
        ADD_GUEST,
        REMOVE_GUEST
    }

    protected final String username;
    protected final int age;
    protected final String city;
    protected ArrayList<Client> contentGuests = new ArrayList<>();

    public Client(final UserInput userInput) {
        username = userInput.getUsername();
        age = userInput.getAge();
        city = userInput.getCity();
    }

    public void updateGuests(final UpdateMode mode, final Client guest) {
        if (this == guest) {
            // client is not counted when he accesses his own content
            return;
        }

        switch (mode) {
            case ADD_GUEST -> contentGuests.add(guest);
            case REMOVE_GUEST -> contentGuests.remove(guest);
            default -> System.err.println("Wrong update mode");
        }
    }

    public abstract void deleteClient();
}
