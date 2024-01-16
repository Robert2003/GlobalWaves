package app.notifications;

import app.notifications.observer.NotificationType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public final class Notification {
  @JsonIgnore private NotificationType type;
  private String name;
  private String description;

  public Notification(final String name, final String description) {
    this.name = name;
    this.description = description;
  }

  public Notification(final NotificationType type, final String description) {
    this.type = type;
    if (type == NotificationType.PLAYLIST_SUBSCRIPTION) {
      this.name = "Playlist Subscription";
    } else if (type == NotificationType.NEW_EVENT) {
      this.name = "New Event";
    } else if (type == NotificationType.NEW_ALBUM) {
      this.name = "New Album";
    } else if (type == NotificationType.NEW_MERCHANDISE) {
      this.name = "New Merchandise";
    } else if (type == NotificationType.NEW_ANNOUNCEMENT) {
      this.name = "New Announcement";
    } else if (type == NotificationType.NEW_PODCAST) {
      this.name = "New Podcast";
    }
    this.description = description;
  }
}
