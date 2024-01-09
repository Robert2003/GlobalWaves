package app.commands.executables;

import static app.Constants.CHANGE_PAGE_ERROR_MESSAGE;

import app.commands.Executable;
import app.helpers.UserType;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.pagination.enums.PageType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.Library;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public final class ChangePage implements Executable {
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user.getUserType() != UserType.NORMAL) {
      return null;
    }

    PageType pageType = PageType.DEFAULT;
    for (PageType type : PageType.values()) {
      if (type.getDescription().equalsIgnoreCase(command.getNextPage())) {
        pageType = type;
        break;
      }
    }

    if (pageType == PageType.DEFAULT) {
      return new ChangePageOutputNode(
          command, user.getUsername() + CHANGE_PAGE_ERROR_MESSAGE);
    }

    user.changePage(pageType, true);
    return new ChangePageOutputNode(
        command, user.getUsername() + " accessed " + command.getNextPage() + " successfully.");
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private final class ChangePageOutputNode extends Node {
    private String user;
    private String message;

    ChangePageOutputNode(final InputNode command, final String message) {
      this.setCommand(command.getCommand());
      this.setTimestamp(command.getTimestamp());
      this.setUser(command.getUsername());
      this.setMessage(message);
    }
  }
}
