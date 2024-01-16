package app.clients.services;

public interface Observer {
    /**
     * Adds the received notification to the user's notification log
     * @param type the type of notification that was received
     * @param creatorName the name of the content creator from whom the notification was received
     */
    void updateNotifications(String type, String creatorName);
}
