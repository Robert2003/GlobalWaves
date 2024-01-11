package app.notifications.observer;

import app.notifications.Notification;

public interface Observer {
	void update(Notification notification);
}
