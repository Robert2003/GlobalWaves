package app.commands.executables;

import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import library.Library;
import library.users.User;

public final class Forward implements Executable {
  /**
   * Goes to the next track based on the given input command.
   *
   * @param command The input command for going to the next track.
   * @return The output node containing the next track result.
   */
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return null;
    }

    return user.getAudioPlayer().forward(command);
  }
}
