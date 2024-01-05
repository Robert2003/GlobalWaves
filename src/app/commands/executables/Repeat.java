package app.commands.executables;

import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import library.Library;
import library.users.User;

public final class Repeat implements Executable {
  /**
   * Repeats the playback of the audio based on the given input command.
   *
   * @param command The input command for repeating playback.
   * @return The output node containing the repeat result.
   */
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return null;
    }

    return user.getAudioPlayer().repeat(command);
  }
}
