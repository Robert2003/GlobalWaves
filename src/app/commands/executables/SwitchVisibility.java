package app.commands.executables;

import app.Constants;
import app.commands.Executable;
import app.helpers.PlaylistVisibilityState;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.io.nodes.output.OutputNode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.Library;
import library.entities.audio.audio.collections.Playlist;
import library.users.User;

public final class SwitchVisibility implements Executable {
  /**
   * Switches the visibility of a playlist and if it is being set on private, the playlist is being
   * removed from all users that have followed it.
   *
   * @param command The input command.
   * @return The output node containing the result of the visibility switch.
   */
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());
    if (user == null) {
      return null;
    }

    if (command.getPlaylistId() > user.getOwnedPlaylists().size()) {
      return new SwitchVisibilityOutputNode(command, Constants.SWITCH_VISIBILITY_ERROR_MESSAGE);
    }

    Playlist playlist = user.getOwnedPlaylists().get(command.getPlaylistId() - 1);
    playlist.switchVisibility();

    if (playlist.getVisibility() == PlaylistVisibilityState.PRIVATE) {
      for (User u : Library.getInstance().getUsers()) {
        u.getFollowedPlaylists().remove(playlist);
      }
    }

    return new SwitchVisibilityOutputNode(
        command,
        Constants.SWITCH_VISIBILITY_NO_ERROR_MESSAGE
            + playlist.getVisibility().getDescription()
            + Constants.PHRASE_TERMINATOR);
  }

  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private final class SwitchVisibilityOutputNode extends OutputNode {
    SwitchVisibilityOutputNode(final InputNode command, final String message) {
      super(command, message);
    }
  }
}
