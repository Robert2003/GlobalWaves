package app.commands.executables;

import static app.searchbar.SearchType.PLAYLIST;

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

public final class Follow implements Executable {
  /**
   * Follows or unfollows a playlist based on the given input command.
   *
   * @param command The input command for following or unfollowing a playlist.
   * @return The output node containing the follow or unfollow result.
   */
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return null;
    }

    if (!user.getSearchBar().hasSelectedTrack()) {
      return new FollowPlaylistOutputNode(command, Constants.FOLLOW_ERROR_MESSAGE);
    }

    if (!user.getSearchBar().getSelectedTrack().getSelectedEntity().getType().equals(PLAYLIST)) {
      return new FollowPlaylistOutputNode(command, Constants.SELECTED_SOURCE_NOT_PLAYLIST_ERROR);
    }

    Playlist selectedPlaylist =
        (Playlist) user.getSearchBar().getSelectedTrack().getSelectedEntity();
    if (selectedPlaylist.getOwner().equals(user.getUsername())) {
      return new FollowPlaylistOutputNode(command, Constants.FOLLOW_OWN_PLAYLIST_ERROR);
    }

    String message = null;
    if (user.getFollowedPlaylists().contains(selectedPlaylist)) {
      user.getFollowedPlaylists().remove(selectedPlaylist);
      selectedPlaylist.setFollowers(selectedPlaylist.getFollowers() - 1);
      message = Constants.UNFOLLOW_NO_ERROR_MESSAGE;
    } else if (selectedPlaylist.getVisibility() == PlaylistVisibilityState.PUBLIC) {
      user.getFollowedPlaylists().add(selectedPlaylist);
      selectedPlaylist.setFollowers(selectedPlaylist.getFollowers() + 1);
      message = Constants.FOLLOW_NO_ERROR_MESSAGE;
    }

    return new FollowPlaylistOutputNode(command, message);
  }

  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private final class FollowPlaylistOutputNode extends OutputNode {

    FollowPlaylistOutputNode(final InputNode command, final String message) {
      super(command, message);
    }
  }
}
