package app.audio.player.timemanagement;

import app.audio.player.timemanagement.concretestrategies.AlbumTimeManagerStrategy;
import app.audio.player.timemanagement.concretestrategies.PlaylistTimeManagerStrategy;
import app.audio.player.timemanagement.concretestrategies.PodcastTimeManagerStrategy;
import app.audio.player.timemanagement.concretestrategies.SongTimeManagerStrategy;
import app.io.nodes.input.InputNode;
import library.Library;
import library.entities.audio.AudioEntity;
import library.users.User;

/**
 * The TimeManagerStrategyFactory class is responsible for creating time manager strategies based on
 * the selected entity type.
 */
public final class TimeManagerStrategyFactory {
  private TimeManagerStrategyFactory() {
  }

  /**
   * Creates a time manager strategy based on the selected entity type.
   *
   * @param command the input command
   * @return the time manager strategy
   * @throws IllegalArgumentException if the selected entity type is unknown
   */
  public static TimeManagerStrategy createTimeManagerStrategy(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());
    assert user != null;
    AudioEntity selectedEntity = user.getSearchBar().getSelectedTrack().getSelectedEntity();

    return switch (selectedEntity.getType()) {
      case SONG -> new SongTimeManagerStrategy(command.getTimestamp());
      case PLAYLIST -> new PlaylistTimeManagerStrategy(command.getTimestamp());
      case PODCAST -> new PodcastTimeManagerStrategy(command.getTimestamp());
      case ALBUM -> new AlbumTimeManagerStrategy(command.getTimestamp());
      default -> throw new IllegalArgumentException(
          "Unknown time management type: " + selectedEntity.getType());
    };
  }
}
