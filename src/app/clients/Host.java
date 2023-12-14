package app.clients;

import app.management.Library;
import fileio.input.UserInput;

public class Host extends Client {
    public Host(final UserInput userInput) {
        super(userInput);
    }

    @Override
    public void deleteClient() {
        Library.getLibrary().getHosts().remove(username);
    }
}
