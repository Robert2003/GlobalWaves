package app.commands.executables;

import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import library.Library;
import library.users.User;

public final class Status implements Executable {
  /**
   * Gets the status of the audio player based on the given input command.
   *
   * @param command The input command for getting the status.
   * @return The output node containing the status result.
   */
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return null;
    }

    return user.getAudioPlayer().status(command);
  }
}
