package app.clients;

public interface GuestObserver {
    void updateGuests(final Client.GuestMode mode, final Client guest);
}
