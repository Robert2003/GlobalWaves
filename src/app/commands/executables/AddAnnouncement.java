package app.commands.executables;

import static app.Constants.ADD_ANNOUNCEMENT_NO_ERROR_MESSAGE;
import static app.Constants.DOESNT_EXIST;
import static app.Constants.HAS_ANNOUNCEMENT_ERROR_MESSAGE;
import static app.Constants.NOT_HOST_ERROR_MESSAGE;
import static app.Constants.THE_USERNAME;

import app.commands.Executable;
import app.helpers.UserType;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.Library;
import library.entities.Announcement;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public final class AddAnnouncement implements Executable {
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return new AddAnnouncementOutputNode(
          command, THE_USERNAME + command.getUsername() + DOESNT_EXIST);
    }

    if (user.getUserType() != UserType.HOST) {
      return new AddAnnouncementOutputNode(command, command.getUsername() + NOT_HOST_ERROR_MESSAGE);
    }

    if (hasAnnouncement(command)) {
      return new AddAnnouncementOutputNode(
          command, command.getUsername() + HAS_ANNOUNCEMENT_ERROR_MESSAGE);
    }

    Announcement announcement = new Announcement(command);
    user.getAnnouncements().add(announcement);
    return new AddAnnouncementOutputNode(
        command, command.getUsername() + ADD_ANNOUNCEMENT_NO_ERROR_MESSAGE);
  }

  private boolean hasAnnouncement(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());
    for (Announcement announcement : user.getAnnouncements()) {
      if (announcement.getName().equals(command.getName())
          && announcement.getOwner().equals(command.getUsername())) {
        return true;
      }
    }
    return false;
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private final class AddAnnouncementOutputNode extends Node {
    private String user;
    private String message;

    AddAnnouncementOutputNode(final InputNode command, final String message) {
      this.setCommand(command.getCommand());
      this.setTimestamp(command.getTimestamp());
      this.setUser(command.getUsername());
      this.setMessage(message);
    }
  }
}
