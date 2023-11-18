package entities;

import fileio.input.UserInput;

public class User {
    private String username;
    private int age;
    private String city;

    public User() {
    }

    public User(final UserInput userInput) {
        this.username = userInput.getUsername();
        this.age = userInput.getAge();
        this.city = userInput.getCity();
    }
}
