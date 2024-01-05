package app.commands.executables;

import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import library.Library;
import library.users.User;

public final class Load implements Executable {
  /**
   * Loads audio file from a given input command.
   *
   * @param command The input command to load.
   * @return The output node containing the loading result.
   */
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return null;
    }

    return user.getAudioPlayer().load(command);
  }
}
