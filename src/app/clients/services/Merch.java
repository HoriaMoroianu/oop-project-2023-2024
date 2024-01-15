package app.clients.services;

import app.clients.Artist;
import lombok.Getter;

@Getter
public class Merch {
    private final String name;
    private final String description;
    private final Integer price;
    private final Artist owner;

    public Merch(final String name, final String description,
                 final Integer price, final Artist owner) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.owner = owner;
    }
}
