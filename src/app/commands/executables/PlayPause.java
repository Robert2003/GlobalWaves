package app.commands.executables;

import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import library.Library;
import library.users.User;

public final class PlayPause implements Executable {
  /**
   * Toggles between play and pause based on the given input command.
   *
   * @param command The input command for toggling play and pause.
   * @return The output node containing the toggle result.
   */
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return null;
    }

    return user.getAudioPlayer().togglePlayPause(command);
  }
}
