package app.commands.executables;

import static app.Constants.DOESNT_EXIST;
import static app.Constants.NOT_NORMAL_USER_ERROR_MESSAGE;
import static app.Constants.SWITCH_CONNECTION_NO_ERROR_MESSAGE;
import static app.Constants.THE_USERNAME;

import app.commands.Executable;
import app.helpers.UserType;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.Library;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public final class SwitchConnectionStatus implements Executable {
  /**
   * Goes to the previous track based on the given input command.
   *
   * @param command The input command for going to the previous track.
   * @return The output node containing the previous track result.
   */
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return new SwitchConnectionOutputNode(
          command, THE_USERNAME + command.getUsername() + DOESNT_EXIST);
    }

    if (user.getUserType() != UserType.NORMAL) {
      return new SwitchConnectionOutputNode(
          command, command.getUsername() + NOT_NORMAL_USER_ERROR_MESSAGE);
    }

    user.setConnectionStatus(user.getConnectionStatus().next());
    return new SwitchConnectionOutputNode(
        command, command.getUsername() + SWITCH_CONNECTION_NO_ERROR_MESSAGE);
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private class SwitchConnectionOutputNode extends Node {
    private String user;
    private String message;

    /**
     * Constructs an OutputNode object with the given command and message.
     *
     * @param command The input command node.
     * @param message The output message.
     */
    SwitchConnectionOutputNode(final InputNode command, final String message) {
      this.setCommand(command.getCommand());
      this.setUser(command.getUsername());
      this.setTimestamp(command.getTimestamp());
      this.setMessage(message);
    }
  }
}
