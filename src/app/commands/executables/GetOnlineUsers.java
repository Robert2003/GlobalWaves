package app.commands.executables;

import app.commands.Executable;
import app.helpers.ConnectionStatus;
import app.helpers.UserType;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;
import library.Library;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public final class GetOnlineUsers implements Executable {
  /**
   * Goes to the previous track based on the given input command.
   *
   * @param command The input command for going to the previous track.
   * @return The output node containing the previous track result.
   */
  @Override
  public Node execute(final InputNode command) {
    List<String> onlineUsers = new ArrayList<>();

    for (User user : Library.getInstance().getUsers()) {
      if (user.getConnectionStatus() == ConnectionStatus.ONLINE
          && user.getUserType() == UserType.NORMAL) {
        onlineUsers.add(user.getUsername());
      }
    }

    return new GetOnlineUsersOutputNode(command, onlineUsers);
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private class GetOnlineUsersOutputNode extends Node {
    private List<String> result;

    /**
     * Constructs an OutputNode object with the given command and message.
     *
     * @param command The input command node.
     */
    GetOnlineUsersOutputNode(final InputNode command, final List<String> result) {
      this.setCommand(command.getCommand());
      this.setTimestamp(command.getTimestamp());
      this.setResult(new ArrayList<>(result));
    }
  }
}
