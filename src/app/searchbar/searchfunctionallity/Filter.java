package app.searchbar.searchfunctionallity;

import static app.searchbar.SearchType.PLAYLIST;
import static app.searchbar.SearchType.PODCAST;
import static app.searchbar.SearchType.SONG;

import app.io.nodes.input.InputNode;
import app.searchbar.SearchType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/** Represents a filter used for searching songs, playlists, or podcasts. */
@Getter
@Setter
public final class Filter {
  private final SearchType type;
  private final List<String> activeFilters = new ArrayList<>();
  private String name;
  private String album;
  private List<String> tags;
  private String lyrics;
  private String genre;
  private String releaseYear;
  private Integer releaseYearSearchType;
  private String artist;
  private String owner;
  private String description;
  private String username;

  /**
   * Constructs a Filter object based on the given command.
   *
   * @param command The input command containing the filters.
   */
  public Filter(final InputNode command) {
    switch (SearchType.valueOf(command.getType().toUpperCase())) {
      case PLAYLIST:
        type = PLAYLIST;
        break;
      case PODCAST:
        type = PODCAST;
        break;
      default:
        type = SONG;
        break;
    }

    for (Map.Entry<String, Object> e : command.getFilters().entrySet()) {
      switch (e.getKey()) {
        case "name":
          this.name = (String) e.getValue();
          activeFilters.add("name");
          break;
        case "album":
          this.album = (String) e.getValue();
          activeFilters.add("album");
          break;
        case "tags":
          this.tags = new ArrayList<>();
          this.tags.addAll((Collection<? extends String>) e.getValue());
          activeFilters.add("tags");
          break;
        case "lyrics":
          this.lyrics = (String) e.getValue();
          activeFilters.add("lyrics");
          break;
        case "genre":
          this.genre = (String) e.getValue();
          activeFilters.add("genre");
          break;
        case "releaseYear":
          String value = (String) e.getValue();
          if (value.charAt(0) == '>') {
            this.releaseYearSearchType = 1;
          } else {
            this.releaseYearSearchType = -1;
          }
          this.releaseYear = value.substring(1);
          activeFilters.add("releaseYear");
          break;
        case "artist":
          this.artist = (String) e.getValue();
          activeFilters.add("artist");
          break;
        case "owner":
          this.owner = (String) e.getValue();
          activeFilters.add("owner");
          break;
        case "description":
          this.description = (String) e.getValue();
          activeFilters.add("description");
          break;
        case "username":
          this.username = (String) e.getValue();
          activeFilters.add("username");
          break;
        default:
          throw new IllegalStateException("Unexpected value: " + e.getKey());
      }
    }
  }

  /**
   * Gets the release year as an integer.
   *
   * @return The release year as an integer, or 0 if not available.
   */
  public Integer getReleaseYear() {
    if (releaseYear != null) {
      return Integer.parseInt(releaseYear);
    }
    return 0;
  }
}
