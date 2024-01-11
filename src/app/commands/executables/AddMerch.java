package app.commands.executables;

import static app.Constants.ADD_MERCHANDISE_NO_ERROR_MESSAGE;
import static app.Constants.DOESNT_EXIST;
import static app.Constants.HAS_MERCHANDISE_ERROR_MESSAGE;
import static app.Constants.NOT_ARTIST_ERROR_MESSAGE;
import static app.Constants.PRICE_ERROR_MESSAGE;
import static app.Constants.THE_USERNAME;

import app.commands.Executable;
import app.helpers.UserType;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.notifications.Notification;
import app.notifications.observer.NotificationType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.Library;
import library.entities.Merch;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public final class AddMerch implements Executable {
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return new AddMerchOutputNode(command, THE_USERNAME + command.getUsername() + DOESNT_EXIST);
    }

    if (user.getUserType() != UserType.ARTIST) {
      return new AddMerchOutputNode(command, command.getUsername() + NOT_ARTIST_ERROR_MESSAGE);
    }

    if (hasMerch(command)) {
      return new AddMerchOutputNode(command, command.getUsername() + HAS_MERCHANDISE_ERROR_MESSAGE);
    }

    if (command.getPrice() < 0) {
      return new AddMerchOutputNode(command, PRICE_ERROR_MESSAGE);
    }

    Merch merch = new Merch(command);
    user.getMerch().add(merch);

    Notification notification = new Notification(NotificationType.NEW_MERCHANDISE, "New Merchandise from " + user.getUsername() + ".");
    user.notifyObservers(notification);

    return new AddMerchOutputNode(
        command, command.getUsername() + ADD_MERCHANDISE_NO_ERROR_MESSAGE);
  }

  private boolean hasMerch(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());
    for (Merch merch : user.getMerch()) {
      if (merch.getName().equals(command.getName())
          && merch.getOwner().equals(command.getUsername())) {
        return true;
      }
    }
    return false;
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private final class AddMerchOutputNode extends Node {
    private String user;
    private String message;

    AddMerchOutputNode(final InputNode command, final String message) {
      this.setCommand(command.getCommand());
      this.setTimestamp(command.getTimestamp());
      this.setUser(command.getUsername());
      this.setMessage(message);
    }
  }
}
