package entities;

import fileio.input.UserInput;

public class User {
    private String username;
    private int age;
    private String city;

    public User() {
    }

    public User(final UserInput userInput) {
        username = userInput.getUsername();
        age = userInput.getAge();
        city = userInput.getCity();
    }
}
