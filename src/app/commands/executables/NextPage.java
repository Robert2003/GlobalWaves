package app.commands.executables;

import app.Constants;
import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.pagination.Page;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public class NextPage implements Executable {
  @Override
  public Node execute(InputNode command) {
    User user = library.Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return null;
    }

    Page page = user.getPageHistory().getNextPage();

    if (page == null) {
      return new NextPageOutputNode(command, "There are no pages left to go forward.");
    }

    user.changePage(page, false);
    return new NextPageOutputNode(
        command,
        "The user " + user.getUsername() + " has navigated successfully to the next page.");
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private final class NextPageOutputNode extends Node {
    private String user;
    private String message;

    NextPageOutputNode(final InputNode command, final String message) {
      this.setCommand(command.getCommand());
      this.setTimestamp(command.getTimestamp());
      this.setUser(command.getUsername());
      this.setMessage(message);
    }
  }
}
