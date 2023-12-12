package app.clients;

import fileio.input.UserInput;
import lombok.Getter;

@Getter
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
