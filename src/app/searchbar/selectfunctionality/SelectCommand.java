package app.searchbar.selectfunctionality;

import app.Constants;
import app.io.nodes.input.InputNode;
import app.io.nodes.output.SearchOutputNode;
import app.io.nodes.output.SelectOutputNode;

public final class SelectCommand {

  /**
   * Selects a node based on the given command and last search.
   *
   * @param command the input command
   * @param lastSearch the last search node
   * @return the selected node
   */
  public SelectOutputNode select(final InputNode command, final SearchOutputNode lastSearch) {
    String message =
        Constants.SELECT_NO_ERROR_MESSAGE
            + lastSearch.getResultedEntities().get(command.getItemNumber() - 1).getName()
            + Constants.PHRASE_TERMINATOR;
    return new SelectOutputNode(command, message, lastSearch);
  }
}
