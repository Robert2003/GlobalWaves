package app.searchbar.searchfunctionallity.searchstrategies;

import app.Constants;
import app.helpers.PlaylistVisibilityState;
import app.io.nodes.input.InputNode;
import app.io.nodes.output.SearchOutputNode;
import app.searchbar.searchfunctionallity.Filter;
import app.searchbar.searchfunctionallity.Searchable;
import java.util.List;
import java.util.stream.Collectors;
import library.Library;
import library.entities.audio.AudioEntity;
import library.entities.audio.audio.collections.Playlist;

/**
 * This class represents a search strategy for playlists. It implements the Searchable interface and
 * provides methods for searching playlists based on different criteria.
 */
public final class PlaylistSearchStrategy implements Searchable {
  @Override
  public SearchOutputNode search(final InputNode command) {
    // Search for playlists in user's playlists
    List<Playlist> playlists = Library.getInstance().getPlaylists();

    if (playlists == null) {
      return new SearchOutputNode(command, Constants.SEARCH_PLAYLIST_ERROR_MESSAGE);
    }

    Filter filter = new Filter(command);

    List<AudioEntity> outputList =
        playlists.stream()
            .filter(playlist -> isMatch(playlist, filter))
            .filter(playlist -> hasAccess(playlist, command))
            .limit(Constants.PRINT_LIMIT)
            .collect(Collectors.toList());

    return new SearchOutputNode(
        command,
        outputList,
        Constants.SEARCH_NO_ERROR_BEFORE_SIZE_MESSAGE
            + outputList.size()
            + Constants.SEARCH_NO_ERROR_AFTER_SIZE_MESSAGE);
  }

  @Override
  public boolean isMatch(final AudioEntity entity, final Filter query) {
    return (!query.getActiveFilters().contains("name") || searchName((Playlist) entity, query))
        && (!query.getActiveFilters().contains("owner") || searchOwner((Playlist) entity, query));
  }

  /**
   * Searches for a playlist based on its name.
   *
   * @param playlist The playlist to be searched.
   * @param query The filter query.
   * @return true if the playlist's name matches the query, false otherwise.
   */
  private boolean searchName(final Playlist playlist, final Filter query) {
    if (query.getName() == null) {
      return false;
    }
    if (playlist.getName() == null) {
      return false;
    }

    return playlist.getName().toLowerCase().indexOf(query.getName().toLowerCase()) == 0;
  }

  /**
   * Searches for a playlist based on its owner.
   *
   * @param playlist The playlist to be searched.
   * @param query The filter query.
   * @return true if the playlist's owner matches the query, false otherwise.
   */
  private boolean searchOwner(final Playlist playlist, final Filter query) {
    if (query.getOwner() == null) {
      return false;
    }
    if (playlist.getOwner() == null) {
      return false;
    }

    return playlist.getOwner().equalsIgnoreCase(query.getOwner());
  }

  /**
   * Checks if the user that has performed the search has access to the playlist.
   *
   * @param playlist The playlist to be checked.
   * @param command The input command.
   * @return true if the user has access to the playlist, false otherwise.
   */
  private boolean hasAccess(final Playlist playlist, final InputNode command) {
    return playlist.getVisibility() != PlaylistVisibilityState.PRIVATE
        || playlist.getOwner().equalsIgnoreCase(command.getUsername());
  }
}
