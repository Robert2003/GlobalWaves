package app.commands.executables;

import static app.Constants.IS_OFFLINE_ERROR_MESSAGE;
import static app.searchbar.SearchType.PODCAST;

import app.Constants;
import app.commands.Executable;
import app.helpers.ConnectionStatus;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.io.nodes.output.PlayerOutputNode;
import library.Library;
import library.entities.audio.audioFiles.Song;
import library.users.User;

public final class Like implements Executable {
  /**
   * Likes or unlikes the currently loaded song based on the given input command.
   *
   * @param command The input command for liking or unliking.
   * @return The output node containing the like or unlike result.
   */
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return null;
    }

    if (user.getConnectionStatus() == ConnectionStatus.OFFLINE) {
      return new PlayerOutputNode(command, user.getUsername() + IS_OFFLINE_ERROR_MESSAGE);
    }

    String message;

    if (!user.getAudioPlayer().hasLoadedTrack()) {
      return new PlayerOutputNode(command, Constants.LIKE_ERROR_MESSAGE);
    }

    if (user.getAudioPlayer().getLoadedTrack().getType().equals(PODCAST)) {
      return new PlayerOutputNode(command, Constants.LOADED_SOURCE_NOT_SONG_ERROR);
    }

    Song loadedTrack =
        (Song) user.getAudioPlayer().getTimeManager().getPlayingAudioEntity(user.getAudioPlayer());

    if (user.getLikedSongs().contains(loadedTrack)) {
      user.getLikedSongs().remove(loadedTrack);
      loadedTrack.setNumberOfLikes(loadedTrack.getNumberOfLikes() - 1);
      message = Constants.UNLIKE_NO_ERROR_MESSAGE;
    } else {
      user.getLikedSongs().add(loadedTrack);
      loadedTrack.setNumberOfLikes(loadedTrack.getNumberOfLikes() + 1);
      message = Constants.LIKE_NO_ERROR_MESSAGE;
    }

    return new PlayerOutputNode(command, message);
  }
}
