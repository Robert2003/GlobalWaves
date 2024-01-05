package app.commands.executables;

import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import library.Library;
import library.users.User;

public final class Shuffle implements Executable {
  /**
   * Shuffles the playlist or tracks based on the given input command.
   *
   * @param command The input command for shuffling.
   * @return The output node containing the shuffle result.
   */
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return null;
    }

    return user.getAudioPlayer().shuffle(command);
  }
}
