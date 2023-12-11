package app.clients;

import app.Page;
import fileio.input.UserInput;
import lombok.Getter;

@Getter
public abstract class Client {
    protected final String username;
    protected final int age;
    protected final String city;
    protected Page page;

    public Client(final UserInput userInput) {
        username = userInput.getUsername();
        age = userInput.getAge();
        city = userInput.getCity();
    }
}
