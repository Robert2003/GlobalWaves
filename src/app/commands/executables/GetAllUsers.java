package app.commands.executables;

import app.commands.Executable;
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

public final class GetAllUsers implements Executable {
  @Override
  public Node execute(final InputNode command) {
    List<String> users = new ArrayList<>();

    List<User> normalUsers =
        Library.getInstance().getUsers().stream()
            .filter(user -> user.getUserType() == UserType.NORMAL)
            .toList();

    List<User> artists =
        Library.getInstance().getUsers().stream()
            .filter(user -> user.getUserType() == UserType.ARTIST)
            .toList();

    List<User> hosts =
        Library.getInstance().getUsers().stream()
            .filter(user -> user.getUserType() == UserType.HOST)
            .toList();

    normalUsers.forEach(user -> users.add(user.getUsername()));
    artists.forEach(user -> users.add(user.getUsername()));
    hosts.forEach(user -> users.add(user.getUsername()));

    return new GetAllUsersOutputNode(command, users);
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private class GetAllUsersOutputNode extends Node {
    private List<String> result;

    /**
     * Constructs an OutputNode object with the given command and message.
     *
     * @param command The input command node.
     */
    GetAllUsersOutputNode(final InputNode command, final List<String> result) {
      this.setCommand(command.getCommand());
      this.setTimestamp(command.getTimestamp());
      this.setResult(new ArrayList<>(result));
    }
  }
}
