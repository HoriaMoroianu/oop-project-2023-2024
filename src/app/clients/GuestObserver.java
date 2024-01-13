package app.clients;

public interface GuestObserver {
    /**
     * Updates the guest list of a client
     * @param mode for setting the list update mode - add/remove guest
     * @param guest that interacts with the content
     */
    void updateGuests(Client.GuestMode mode, Client guest);
}
