package app.clients;

import app.Page;
import fileio.input.UserInput;

public class Host extends Client {
    public Host(final UserInput userInput) {
        super(userInput);
        page = new Page(Page.Type.HOST);
    }
}
