package app.commands.executables;

import static app.Constants.ADD_USER_NO_ERROR_MESSAGE;
import static app.Constants.THE_USERNAME;
import static app.Constants.USERNAME_IS_TAKEN;

import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.Library;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public final class AddUser implements Executable {
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user != null) {
      return new AddUserOutputNode(
          command, THE_USERNAME + command.getUsername() + USERNAME_IS_TAKEN);
    }

    User newUser = new User(command);
    Library.getInstance().getUsers().add(newUser);
    return new AddUserOutputNode(
        command, THE_USERNAME + command.getUsername() + ADD_USER_NO_ERROR_MESSAGE);
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private final class AddUserOutputNode extends Node {
    private String user;
    private String message;

    AddUserOutputNode(final InputNode command, final String message) {
      this.setCommand(command.getCommand());
      this.setTimestamp(command.getTimestamp());
      this.setUser(command.getUsername());
      this.setMessage(message);
    }
  }
}
