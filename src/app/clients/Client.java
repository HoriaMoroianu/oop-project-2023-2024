package app.clients;

import fileio.input.UserInput;

public abstract class Client {
    protected final String username;
    protected final int age;
    protected final String city;

    public Client(final UserInput userInput) {
        username = userInput.getUsername();
        age = userInput.getAge();
        city = userInput.getCity();
    }
}
