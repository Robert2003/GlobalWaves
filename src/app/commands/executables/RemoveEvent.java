package app.commands.executables;

import static app.Constants.DOESNT_EXIST;
import static app.Constants.NOT_ARTIST_ERROR_MESSAGE;
import static app.Constants.NO_EVENT_ERROR_MESSAGE;
import static app.Constants.REMOVE_EVENT_NO_ERROR_MESSAGE;
import static app.Constants.THE_USERNAME;

import app.commands.Executable;
import app.helpers.UserType;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.Library;
import library.entities.Event;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public final class RemoveEvent implements Executable {
  @Override
  public Node execute(final InputNode command) {
    User artist = Library.getInstance().getUserByName(command.getUsername());

    if (artist == null) {
      return new RemoveEventOutputNode(
          command, THE_USERNAME + command.getUsername() + DOESNT_EXIST);
    }

    if (artist.getUserType() != UserType.ARTIST) {
      return new RemoveEventOutputNode(command, command.getUsername() + NOT_ARTIST_ERROR_MESSAGE);
    }

    if (artist.getEventByName(command.getName()) == null) {
      return new RemoveEventOutputNode(
          command, command.getUsername() + NO_EVENT_ERROR_MESSAGE);
    }

    Event event = artist.getEventByName(command.getName());
    artist.getEvents().remove(event);
    return new RemoveEventOutputNode(
        command, command.getUsername() + REMOVE_EVENT_NO_ERROR_MESSAGE);
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private final class RemoveEventOutputNode extends Node {
    private String user;
    private String message;

    RemoveEventOutputNode(final InputNode command, final String message) {
      this.setCommand(command.getCommand());
      this.setTimestamp(command.getTimestamp());
      this.setUser(command.getUsername());
      this.setMessage(message);
    }
  }
}
