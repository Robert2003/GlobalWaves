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
import library.entities.audio.audio.collections.Album;
import library.users.User;

/**
 * This class represents a search strategy for songs. It implements the Searchable interface and
 * provides methods for searching songs based on different criteria.
 */
public final class AlbumSearchStrategy implements Searchable {
  @Override
  public SearchOutputNode search(final InputNode command) {
    // Search for songs in LibraryInput
    List<Album> albums = Library.getInstance().getAlbums();
    // Implement search logic for songs
    Filter filter = new Filter(command);

    List<AudioEntity> outputList =
        albums.stream()
            .filter(album -> isMatch(album, filter))
            .sorted(
                (o1, o2) -> {
                  int artist1Index = Library.getInstance().getUserIndexByName(o1.getOwner());
                  int artist2Index = Library.getInstance().getUserIndexByName(o2.getOwner());

                  return artist1Index - artist2Index;
                })
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
    return (!query.getActiveFilters().contains("name") || searchName((Album) entity, query))
        && (!query.getActiveFilters().contains("owner") || searchOwner((Album) entity, query))
        && (!query.getActiveFilters().contains("description")
            || searchDescription((Album) entity, query));
  }

  private boolean searchName(final Album album, final Filter query) {
    if (query.getName() == null) {
      return false;
    }
    if (album.getName() == null) {
      return false;
    }

    return album.getName().toLowerCase().indexOf(query.getName().toLowerCase()) == 0;
  }

  /**
   * Searches for a album based on its owner.
   *
   * @param album The album to be searched.
   * @param query The filter query.
   * @return true if the album's owner matches the query, false otherwise.
   */
  private boolean searchOwner(final Album album, final Filter query) {
    if (query.getOwner() == null) {
      return false;
    }
    if (album.getOwner() == null) {
      return false;
    }

    return album.getOwner().toLowerCase().indexOf(query.getOwner().toLowerCase()) == 0;
  }

  private boolean searchDescription(final Album album, final Filter query) {
    if (query.getOwner() == null) {
      return false;
    }
    if (album.getOwner() == null) {
      return false;
    }

    return album.getDescription().toLowerCase().indexOf(query.getDescription().toLowerCase()) == 0;
  }
}
