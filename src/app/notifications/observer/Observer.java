package app.notifications.observer;

import app.notifications.Notification;

public interface Observer {
  /**
   * Updates the observer with a new notification.
   *
   * @param notification The new notification.
   */
  void update(Notification notification);
}
