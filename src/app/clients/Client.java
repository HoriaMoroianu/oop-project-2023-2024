package app.clients;

import fileio.input.UserInput;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public abstract class Client implements GuestObserver {
    public enum GuestMode {
        ADD_GUEST,
        REMOVE_GUEST
    }

    protected final String username;
    protected final Integer age;
    protected final String city;
    protected ArrayList<Client> contentGuests = new ArrayList<>();

    public Client(final UserInput userInput) {
        username = userInput.getUsername();
        age = userInput.getAge();
        city = userInput.getCity();
    }

    /**
     * Updates the guest list of this client
     * @param mode for setting the list update mode - add/remove guest
     * @param guest that interacts with the content
     */
    public void updateGuests(final GuestMode mode, final Client guest) {
        if (this == guest) {
            // Client is not counted when he accesses his own content
            return;
        }

        switch (mode) {
            case ADD_GUEST -> contentGuests.add(guest);
            case REMOVE_GUEST -> contentGuests.remove(guest);
            default -> System.err.println("Wrong update mode");
        }
    }

    /**
     * Deletes this client and all his entries from the application
     */
    public abstract void deleteClient();
}
