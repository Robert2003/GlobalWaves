package app.commands.executables;

import app.commands.Executable;
import app.helpers.PlaylistManager;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import library.Library;
import library.users.User;

public final class CreatePlaylist implements Executable {
  /**
   * Creates a playlist with the given input command.
   *
   * @param command The input command for creating a playlist.
   * @return The output node containing the playlist creation result.
   */
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return null;
    }

    return PlaylistManager.getInstance().createPlaylist(command);
  }
}
