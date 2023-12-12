package commands.client;

import app.Event;
import app.clients.Artist;
import app.clients.Client;
import app.management.Library;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;

public final class AddEvent extends Command {
    private final String name;
    private final String description;
    private final String date;


    private final int february = 2;
    private final int maxFebruaryDays = 28;
    private final int maxMonthDays = 31;
    private final int maxMonths = 12;
    private final int minYear = 1900;
    private final int maxYear = 2023;

    public AddEvent(final CommandInput commandInput) {
        super(commandInput);
        name = commandInput.getName();
        description = commandInput.getDescription();
        date = commandInput.getDate();
    }

    @Override
    protected ObjectNode executeCommand() {
        Client client = Library.getLibrary().getClient(username);
        if (client == null) {
            message = "The username " + username + " doesn't exist.";
            return new ObjectMapper().valueToTree(this);
        }
        if (client.getClass() != Artist.class) {
            message = username + " is not an artist.";
            return new ObjectMapper().valueToTree(this);
        }
        Artist artist = (Artist) client;

        if (artist.getEventsNames().contains(name)) {
            message = username + " has another event with the same name.";
            return new ObjectMapper().valueToTree(this);
        }

        if (!validDate(date)) {
            message = "Event for " + username + " does not have a valid date.";
            return new ObjectMapper().valueToTree(this);
        }

        artist.getEvents().add(new Event(name, description, date));
        message = username + " has added new event successfully.";
        return new ObjectMapper().valueToTree(this);
    }

    private boolean validDate(final String stringDate) {
        String[] splitDate = stringDate.split("-");

        int day = Integer.parseInt(splitDate[0]);
        int month = Integer.parseInt(splitDate[1]);
        int year = Integer.parseInt(splitDate[2]);

        return (month != february || day <= maxFebruaryDays)
                && day <= maxMonthDays
                && month <= maxMonths
                && year >= minYear && year <= maxYear;
    }
}
