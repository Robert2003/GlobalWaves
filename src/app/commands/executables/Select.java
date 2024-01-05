package app.commands.executables;

import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import library.Library;
import library.users.User;

public final class Select implements Executable {
  /**
   * Executes a select operation based on the given input command.
   *
   * @param command The input command to execute.
   * @return The result of the select operation.
   */
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return null;
    }

    return user.getSearchBar().executeSelect(command);
  }
}
