package app.commands.executables;

import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import library.Library;
import library.users.User;

public final class Prev implements Executable {
  /**
   * Skips to the previous playlist item (video or song) based on the given input command.
   *
   * @param command The input command for skipping to the previous playlist item.
   * @return The output node containing the skip to previous result.
   */
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return null;
    }

    return user.getAudioPlayer().prev(command);
  }
}
