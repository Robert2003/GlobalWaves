package app.commands.executables;

import static app.Constants.IS_OFFLINE_ERROR_MESSAGE;

import app.commands.Executable;
import app.helpers.ConnectionStatus;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.io.nodes.output.SearchOutputNode;
import library.Library;
import library.users.User;

public final class Search implements Executable {
  /**
   * Executes a search based on the given input command.
   *
   * @param command The input command to execute.
   * @return The result of the search execution.
   */
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return null;
    }

    if (user.getConnectionStatus() == ConnectionStatus.OFFLINE) {
      return new SearchOutputNode(command, user.getUsername() + IS_OFFLINE_ERROR_MESSAGE);
    }

    return user.getSearchBar().executeSearch(command);
  }
}
