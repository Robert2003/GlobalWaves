package app.searchbar.searchfunctionallity.searchstrategies;

import app.Constants;
import app.io.nodes.input.InputNode;
import app.io.nodes.output.SearchOutputNode;
import app.searchbar.searchfunctionallity.Filter;
import app.searchbar.searchfunctionallity.Searchable;
import java.util.List;
import java.util.stream.Collectors;
import library.Library;
import library.entities.audio.AudioEntity;
import library.entities.audio.audio.collections.Podcast;

/**
 * This class represents a search strategy for podcasts. It implements the Searchable interface and
 * provides methods for searching podcasts based on different criteria.
 */
public final class PodcastSearchStrategy implements Searchable {
  @Override
  public SearchOutputNode search(final InputNode command) {
    // Search for podcasts in LibraryInput
    List<Podcast> podcasts = Library.getInstance().getPodcasts();
    Filter filter = new Filter(command);

    List<AudioEntity> outputList =
        podcasts.stream()
            .filter(podcast -> isMatch(podcast, filter))
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
    return (!query.getActiveFilters().contains("name") || searchName((Podcast) entity, query))
        && (!query.getActiveFilters().contains("owner") || searchOwner((Podcast) entity, query));
  }

  /**
   * Searches for a match based on the podcast name and the filter query.
   *
   * @param podcast The podcast to be checked.
   * @param query The filter query.
   * @return true if there is a match, false otherwise.
   */
  private boolean searchName(final Podcast podcast, final Filter query) {
    if (query.getName() == null) {
      return false;
    }
    if (podcast.getName() == null) {
      return false;
    }

    return podcast.getName().toLowerCase().indexOf(query.getName().toLowerCase()) == 0;
  }

  /**
   * Searches for a match based on the podcast owner and the filter query.
   *
   * @param podcast The podcast to be checked.
   * @param query The filter query.
   * @return true if there is a match, false otherwise.
   */
  private boolean searchOwner(final Podcast podcast, final Filter query) {
    if (query.getOwner() == null) {
      return false;
    }
    if (podcast.getOwner() == null) {
      return false;
    }

    return podcast.getOwner().toLowerCase().indexOf(query.getOwner().toLowerCase()) == 0;
  }
}
