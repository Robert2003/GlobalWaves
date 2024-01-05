package app.commands.executables;

import static app.Constants.DOESNT_EXIST;
import static app.Constants.NOT_HOST_ERROR_MESSAGE;
import static app.Constants.NO_ANNOUNCEMENT_ERROR_MESSAGE;
import static app.Constants.REMOVE_ANNOUNCEMENT_NO_ERROR_MESSAGE;
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

public final class RemoveAnnouncement implements Executable {
  @Override
  public Node execute(final InputNode command) {
    User host = Library.getInstance().getUserByName(command.getUsername());

    if (host == null) {
      return new RemoveAnnouncementOutputNode(
          command, THE_USERNAME + command.getUsername() + DOESNT_EXIST);
    }

    if (host.getUserType() != UserType.HOST) {
      return new RemoveAnnouncementOutputNode(
          command, command.getUsername() + NOT_HOST_ERROR_MESSAGE);
    }

    if (host.getAnnouncementByName(command.getName()) == null) {
      return new RemoveAnnouncementOutputNode(
          command, command.getUsername() + NO_ANNOUNCEMENT_ERROR_MESSAGE);
    }

    Announcement announcement = host.getAnnouncementByName(command.getName());
    host.getAnnouncements().remove(announcement);
    return new RemoveAnnouncementOutputNode(
        command, command.getUsername() + REMOVE_ANNOUNCEMENT_NO_ERROR_MESSAGE);
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private final class RemoveAnnouncementOutputNode extends Node {
    private String user;
    private String message;

    RemoveAnnouncementOutputNode(final InputNode command, final String message) {
      this.setCommand(command.getCommand());
      this.setTimestamp(command.getTimestamp());
      this.setUser(command.getUsername());
      this.setMessage(message);
    }
  }
}
