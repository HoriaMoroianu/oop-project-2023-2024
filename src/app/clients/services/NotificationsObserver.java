package app.clients.services;

import app.clients.User;

import java.util.HashMap;

public class NotificationsObserver implements Observer {
    private final User user;
    public NotificationsObserver(final User user) {
        this.user = user;
    }

    /**
     * Adds the received notification to the user's notification log
     * @param type the type of notification that was received
     * @param creatorName the name of the content creator from whom the notification was received
     */
    @Override
    public void updateNotifications(final String type, final String creatorName) {
        HashMap<String, String> notification = new HashMap<>();
        notification.put(type, type + " from " + creatorName + ".");
        user.getNotifications().add(notification);
    }
}
