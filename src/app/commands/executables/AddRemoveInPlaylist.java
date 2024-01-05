package app.commands.executables;

import app.commands.Executable;
import app.helpers.PlaylistManager;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import library.Library;
import library.users.User;

public final class AddRemoveInPlaylist implements Executable {
  /**
   * Adds or removes tracks in a playlist based on the given input command.
   *
   * @param command The input command for adding or removing tracks in a playlist.
   * @return The output node containing the add or remove result.
   */
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return null;
    }

    return PlaylistManager.getInstance().addRemoveInPlaylist(command);
  }
}
