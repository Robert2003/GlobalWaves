package app.commands.executables;

import app.Constants;
import app.commands.Executable;
import app.helpers.PlaylistVisibilityState;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.io.nodes.output.Top5OutputNode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import library.Library;
import library.entities.audio.AudioEntity;
import library.entities.audio.audio.collections.Playlist;

public final class GetTop5Playlists implements Executable {
  /**
   * Returns the top 5 playlists based on followers and creation date.
   *
   * @param command The input command.
   * @return The top 5 playlists.
   */
  @Override
  public Node execute(final InputNode command) {
    List<AudioEntity> top5Playlists =
        Library.getInstance().getPlaylists().stream()
            .filter(playlist -> playlist.getVisibility() == PlaylistVisibilityState.PUBLIC)
            .sorted(
                Comparator.comparingInt(Playlist::getFollowers)
                    .reversed()
                    .thenComparing(Playlist::getCreationDate))
            .limit(Constants.PRINT_LIMIT)
            .collect(Collectors.toList());

    return new Top5OutputNode(command, top5Playlists);
  }
}
