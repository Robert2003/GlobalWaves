package app.searchbar.searchfunctionallity;

import app.searchbar.SearchType;
import app.searchbar.searchfunctionallity.searchstrategies.AlbumSearchStrategy;
import app.searchbar.searchfunctionallity.searchstrategies.ArtistSearchStrategy;
import app.searchbar.searchfunctionallity.searchstrategies.HostSearchStrategy;
import app.searchbar.searchfunctionallity.searchstrategies.PlaylistSearchStrategy;
import app.searchbar.searchfunctionallity.searchstrategies.PodcastSearchStrategy;
import app.searchbar.searchfunctionallity.searchstrategies.SongSearchStrategy;

/**
 * The SearchStrategyFactory class is responsible for creating search strategies based on the given
 * search type.
 */
public final class SearchStrategyFactory {
  private SearchStrategyFactory() {
  }

  /**
   * Creates a search strategy based on the given search type.
   *
   * @param searchType the type of search strategy to create
   * @return the created search strategy
   * @throws IllegalArgumentException if the search type is unknown
   */
  public static Searchable createSearchStrategy(final SearchType searchType) {
    return switch (searchType) {
      case SONG -> new SongSearchStrategy();
      case PLAYLIST -> new PlaylistSearchStrategy();
      case PODCAST -> new PodcastSearchStrategy();
      case ALBUM -> new AlbumSearchStrategy();
      case ARTIST -> new ArtistSearchStrategy();
      case HOST -> new HostSearchStrategy();
      default -> throw new IllegalArgumentException("Unknown search type: " + searchType);
    };
  }
}
