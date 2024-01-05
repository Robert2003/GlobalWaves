package app.commands.executables;

import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import library.Library;
import library.users.User;

public final class Backward implements Executable {
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
      return null;
    }

    return user.getAudioPlayer().backward(command);
  }
}
