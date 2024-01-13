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
import library.entities.audio.audioFiles.Song;

/**
 * This class represents a search strategy for songs. It implements the Searchable interface and
 * provides methods for searching songs based on different criteria.
 */
public final class SongSearchStrategy implements Searchable {
  @Override
  public SearchOutputNode search(final InputNode command) {
    // Search for songs in LibraryInput
    List<Song> songs = Library.getInstance().getSongs();
    // Implement search logic for songs
    Filter filter = new Filter(command);

    List<AudioEntity> outputList =
        songs.stream()
            .filter(song -> isMatch(song, filter))
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
    return (!query.getActiveFilters().contains("name") || searchName((Song) entity, query))
        && (!query.getActiveFilters().contains("album") || searchAlbum((Song) entity, query))
        && (!query.getActiveFilters().contains("lyrics") || searchLyrics((Song) entity, query))
        && (!query.getActiveFilters().contains("genre") || searchGenre((Song) entity, query))
        && (!query.getActiveFilters().contains("artist") || searchArtist((Song) entity, query))
        && (!query.getActiveFilters().contains("tags") || searchTags((Song) entity, query))
        && (!query.getActiveFilters().contains("releaseYear") || searchYear((Song) entity, query));
  }

  private boolean searchName(final Song song, final Filter query) {
    if (query.getName() == null) {
      return false;
    }
    if (song.getName() == null) {
      return false;
    }

//    return song.getName().toLowerCase().indexOf(query.getName().toLowerCase()) == 0;
    return song.getName().regionMatches(true, 0, query.getName(), 0, query.getName().length());
  }

  private boolean searchAlbum(final Song song, final Filter query) {
    if (query.getAlbum() == null) {
      return false;
    }
    if (song.getAlbum() == null) {
      return false;
    }

    return song.getAlbum().equalsIgnoreCase(query.getAlbum());
  }

  private boolean searchLyrics(final Song song, final Filter query) {
    if (query.getLyrics() == null) {
      return false;
    }
    if (song.getLyrics() == null) {
      return false;
    }

    return song.getLyrics().toLowerCase().contains(query.getLyrics().toLowerCase());
  }

  private boolean searchGenre(final Song song, final Filter query) {
    if (query.getGenre() == null) {
      return false;
    }
    if (song.getGenre() == null) {
      return false;
    }

    return song.getGenre().equalsIgnoreCase(query.getGenre());
  }

  private boolean searchArtist(final Song song, final Filter query) {
    if (query.getArtist() == null) {
      return false;
    }
    if (song.getArtist() == null) {
      return false;
    }

    return song.getArtist().equalsIgnoreCase(query.getArtist());
  }

  private boolean searchTags(final Song song, final Filter query) {
    if (query.getTags() == null || query.getTags().isEmpty()) {
      return false;
    }
    if (song.getTags() == null || song.getTags().isEmpty()) {
      return false;
    }

    return song.getTags().containsAll(query.getTags());
  }

  private boolean searchYear(final Song song, final Filter command) {
    if (command.getReleaseYearSearchType() == null) {
      return false;
    }
    if (song.getReleaseYear() == null) {
      return false;
    }

    if (command.getReleaseYearSearchType() < 0) {
      return song.getReleaseYear() < command.getReleaseYear();
    }
    return song.getReleaseYear() > command.getReleaseYear();
  }
}
