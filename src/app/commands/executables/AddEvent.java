package app.commands.executables;

import static app.Constants.ADD_EVENT_NO_ERROR_MESSAGE;
import static app.Constants.DOESNT_EXIST;
import static app.Constants.EVENT_FOR;
import static app.Constants.HAS_EVENT_ERROR_MESSAGE;
import static app.Constants.INVALID_DATE_ERROR_MESSAGE;
import static app.Constants.NOT_ARTIST_ERROR_MESSAGE;
import static app.Constants.THE_USERNAME;

import app.commands.Executable;
import app.helpers.UserType;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.notifications.Notification;
import app.notifications.observer.NotificationType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import library.Library;
import library.entities.Event;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public final class AddEvent implements Executable {
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return new AddEventOutputNode(command, THE_USERNAME + command.getUsername() + DOESNT_EXIST);
    }

    if (user.getUserType() != UserType.ARTIST) {
      return new AddEventOutputNode(command, command.getUsername() + NOT_ARTIST_ERROR_MESSAGE);
    }

    if (hasEvent(command)) {
      return new AddEventOutputNode(command, command.getUsername() + HAS_EVENT_ERROR_MESSAGE);
    }

    if (!isDateValid(command.getDate())) {
      return new AddEventOutputNode(
          command, EVENT_FOR + command.getUsername() + INVALID_DATE_ERROR_MESSAGE);
    }

    Event event = new Event(command);
    user.getEvents().add(event);

    Notification notification =
        new Notification(NotificationType.NEW_EVENT, "New Event from " + user.getUsername() + ".");
    user.notifyObservers(notification);

    return new AddEventOutputNode(command, command.getUsername() + ADD_EVENT_NO_ERROR_MESSAGE);
  }

  private boolean hasEvent(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());
    for (Event event : user.getEvents()) {
      if (event.getName().equals(command.getName())
          && event.getOwner().equals(command.getUsername())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Source of inspiration: <a
   * href="https://www.baeldung.com/java-string-valid-date">SimpleDateFormat</a>
   */
  private boolean isDateValid(final String date) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    dateFormat.setLenient(false);

    try {
      dateFormat.parse(date);
      return true;
    } catch (ParseException e) {
      return false;
    }
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private final class AddEventOutputNode extends Node {
    private String user;
    private String message;

    AddEventOutputNode(final InputNode command, final String message) {
      this.setCommand(command.getCommand());
      this.setTimestamp(command.getTimestamp());
      this.setUser(command.getUsername());
      this.setMessage(message);
    }
  }
}
