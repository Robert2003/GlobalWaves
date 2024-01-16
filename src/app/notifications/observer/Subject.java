package app.notifications.observer;

import app.notifications.Notification;

public interface Subject {
  /**
 * Adds an observer to the subject's list of observers.
 *
 * @param observer The observer to be added.
 */
void addObserver(Observer observer);

/**
 * Removes an observer from the subject's list of observers.
 *
 * @param observer The observer to be removed.
 */
void removeObserver(Observer observer);

/**
 * Notifies all observers about a state change.
 *
 * @param notification The notification about the state change.
 */
void notifyObservers(Notification notification);
}
