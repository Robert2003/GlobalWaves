package app.searchbar.searchfunctionallity.searchstrategies;

import app.Constants;
import app.helpers.UserType;
import app.io.nodes.input.InputNode;
import app.io.nodes.output.SearchOutputNode;
import app.searchbar.searchfunctionallity.Filter;
import app.searchbar.searchfunctionallity.Searchable;
import java.util.List;
import library.Library;
import library.entities.audio.AudioEntity;
import library.users.User;

/**
 * This class represents a search strategy for playlists. It implements the Searchable interface and
 * provides methods for searching playlists based on different criteria.
 */
public final class ArtistSearchStrategy implements Searchable {
  @Override
  public SearchOutputNode search(final InputNode command) {
    // Search for users in user's users
    List<User> users = Library.getInstance().getUsers();

    if (users == null) {
      return new SearchOutputNode(command, Constants.SEARCH_PLAYLIST_ERROR_MESSAGE);
    }

    Filter filter = new Filter(command);

    List<User> outputList =
        users.stream()
            .filter(user -> isMatch(user, filter))
            .filter(user -> user.getUserType() == UserType.ARTIST)
            .limit(Constants.PRINT_LIMIT)
            .toList();

    return new SearchOutputNode(
        command,
        outputList,
        Constants.SEARCH_NO_ERROR_BEFORE_SIZE_MESSAGE
            + outputList.size()
            + Constants.SEARCH_NO_ERROR_AFTER_SIZE_MESSAGE);
  }

  @Override
  public boolean isMatch(final AudioEntity entity, final Filter query) {
    return false;
  }

  private boolean isMatch(final User user, final Filter query) {
    return (!query.getActiveFilters().contains("name") || searchName(user, query));
  }

  private boolean searchName(final User user, final Filter query) {
    if (query.getName() == null) {
      return false;
    }
    if (user.getUsername() == null) {
      return false;
    }

    return user.getUsername().toLowerCase().indexOf(query.getName().toLowerCase()) == 0;
  }
}
